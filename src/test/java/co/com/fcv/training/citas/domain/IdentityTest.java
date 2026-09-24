package co.com.fcv.training.citas.domain;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.*;

class IdentityTest {
    @Test void canonicalizesIdentity() {
        assertThat(Identity.email("  USER@Example.COM ")).isEqualTo("user@example.com");
        assertThat(Identity.documentType(" cc ")).isEqualTo("CC");
        assertThat(Identity.required(" 123 ")).isEqualTo("123");
    }

    @Test void sessionExpiresAndRevokes() {
        Instant now = Instant.parse("2026-09-17T12:00:00Z");
        Long id = 1L;
        assertThat(new RefreshSession(id, id, "hash", now.plusSeconds(1), null).activeAt(now)).isTrue();
        assertThat(new RefreshSession(id, id, "hash", now, null).activeAt(now)).isFalse();
        assertThat(new RefreshSession(id, id, "hash", now.plusSeconds(1), now).activeAt(now)).isFalse();
    }
}
