package co.com.fcv.training.citas.application;

import co.com.fcv.training.citas.domain.Account;
import co.com.fcv.training.citas.domain.Identity;
import co.com.fcv.training.citas.domain.RefreshSession;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.util.HexFormat;
import java.util.Set;

public class AuthService {
    public record Registration(String firstName, String lastName, String documentType,
                               String documentNumber, String email, String phone, String password, Long insurancePlanId) {}
    public record Tokens(String accessToken, String refreshToken, long expiresIn) {}

    private final Ports.Accounts accounts;
    private final Ports.Sessions sessions;
    private final Ports.Passwords passwords;
    private final Ports.Tokens tokens;
    private final Ports.Transactions transactions;
    private final Clock clock;
    private final Ports.Affiliations affiliations;

    public AuthService(Ports.Accounts accounts, Ports.Sessions sessions, Ports.Passwords passwords,
                       Ports.Tokens tokens, Ports.Transactions transactions, Clock clock, Ports.Affiliations affiliations) {
        this.accounts = accounts;
        this.sessions = sessions;
        this.passwords = passwords;
        this.tokens = tokens;
        this.transactions = transactions;
        this.clock = clock;
        this.affiliations = affiliations;
    }

    public Account register(Registration input) {
        return transactions.run(() -> {
            String email = Identity.email(input.email());
            String type = Identity.documentType(input.documentType());
            String number = Identity.required(input.documentNumber());
            if (accounts.existsEmail(email) || accounts.existsDocument(type, number)) throw new DuplicateIdentity();
            String password = input.password();
            if (password == null || password.isBlank()) throw new IllegalArgumentException("Contraseña obligatoria");
            if (password.getBytes(StandardCharsets.UTF_8).length > 72) throw new IllegalArgumentException("Contraseña demasiado larga");
            Account account = new Account(null, Identity.required(input.firstName()),
                    Identity.required(input.lastName()), type, number, email,
                    Identity.required(input.phone()), passwords.hash(password), Set.of("USER"));
            Account saved = accounts.save(account);
            if (input.insurancePlanId() != null) affiliations.createCurrent(saved.id(), input.insurancePlanId());
            return saved;
        });
    }

    public Tokens login(String email, String password) {
        return transactions.run(() -> {
            if (password == null || password.getBytes(StandardCharsets.UTF_8).length > 72) throw new AuthFailure();
            Account account = accounts.byEmail(Identity.email(email)).orElseThrow(AuthFailure::new);
            if (!passwords.matches(password, account.passwordHash())) throw new AuthFailure();
            return issue(account);
        });
    }

    public Tokens refresh(String rawToken) {
        return transactions.run(() -> {
            Ports.RefreshIdentity identity = tokens.readRefresh(rawToken);
            RefreshSession session = sessions.lockByJtiHash(hash(identity.jti())).orElseThrow(AuthFailure::new);
            if (!session.userId().equals(identity.userId()) || !session.activeAt(clock.instant())) throw new AuthFailure();
            Account account = accounts.byId(identity.userId()).orElseThrow(AuthFailure::new);
            sessions.revoke(session.id(), clock.instant());
            return issue(account);
        });
    }

    public void logout(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) return;
        try {
            transactions.run(() -> {
                Ports.RefreshIdentity identity = tokens.readRefresh(rawToken);
                sessions.lockByJtiHash(hash(identity.jti()))
                        .filter(s -> s.userId().equals(identity.userId()) && s.activeAt(clock.instant()))
                        .ifPresent(s -> sessions.revoke(s.id(), clock.instant()));
                return null;
            });
        } catch (AuthFailure ignored) {
            // Logout remains idempotent and the controller clears the cookie.
        }
    }

    private Tokens issue(Account account) {
        Ports.IssuedRefresh refresh = tokens.refresh(account.id());
        sessions.save(new RefreshSession(null, account.id(), hash(refresh.jti()), refresh.expiresAt(), null));
        return new Tokens(tokens.access(account.id(), account.roles()), refresh.value(), tokens.accessSeconds());
    }

    static String hash(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
