package co.com.fcv.training.citas.domain;

import java.time.Instant;

public record RefreshSession(Long id, Long userId, String jtiHash, Instant expiresAt, Instant revokedAt) {
    public boolean activeAt(Instant now) {
        return revokedAt == null && expiresAt.isAfter(now);
    }
}
