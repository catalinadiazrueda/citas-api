package co.com.fcv.training.citas.adapter.persistence;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
class SessionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @Column(name = "user_id", nullable = false) Long userId;
    @Column(name = "token_hash", nullable = false, length = 255) String tokenHash;
    @Column(name = "expires_at", nullable = false) Instant expiresAt;
    @Column(name = "revoked_at") Instant revokedAt;
    protected SessionEntity() {}
}
