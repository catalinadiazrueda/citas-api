package co.com.fcv.training.citas.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.Map;

/** Optional outbound adapter. An unavailable n8n webhook must never roll back a clinical scheduling transaction. */
@Component
class AppointmentStatusNotifier {
    private final JdbcTemplate jdbc; private final String webhook; private final String secret;
    AppointmentStatusNotifier(JdbcTemplate jdbc, @Value("${app.automation.status-webhook-url:}") String webhook,
                              @Value("${app.automation.status-webhook-secret:}") String secret) {
        this.jdbc=jdbc; this.webhook=webhook; this.secret=secret;
        if (webhook != null && !webhook.isBlank() && (secret == null || secret.length() < 32))
            throw new IllegalStateException("STATUS_WEBHOOK_SECRET debe tener al menos 32 caracteres cuando STATUS_WEBHOOK_URL está configurada");
    }
    void notifyChange(Long appointment,String status,String reason) {
        if(webhook==null || webhook.isBlank()) return;
        Map<String,Object> patient=jdbc.queryForMap("select u.email from appointments a join users u on u.id=a.patient_user_id where a.id=?",appointment);
        try { RestClient.create().post().uri(webhook).header("X-Webhook-Secret", secret).body(Map.of("appointmentId",appointment,"status",status,"patientEmail",patient.get("email"),"reason",reason==null?"":reason)).retrieve().toBodilessEntity(); }
        catch (Exception ignored) { /* n8n delivery is observational and retryable outside the booking transaction. */ }
    }
}
