package co.com.fcv.training.citas.adapter.web;

import co.com.fcv.training.citas.application.PasswordRecoveryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/password-recovery")
class PasswordRecoveryAdminController {
    private final PasswordRecoveryService recovery;
    private final boolean local;

    PasswordRecoveryAdminController(PasswordRecoveryService recovery, @Value("${spring.profiles.active:}") String profiles) {
        this.recovery = recovery;
        this.local = List.of(profiles.split(",")).contains("local");
    }

    @GetMapping("/inbox")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<?> inbox() {
        if (!local) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(recovery.localInbox());
    }
}
