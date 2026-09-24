package co.com.fcv.training.citas;

import co.com.fcv.training.citas.adapter.security.JwtTokens;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.UUID;
import java.time.Instant;
import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;
import java.util.concurrent.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Import(AuthIntegrationTest.RoleProbe.class)
class AuthIntegrationTest {
    @Container static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4");
    private static final String ACCESS_KEY = UUID.randomUUID().toString() + UUID.randomUUID();
    private static final String REFRESH_KEY = UUID.randomUUID().toString() + UUID.randomUUID();

    @DynamicPropertySource static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("app.jwt.access-secret", () -> ACCESS_KEY);
        registry.add("app.jwt.refresh-secret", () -> REFRESH_KEY);
        registry.add("app.cookie.secure", () -> true);
        registry.add("app.cookie.same-site", () -> "None");
    }

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Autowired JdbcTemplate jdbc;
    @Autowired JwtTokens jwt;

    private String uniqueEmail() { return "user-" + UUID.randomUUID() + "@example.test"; }
    private String uniqueDoc() { return UUID.randomUUID().toString(); }
    private String registration(String email, String doc) {
        return """
            {"firstName":"Ana","lastName":"Prueba","documentType":"cc","documentNumber":"%s","email":"%s","phone":"3000000000","password":"SyntheticPass123!"}
            """.formatted(doc, email);
    }
    private void register(String email, String doc) throws Exception {
        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(registration(email, doc)))
                .andExpect(status().isCreated());
    }
    @Test void registrationCreatesOptionalCurrentInsuranceAffiliation() throws Exception {
        Long planId = jdbc.queryForObject("select id from eps_plans where active=true limit 1", Long.class);
        String email = uniqueEmail();
        String body = mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                {"firstName":"Ana","lastName":"Plan","documentType":"CC","documentNumber":"%s","email":"%s","phone":"3000000000","password":"SyntheticPass123!","insurancePlanId":%d}
                """.formatted(uniqueDoc(), email, planId))).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        Long userId = mapper.readTree(body).get("id").asLong();
        assertThat(jdbc.queryForObject("select count(*) from user_insurance_affiliations where user_id=? and plan_id=? and is_current=true", Integer.class, userId, planId)).isEqualTo(1);
    }
    private org.springframework.test.web.servlet.ResultActions login(String email, String password) throws Exception {
        return mvc.perform(post("/api/v1/auth/login").header("X-Requested-With", "XMLHttpRequest")
                .contentType(MediaType.APPLICATION_JSON).content("""
                    {"email":"%s","password":"%s"}
                    """.formatted(email, password)));
    }

    @Test void registrationUsesUniqueIdentityAndBcrypt() throws Exception {
        String email = uniqueEmail(); String doc = uniqueDoc();
        String body = mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(registration("  " + email.toUpperCase() + "  ", doc))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andReturn().getResponse().getContentAsString();
        assertThat(mapper.readTree(body).get("email").asText()).isEqualTo(email);
        String hash = jdbc.queryForObject("select password_hash from users where email = ?", String.class, email);
        assertThat(hash).startsWith("$2").isNotEqualTo("SyntheticPass123!");
        assertThat(jdbc.queryForObject("select count(*) from user_roles ur join roles r on ur.role_id=r.id where r.code='USER'", Integer.class)).isGreaterThan(0);

        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(registration(email, uniqueDoc()))).andExpect(status().isConflict());
        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(registration(uniqueEmail(), doc))).andExpect(status().isConflict());
        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(registration(uniqueEmail(), doc).replace("\"documentType\":\"cc\"", "\"documentType\":\"ce\"")))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(registration("invalid-email", uniqueDoc()))).andExpect(status().isBadRequest());
        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(registration(uniqueEmail(), uniqueDoc()).replace("\"phone\":\"3000000000\",", "")))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(registration(uniqueEmail(), uniqueDoc()).replace("SyntheticPass123!", "x".repeat(73))))
                .andExpect(status().isBadRequest());
        assertThat(jdbc.queryForObject("select count(*) from users where email = ?", Integer.class, email)).isEqualTo(1);
    }

    @Test void loginRefreshLogoutAndRoles() throws Exception {
        String email = uniqueEmail(); register(email, uniqueDoc());
        var loginResult = login(email, "SyntheticPass123!").andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(900)).andReturn();
        String access = mapper.readTree(loginResult.getResponse().getContentAsString()).get("accessToken").asText();
        String cookie = loginResult.getResponse().getHeader("Set-Cookie");
        assertThat(cookie).contains("HttpOnly", "Secure", "SameSite=None").doesNotContain(access);
        String refresh = loginResult.getResponse().getCookie("refresh_token").getValue();
        assertThat(jwt.accessDecoder().decode(access).getClaimAsStringList("roles")).containsExactly("USER");
        mvc.perform(get("/test/role-user").header("Authorization", "Bearer " + access)).andExpect(status().isOk());
        mvc.perform(get("/test/role-admin").header("Authorization", "Bearer " + access)).andExpect(status().isForbidden());
        mvc.perform(get("/test/role-user")).andExpect(status().isUnauthorized());

        var renewed = mvc.perform(post("/api/v1/auth/refresh").header("X-Requested-With", "XMLHttpRequest")
                .cookie(new Cookie("refresh_token", refresh))).andExpect(status().isOk()).andReturn();
        String nextRefresh = renewed.getResponse().getCookie("refresh_token").getValue();
        assertThat(nextRefresh).isNotEqualTo(refresh);
        mvc.perform(post("/api/v1/auth/refresh").header("X-Requested-With", "XMLHttpRequest")
                .cookie(new Cookie("refresh_token", refresh))).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/v1/auth/logout").header("X-Requested-With", "XMLHttpRequest")
                .cookie(new Cookie("refresh_token", nextRefresh))).andExpect(status().isNoContent())
                .andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString("Max-Age=0")));
        mvc.perform(post("/api/v1/auth/refresh").header("X-Requested-With", "XMLHttpRequest")
                .cookie(new Cookie("refresh_token", nextRefresh))).andExpect(status().isUnauthorized());
    }

    @Test void invalidCredentialsTokensAndOrigin() throws Exception {
        String email = uniqueEmail(); register(email, uniqueDoc());
        String unknown = login(uniqueEmail(), "wrong").andExpect(status().isUnauthorized()).andReturn().getResponse().getContentAsString();
        String badPassword = login(email, "wrong").andExpect(status().isUnauthorized()).andReturn().getResponse().getContentAsString();
        assertThat(badPassword).isEqualTo(unknown);
        var valid = login(email, "SyntheticPass123!").andExpect(status().isOk()).andReturn();
        String access = mapper.readTree(valid.getResponse().getContentAsString()).get("accessToken").asText();
        mvc.perform(post("/api/v1/auth/refresh").header("X-Requested-With", "XMLHttpRequest")
                .cookie(new Cookie("refresh_token", access))).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/v1/auth/refresh").header("X-Requested-With", "XMLHttpRequest")
                .cookie(new Cookie("refresh_token", "forged.token.value"))).andExpect(status().isUnauthorized());
        var key = new SecretKeySpec(REFRESH_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        JwtEncoder encoder = new NimbusJwtEncoder(new ImmutableSecret<>(key));
        String expired = encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),
                JwtClaimsSet.builder().issuer("citas-api").subject(UUID.randomUUID().toString())
                        .id(UUID.randomUUID().toString()).issuedAt(Instant.now().minusSeconds(120))
                        .expiresAt(Instant.now().minusSeconds(60)).claim("token_use", "refresh").build())).getTokenValue();
        mvc.perform(post("/api/v1/auth/refresh").header("X-Requested-With", "XMLHttpRequest")
                .cookie(new Cookie("refresh_token", expired))).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/v1/auth/refresh").cookie(new Cookie("refresh_token", "anything")))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/v1/auth/login").header("X-Requested-With", "XMLHttpRequest")
                .header("Origin", "https://attacker.example").contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andExpect(status().isForbidden());
    }

    @Test void browserPreflightAllowsConfiguredFrontendWithCredentials() throws Exception {
        mvc.perform(options("/api/v1/auth/login")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "content-type,x-requested-with"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
    }

    @Test void simultaneousRefreshAllowsOnlyOneRotation() throws Exception {
        String email = uniqueEmail(); register(email, uniqueDoc());
        String token = login(email, "SyntheticPass123!").andExpect(status().isOk())
                .andReturn().getResponse().getCookie("refresh_token").getValue();
        ExecutorService pool = Executors.newFixedThreadPool(2);
        CountDownLatch start = new CountDownLatch(1);
        try {
            Callable<Integer> call = () -> {
                start.await();
                return mvc.perform(post("/api/v1/auth/refresh").header("X-Requested-With", "XMLHttpRequest")
                        .cookie(new Cookie("refresh_token", token))).andReturn().getResponse().getStatus();
            };
            Future<Integer> first = pool.submit(call);
            Future<Integer> second = pool.submit(call);
            start.countDown();
            assertThat(java.util.List.of(first.get(15, TimeUnit.SECONDS), second.get(15, TimeUnit.SECONDS)))
                    .containsExactlyInAnyOrder(200, 401);
        } finally {
            pool.shutdownNow();
        }
    }

    @RestController static class RoleProbe {
        @GetMapping("/test/role-user") @PreAuthorize("hasRole('USER')") String user() { return "ok"; }
        @GetMapping("/test/role-admin") @PreAuthorize("hasRole('ADMIN')") String admin() { return "ok"; }
    }
}
