package co.com.fcv.training.citas.application;

import co.com.fcv.training.citas.domain.Account;
import co.com.fcv.training.citas.domain.RefreshSession;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public final class Ports {
    private Ports() {}

    public interface Accounts {
        boolean existsEmail(String email);
        boolean existsDocument(String type, String number);
        Optional<Account> byEmail(String email);
        Optional<Account> byId(Long id);
        Account save(Account account);
    }

    public interface Sessions {
        void save(RefreshSession session);
        Optional<RefreshSession> lockByJtiHash(String hash);
        void revoke(Long id, Instant when);
    }

    public interface Passwords {
        String hash(String raw);
        boolean matches(String raw, String hash);
    }

    public interface Affiliations { void createCurrent(Long userId, Long planId); }

    public record IssuedRefresh(String value, String jti, Instant expiresAt) {}
    public record RefreshIdentity(Long userId, String jti) {}

    public interface Tokens {
        String access(Long userId, Set<String> roles);
        IssuedRefresh refresh(Long userId);
        RefreshIdentity readRefresh(String token);
        long accessSeconds();
    }

    public interface Transactions {
        <T> T run(Supplier<T> work);
    }
}
