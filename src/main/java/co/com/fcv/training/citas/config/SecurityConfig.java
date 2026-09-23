package co.com.fcv.training.citas.config;

import co.com.fcv.training.citas.adapter.security.JwtTokens;
import co.com.fcv.training.citas.adapter.web.ProblemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableMethodSecurity
class SecurityConfig {
    @Bean FilterRegistrationBean<AuthRequestGuard> guardRegistration(AuthRequestGuard guard) {
        FilterRegistrationBean<AuthRequestGuard> registration = new FilterRegistrationBean<>(guard);
        registration.setEnabled(false);
        return registration;
    }

    @Bean SecurityFilterChain security(HttpSecurity http, JwtTokens tokens, AuthRequestGuard guard,
                                       ProblemWriter problems, CorsConfigurationSource corsConfigurationSource) throws Exception {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            return roles == null ? List.<GrantedAuthority>of()
                    : roles.stream().map(r -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + r)).toList();
        });
        return http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/catalogs/plans").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(guard, UsernamePasswordAuthenticationFilter.class)
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.decoder(tokens.accessDecoder()).jwtAuthenticationConverter(converter))
                        .authenticationEntryPoint((req, res, ex) -> problems.write(res, 401, "No autenticado"))
                        .accessDeniedHandler((req, res, ex) -> problems.write(res, 403, "Acceso denegado")))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, error) -> problems.write(res, 401, "No autenticado"))
                        .accessDeniedHandler((req, res, error) -> problems.write(res, 403, "Acceso denegado")))
                .build();
    }

    @Bean CorsConfigurationSource corsConfigurationSource(@Value("${app.cors.frontend-origin}") String origin) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(origin));
        config.setAllowedMethods(List.of("POST", "GET", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-Requested-With"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}
