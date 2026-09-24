package co.com.fcv.training.citas.domain;

import java.util.Set;

public record Account(Long id, String firstName, String lastName, String documentType,
                      String documentNumber, String email, String phone, String passwordHash,
                      Set<String> roles) {
    public Account {
        roles = Set.copyOf(roles);
    }
}
