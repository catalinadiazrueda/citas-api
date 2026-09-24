package co.com.fcv.training.citas.adapter.persistence;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @Column(name = "first_name", nullable = false, length = 80) String firstName;
    @Column(name = "last_name", nullable = false, length = 80) String lastName;
    @Column(name = "document_type", nullable = false, length = 20) String documentType;
    @Column(name = "document_number", nullable = false, length = 40) String documentNumber;
    @Column(nullable = false, length = 160) String email;
    @Column(length = 30) String phone;
    @Column(name = "password_hash", nullable = false, length = 255) String passwordHash;
    @Column(name = "created_at", nullable = false) Instant createdAt;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<RoleEntity> roles = new HashSet<>();

    protected UserEntity() {}
}
