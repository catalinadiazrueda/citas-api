package co.com.fcv.training.citas;

import co.com.fcv.training.citas.application.SchedulingService;
import co.com.fcv.training.citas.adapter.security.JwtTokens;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.*;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class SchedulingServiceIntegrationTest {
    @Container static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4");
    @DynamicPropertySource static void properties(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", MYSQL::getJdbcUrl); r.add("spring.datasource.username", MYSQL::getUsername); r.add("spring.datasource.password", MYSQL::getPassword);
        r.add("app.jwt.access-secret", () -> "a".repeat(40)); r.add("app.jwt.refresh-secret", () -> "b".repeat(40)); r.add("app.cookie.secure", () -> true); r.add("app.cookie.same-site", () -> "None");
    }
    @Autowired SchedulingService scheduling; @Autowired JdbcTemplate jdbc; @Autowired MockMvc mvc; @Autowired JwtTokens jwt;
    record ReservationAttempt(int status, String detail) {}

    @Test void concurrentReservationsAllowOnlyOneAndRetainSpecializedSlots() throws Exception {
        String suffix=UUID.randomUUID().toString();
        Long professional=scheduling.createProfessional("Concurrent","Professional","CC","P"+suffix,"concurrent-pro-"+suffix+"@example.test","300", "hash", "CPC"+suffix,"CLIC"+suffix);
        Long owner=jdbc.queryForObject("select user_id from professionals where id=?",Long.class,professional);
        Long general=scheduling.createSpecialty("GEN"+suffix,"Medicina General "+suffix,30,true).id();
        Long specialized=scheduling.createSpecialty("SPEC"+suffix,"Especialidad "+suffix,60,false).id();
        Long location=jdbc.queryForObject("select id from locations where active=true limit 1",Long.class);
        scheduling.setProfessionalSpecialties(professional,List.of(general,specialized),general);
        scheduling.setProfessionalLocations(professional,List.of(location));
        LocalDate date=LocalDate.now().plusDays(3);
        scheduling.createBlock(owner,location,date,LocalTime.of(8,0),LocalTime.of(10,0));
        Long patientOne=user("race-one-"+suffix+"@example.test","R1"+suffix);
        Long patientTwo=user("race-two-"+suffix+"@example.test","R2"+suffix);
        CountDownLatch ready=new CountDownLatch(2), start=new CountDownLatch(1);
        ExecutorService executor=Executors.newFixedThreadPool(2);
        try {
            var attempts=new ArrayList<Future<ReservationAttempt>>();
            for (Long patient : List.of(patientOne,patientTwo)) {
                attempts.add(executor.submit(() -> {
                    ready.countDown();
                    start.await();
                    var response=mvc.perform(post("/api/v1/appointments")
                            .header("Authorization","Bearer "+jwt.access(patient,java.util.Set.of("USER")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"professionalId":%d,"locationId":%d,"specialtyId":%d,"date":"%s","startTime":"08:00","reason":"Concurrencia"}
                                    """.formatted(professional,location,general,date))).andReturn().getResponse();
                    return new ReservationAttempt(response.getStatus(),response.getContentAsString());
                }));
            }
            assertThat(ready.await(10, java.util.concurrent.TimeUnit.SECONDS)).isTrue();
            start.countDown();
            List<ReservationAttempt> results=List.of(attempts.get(0).get(),attempts.get(1).get());
            // Cada intento queda registrado en el resultado y solo uno puede confirmar el slot.
            results.forEach(result -> System.out.printf("Reservation attempt status=%d body=%s%n", result.status(), result.detail()));
            assertThat(results).extracting(ReservationAttempt::status).containsExactlyInAnyOrder(201,409);
            assertThat(results).extracting(ReservationAttempt::detail).anyMatch(body -> body.contains("APPROVED"));
            assertThat(results).extracting(ReservationAttempt::detail).anyMatch(body -> body.contains("409") || body.contains("Franja"));
            assertThat(jdbc.queryForObject("select count(*) from appointments where professional_id=? and specialty_id=? and scheduled_start_at=?",Integer.class,
                    professional,general,LocalDateTime.of(date,LocalTime.of(8,0)))).isEqualTo(1);
            assertThat(jdbc.queryForObject("select count(*) from professional_slots where start_at=? and appointment_id is not null",Integer.class,
                    LocalDateTime.of(date,LocalTime.of(8,0)))).isEqualTo(1);

            Long specializedPatient=user("specialized-"+suffix+"@example.test","R3"+suffix);
            SchedulingService.Appointment specializedAppointment=scheduling.reserve(specializedPatient,professional,location,specialized,
                    LocalDateTime.of(date,LocalTime.of(9,0)),"Solicitud especializada");
            assertThat(specializedAppointment.status()).isEqualTo("REQUESTED");
            assertThat(jdbc.queryForObject("select count(*) from professional_slots where appointment_id=?",Integer.class,specializedAppointment.id())).isEqualTo(2);
            assertThat(jdbc.queryForObject("select count(*) from appointments where professional_id=? and scheduled_start_at=?",Integer.class,
                    professional,LocalDateTime.of(date,LocalTime.of(9,0)))).isEqualTo(1);
        } finally {
            executor.shutdownNow();
        }
    }

    @Test void retainsConsecutiveSlotsAndReleasesThemAfterAdministrativeRejection() {
        String suffix=UUID.randomUUID().toString();
        Long professionalUser=user("prof-"+suffix+"@example.test",suffix); Long professional=scheduling.createProfessional("Pro","Fes","CC","P"+suffix,"pro2-"+suffix+"@example.test","300", "hash", "PC"+suffix,"LIC"+suffix);
        // createProfessional creates a second user; use that owner for the professional block
        Long owner=jdbc.queryForObject("select user_id from professionals where id=?",Long.class,professional);
        Long specialty=scheduling.createSpecialty("ESP"+suffix,"Especialidad "+suffix,60,false).id(); Long location=jdbc.queryForObject("select id from locations where active=true limit 1",Long.class);
        scheduling.setProfessionalSpecialties(professional,List.of(specialty),specialty); scheduling.setProfessionalLocations(professional,List.of(location));
        LocalDate date=LocalDate.now().plusDays(2); scheduling.createBlock(owner,location,date,LocalTime.of(8,0),LocalTime.of(10,0));
        assertThat(scheduling.availability(location,specialty,professional,date)).anyMatch(a -> a.startAt().equals(LocalDateTime.of(date,LocalTime.of(8,0))) && a.endAt().equals(LocalDateTime.of(date,LocalTime.of(9,0))));
        Long patient=user("patient-"+suffix+"@example.test","U"+suffix); Long admin=user("admin-"+suffix+"@example.test","A"+suffix);
        SchedulingService.Appointment appointment=scheduling.reserve(patient,professional,location,specialty,LocalDateTime.of(date,LocalTime.of(8,0)),"Prueba");
        assertThat(appointment.status()).isEqualTo("REQUESTED");
        assertThatThrownBy(() -> scheduling.reserve(patient,professional,location,specialty,LocalDateTime.of(date,LocalTime.of(8,0)),"Duplicada")).hasMessageContaining("Franja");
        assertThat(scheduling.decide(admin,appointment.id(),"REJECT","Sin disponibilidad clínica").status()).isEqualTo("REJECTED");
        assertThat(scheduling.availability(location,specialty,professional,date)).anyMatch(a -> a.startAt().equals(LocalDateTime.of(date,LocalTime.of(8,0))));
    }
    private Long user(String email,String document) { jdbc.update("insert into users(first_name,last_name,document_type,document_number,email,phone,password_hash,active,email_verified) values ('Test','User','CC',?,?,?,'hash',true,false)",document,email,"300"); return jdbc.queryForObject("select id from users where email=?",Long.class,email); }
}
