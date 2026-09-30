package co.com.fcv.training.citas;

import co.com.fcv.training.citas.application.OperationsService;
import co.com.fcv.training.citas.application.SchedulingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest @Testcontainers
class OperationsServiceIntegrationTest {
    @Container static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4");
    @DynamicPropertySource static void properties(DynamicPropertyRegistry r) { r.add("spring.datasource.url",MYSQL::getJdbcUrl);r.add("spring.datasource.username",MYSQL::getUsername);r.add("spring.datasource.password",MYSQL::getPassword);r.add("app.jwt.access-secret",()->"a".repeat(40));r.add("app.jwt.refresh-secret",()->"b".repeat(40)); }
    @Autowired SchedulingService scheduling; @Autowired OperationsService operations; @Autowired JdbcTemplate jdbc;

    @Test void cancellationReleasesSlotsAndLeavesAnImmutableHistoryRecord() {
        Fixture f=fixture(30,true); var booked=scheduling.reserve(f.patient,f.professional,f.location,f.specialty,LocalDateTime.of(f.date,LocalTime.of(8,0)),"Prueba");
        operations.cancel(f.patient,booked.id());
        assertThat(operations.mine(f.patient,"CANCELLED",f.date)).hasSize(1);
        assertThat(jdbc.queryForObject("select count(*) from professional_slots where appointment_id=?",Integer.class,booked.id())).isZero();
        assertThat(operations.history(f.patient,Set.of("USER"),booked.id())).anyMatch(h->"CANCELLED".equals(h.get("status")) && "USER".equals(h.get("source")));
    }
    @Test void rejectedReschedulePreservesOriginalAndReleasesProvisionalSlots() {
        Fixture f=fixture(60,false); var booked=scheduling.reserve(f.patient,f.professional,f.location,f.specialty,LocalDateTime.of(f.date,LocalTime.of(8,0)),"Prueba"); scheduling.decide(f.admin,booked.id(),"APPROVE",null);
        Map<String,Object> request=operations.requestReschedule(f.patient,booked.id(),f.date,LocalTime.of(9,0)); Long id=((Number)request.get("id")).longValue();
        operations.decideReschedule(f.admin,id,"REJECT","Sin cupo administrativo");
        assertThat(operations.mineOne(f.patient,booked.id()).get("startAt")).isEqualTo(LocalDateTime.of(f.date,LocalTime.of(8,0)));
        assertThat(jdbc.queryForObject("select count(*) from professional_slots where reschedule_request_id=?",Integer.class,id)).isZero();
    }
    @Test void approvedRescheduleMovesTheAppointmentOnlyWhenItsProvisionalSlotsRemainHeld() {
        Fixture f=fixture(60,false); var booked=scheduling.reserve(f.patient,f.professional,f.location,f.specialty,LocalDateTime.of(f.date,LocalTime.of(8,0)),"Prueba"); scheduling.decide(f.admin,booked.id(),"APPROVE",null);
        Long request=((Number)operations.requestReschedule(f.patient,booked.id(),f.date,LocalTime.of(9,0)).get("id")).longValue();
        assertThat(jdbc.queryForObject("select count(*) from reschedule_status_history where reschedule_request_id=?",Integer.class,request)).isEqualTo(1);
        operations.decideReschedule(f.admin,request,"APPROVE",null);
        assertThat(operations.mineOne(f.patient,booked.id()).get("startAt")).isEqualTo(LocalDateTime.of(f.date,LocalTime.of(9,0)));
        assertThat(jdbc.queryForObject("select count(*) from professional_slots where appointment_id=?",Integer.class,booked.id())).isEqualTo(2);
    }
    @Test void pendingReschedulePreventsItsAvailabilityBlockFromBeingDeleted() {
        Fixture f=fixture(30,true); var booked=scheduling.reserve(f.patient,f.professional,f.location,f.specialty,LocalDateTime.of(f.date,LocalTime.of(8,0)),"Prueba");
        operations.requestReschedule(f.patient,booked.id(),f.date,LocalTime.of(9,0));
        assertThatThrownBy(() -> scheduling.deleteBlock(f.owner,f.blockId)).hasMessageContaining("reprogramaciones comprometidas");
    }
    @Test void pendingRescheduleCannotBeSeenOrTakenAsAnAvailableSlot() {
        Fixture f=fixture(30,true); var booked=scheduling.reserve(f.patient,f.professional,f.location,f.specialty,LocalDateTime.of(f.date,LocalTime.of(8,0)),"Prueba");
        operations.requestReschedule(f.patient,booked.id(),f.date,LocalTime.of(9,0)); Long another=user("other"+UUID.randomUUID()+"@example.test","O"+UUID.randomUUID());
        assertThat(scheduling.availability(f.location,f.specialty,f.professional,f.date)).noneMatch(slot -> slot.startAt().equals(LocalDateTime.of(f.date,LocalTime.of(9,0))));
        assertThatThrownBy(() -> scheduling.reserve(another,f.professional,f.location,f.specialty,LocalDateTime.of(f.date,LocalTime.of(9,0)),"Duplicada")).hasMessageContaining("Franja");
    }
    private Fixture fixture(int duration,boolean general){String s=UUID.randomUUID().toString();Long p=scheduling.createProfessional("Pro","Fes","CC","P"+s,"pro"+s+"@example.test","300","hash","PC"+s,"LIC"+s);Long owner=jdbc.queryForObject("select user_id from professionals where id=?",Long.class,p);Long specialty=scheduling.createSpecialty("S"+s,"Especialidad "+s,duration,general).id();Long location=jdbc.queryForObject("select id from locations limit 1",Long.class);scheduling.setProfessionalSpecialties(p,List.of(specialty),specialty);scheduling.setProfessionalLocations(p,List.of(location));LocalDate date=LocalDate.now().plusDays(4);Long block=scheduling.createBlock(owner,location,date,LocalTime.of(8,0),LocalTime.of(11,0)).id();Long patient=user("patient"+s+"@example.test","U"+s);Long admin=user("admin"+s+"@example.test","A"+s);return new Fixture(p,owner,patient,admin,location,specialty,date,block);}
    private Long user(String email,String doc){jdbc.update("insert into users(first_name,last_name,document_type,document_number,email,phone,password_hash,active,email_verified) values ('Test','User','CC',?,?,?,'hash',true,false)",doc,email,"300");return jdbc.queryForObject("select id from users where email=?",Long.class,email);}
    record Fixture(Long professional,Long owner,Long patient,Long admin,Long location,Long specialty,LocalDate date,Long blockId){}
}
