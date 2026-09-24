package co.com.fcv.training.citas.adapter.security;

import co.com.fcv.training.citas.application.AuthFailure;
import co.com.fcv.training.citas.application.Ports;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Component
public class JwtTokens implements Ports.Tokens {
    private final JwtEncoder accessEncoder;
    private final JwtEncoder refreshEncoder;
    private final JwtDecoder accessDecoder;
    private final JwtDecoder refreshDecoder;
    private final Clock clock;
    private final String issuer;
    private final Duration accessDuration;
    private final Duration refreshDuration;

    public JwtTokens(@Value("${app.jwt.access-secret}") String accessSecret,
                     @Value("${app.jwt.refresh-secret}") String refreshSecret,
                     @Value("${app.jwt.issuer}") String issuer,
                     @Value("${app.jwt.access-minutes}") long accessMinutes,
                     @Value("${app.jwt.refresh-days}") long refreshDays, Clock clock) {
        if (accessSecret.getBytes(StandardCharsets.UTF_8).length < 32 ||
                refreshSecret.getBytes(StandardCharsets.UTF_8).length < 32 || accessSecret.equals(refreshSecret)) {
            throw new IllegalStateException("Se requieren secretos JWT distintos de al menos 32 bytes");
        }
        this.clock = clock;
        this.issuer = issuer;
        this.accessDuration = Duration.ofMinutes(accessMinutes);
        this.refreshDuration = Duration.ofDays(refreshDays);
        if (accessDuration.isNegative() || accessDuration.isZero() || refreshDuration.isNegative() || refreshDuration.isZero())
            throw new IllegalStateException("Vigencias JWT inválidas");
        var accessKey = new SecretKeySpec(accessSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        var refreshKey = new SecretKeySpec(refreshSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        this.accessEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(accessKey));
        this.refreshEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(refreshKey));
        NimbusJwtDecoder access = NimbusJwtDecoder.withSecretKey(accessKey).macAlgorithm(MacAlgorithm.HS256).build();
        access.setJwtValidator(jwt -> {
            var result = JwtValidators.createDefaultWithIssuer(issuer).validate(jwt);
            if (result.hasErrors()) return result;
            return "access".equals(jwt.getClaimAsString("token_use"))
                    ? org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.success()
                    : org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.failure(
                            new org.springframework.security.oauth2.core.OAuth2Error("invalid_token", "Tipo de token incorrecto", null));
        });
        this.accessDecoder = access;
        NimbusJwtDecoder refresh = NimbusJwtDecoder.withSecretKey(refreshKey).macAlgorithm(MacAlgorithm.HS256).build();
        refresh.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer));
        this.refreshDecoder = refresh;
    }

    public JwtDecoder accessDecoder() { return accessDecoder; }
    public long accessSeconds() { return accessDuration.toSeconds(); }

    public String access(Long userId, Set<String> roles) {
        Instant now = clock.instant();
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer(issuer).subject(userId.toString())
                .issuedAt(now).expiresAt(now.plus(accessDuration)).claim("token_use", "access")
                .claim("roles", roles.stream().sorted().toList()).build();
        return accessEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
    }

    public Ports.IssuedRefresh refresh(Long userId) {
        Instant now = clock.instant();
        Instant expiry = now.plus(refreshDuration);
        String jti = UUID.randomUUID().toString();
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer(issuer).subject(userId.toString())
                .id(jti).issuedAt(now).expiresAt(expiry).claim("token_use", "refresh").build();
        String value = refreshEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
        return new Ports.IssuedRefresh(value, jti, expiry);
    }

    public Ports.RefreshIdentity readRefresh(String token) {
        try {
            Jwt jwt = refreshDecoder.decode(token);
            if (!"refresh".equals(jwt.getClaimAsString("token_use")) || jwt.getId() == null) throw new AuthFailure();
            return new Ports.RefreshIdentity(Long.valueOf(jwt.getSubject()), jwt.getId());
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthFailure();
        }
    }
}
