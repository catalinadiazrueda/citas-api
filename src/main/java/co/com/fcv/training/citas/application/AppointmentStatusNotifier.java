package co.com.fcv.training.citas.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.Map;

/** Optional outbound adapter. An unavailable n8n webhook must never roll back a clinical scheduling transaction. */
@Component
class AppointmentStatusNotifier {
    private final JdbcTemplate jdbc; private final String webhook;
    AppointmentStatusNotifier(JdbcTemplate jdbc, @Value("${app.automation.status-webhook-url:}") String webhook) { this.jdbc=jdbc; this.webhook=webhook; }
    void notifyChange(Long appointment,String status,String reason) {
        if(webhook==null || webhook.isBlank()) return;
        Map<String,Object> patient=jdbc.queryForMap("select u.email from appointments a join users u on u.id=a.patient_user_id where a.id=?",appointment);
        try { RestClient.create().post().uri(webhook).body(Map.of("appointmentId",appointment,"status",status,"patientEmail",patient.get("email"),"reason",reason==null?"":reason)).retrieve().toBodilessEntity(); }
        catch (Exception ignored) { /* n8n delivery is observational and retryable outside the booking transaction. */ }
    }
}
