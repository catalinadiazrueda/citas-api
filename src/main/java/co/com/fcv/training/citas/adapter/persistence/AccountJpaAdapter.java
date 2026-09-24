package co.com.fcv.training.citas.adapter.persistence;

import co.com.fcv.training.citas.application.Ports;
import co.com.fcv.training.citas.domain.Account;
import org.springframework.stereotype.Repository;
import java.time.Clock;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
class AccountJpaAdapter implements Ports.Accounts {
    private final UsersJpa users;
    private final RolesJpa roles;
    private final Clock clock;

    AccountJpaAdapter(UsersJpa users, RolesJpa roles, Clock clock) {
        this.users = users;
        this.roles = roles;
        this.clock = clock;
    }

    public boolean existsEmail(String email) { return users.existsByEmail(email); }
    public boolean existsDocument(String type, String number) { return users.existsByDocumentTypeAndDocumentNumber(type, number); }
    public Optional<Account> byEmail(String email) { return users.findByEmail(email).map(this::toDomain); }
    public Optional<Account> byId(Long id) { return users.findById(id).map(this::toDomain); }

    public Account save(Account account) {
        UserEntity entity = new UserEntity();
        entity.id = account.id();
        entity.firstName = account.firstName();
        entity.lastName = account.lastName();
        entity.documentType = account.documentType();
        entity.documentNumber = account.documentNumber();
        entity.email = account.email();
        entity.phone = account.phone();
        entity.passwordHash = account.passwordHash();
        entity.createdAt = clock.instant();
        entity.roles = account.roles().stream()
                .map(code -> roles.findByCode(code).orElseThrow(() -> new IllegalStateException("Rol faltante")))
                .collect(Collectors.toSet());
        return toDomain(users.saveAndFlush(entity));
    }

    private Account toDomain(UserEntity e) {
        return new Account(e.id, e.firstName, e.lastName, e.documentType,
                e.documentNumber, e.email, e.phone, e.passwordHash,
                e.roles.stream().map(role -> role.code).collect(Collectors.toSet()));
    }
}
