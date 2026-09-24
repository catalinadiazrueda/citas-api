package co.com.fcv.training.citas.adapter.persistence;

import co.com.fcv.training.citas.application.Ports;
import co.com.fcv.training.citas.domain.RefreshSession;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.Optional;

@Repository
class SessionJpaAdapter implements Ports.Sessions {
    private final SessionsJpa sessions;
    SessionJpaAdapter(SessionsJpa sessions) { this.sessions = sessions; }

    public void save(RefreshSession session) {
        SessionEntity e = new SessionEntity();
        e.id = session.id();
        e.userId = session.userId();
        e.tokenHash = session.jtiHash();
        e.expiresAt = session.expiresAt();
        e.revokedAt = session.revokedAt();
        sessions.saveAndFlush(e);
    }

    public Optional<RefreshSession> lockByJtiHash(String hash) {
        return sessions.lockByTokenHash(hash).map(e -> new RefreshSession(e.id,
                e.userId, e.tokenHash, e.expiresAt, e.revokedAt));
    }

    public void revoke(Long id, Instant when) {
        SessionEntity e = sessions.findById(id).orElseThrow();
        e.revokedAt = when;
        sessions.saveAndFlush(e);
    }
}
