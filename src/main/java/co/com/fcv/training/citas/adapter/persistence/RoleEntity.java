package co.com.fcv.training.citas.adapter.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
class RoleEntity {
    @Id Short id;
    @Column(nullable = false, length = 30) String code;
    @Column(nullable = false, length = 80) String name;
    protected RoleEntity() {}
}
