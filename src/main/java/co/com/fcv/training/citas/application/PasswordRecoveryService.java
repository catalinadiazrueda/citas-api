package co.com.fcv.training.citas.application;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;

@Service
public class PasswordRecoveryService {
    public record Notice(String email, String token, Instant expiresAt) {}
    private final JdbcTemplate jdbc;
    private final Ports.Passwords passwords;
    private final Clock clock;
    private final SecureRandom random = new SecureRandom();
    private final java.util.concurrent.ConcurrentLinkedDeque<Notice> localInbox = new java.util.concurrent.ConcurrentLinkedDeque<>();

    public PasswordRecoveryService(JdbcTemplate jdbc, Ports.Passwords passwords, Clock clock) {
        this.jdbc = jdbc;
        this.passwords = passwords;
        this.clock = clock;
    }

    public void request(String rawEmail) {
        String email = rawEmail == null ? "" : rawEmail.trim().toLowerCase(Locale.ROOT);
        List<Long> ids = jdbc.query("select id from users where email = ? and active = true", (rs, n) -> rs.getLong(1), email);
        if (ids.isEmpty()) return;
        Instant expires = clock.instant().plus(Duration.ofMinutes(30));
        String token = randomToken();
        jdbc.update("insert into password_reset_tokens(user_id, token_hash, expires_at) values (?, ?, ?)", ids.getFirst(), hash(token), expires);
        localInbox.addLast(new Notice(email, token, expires));
        while (localInbox.size() > 50) localInbox.pollFirst();
    }

    public void reset(String rawToken, String newPassword) {
        if (rawToken == null || rawToken.isBlank() || newPassword == null || newPassword.getBytes(StandardCharsets.UTF_8).length > 72 || newPassword.isBlank()) {
            throw new IllegalArgumentException("Token o contraseña inválidos");
        }
        String tokenHash = hash(rawToken.trim());
        Instant now = clock.instant();
        List<Long> ids = jdbc.query("select user_id from password_reset_tokens where token_hash = ? and used_at is null and expires_at > ?", (rs, n) -> rs.getLong(1), tokenHash, now);
        if (ids.isEmpty()) throw new AuthFailure();
        Long userId = ids.getFirst();
        int updated = jdbc.update("update users set password_hash = ?, updated_at = ? where id = ?", passwords.hash(newPassword), now, userId);
        if (updated != 1) throw new AuthFailure();
        jdbc.update("update password_reset_tokens set used_at = ? where token_hash = ?", now, tokenHash);
        jdbc.update("update refresh_tokens set revoked_at = ? where user_id = ? and revoked_at is null", now, userId);
    }

    public List<Notice> localInbox() { return List.copyOf(localInbox); }

    private String randomToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    static String hash(String value) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))); }
        catch (Exception e) { throw new IllegalStateException(e); }
    }
}
