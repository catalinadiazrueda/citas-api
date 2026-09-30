package co.com.fcv.training.citas.application;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.*;
import java.util.*;

/** Application service for the post-booking lifecycle. HTTP is deliberately kept out of this class. */
@Service
public class OperationsService {
    private final JdbcTemplate jdbc;
    private final Clock clock; private final AppointmentStatusNotifier notifier;
    public OperationsService(JdbcTemplate jdbc, Clock clock, AppointmentStatusNotifier notifier) { this.jdbc = jdbc; this.clock = clock; this.notifier=notifier; }

    public List<Map<String,Object>> mine(Long user, String status, LocalDate date) {
        String sql = appointmentProjection() + " where a.patient_user_id=?";
        List<Object> args = new ArrayList<>(List.of(user));
        if (status != null && !status.isBlank()) { sql += " and st.code=?"; args.add(status); }
        if (date != null) { sql += " and date(a.scheduled_start_at)=?"; args.add(date); }
        return jdbc.queryForList(sql + " order by a.scheduled_start_at desc", args.toArray());
    }

    public Map<String,Object> mineOne(Long user, Long appointment) {
        return jdbc.query(appointmentProjection() + " where a.id=? and a.patient_user_id=?", (rs, n) -> row(rs), appointment, user)
                .stream().findFirst().orElseThrow(() -> notFound("Cita"));
    }

    @Transactional public void cancel(Long user, Long appointment) {
        Map<String,Object> a = jdbc.queryForMap("select a.id,a.scheduled_start_at,st.code from appointments a join appointment_statuses st on st.id=a.status_id where a.id=? and a.patient_user_id=? for update", appointment, user);
        if (!"APPROVED".equals(a.get("code")) && !"REQUESTED".equals(a.get("code"))) throw conflict("La cita no se puede cancelar");
        if (!((LocalDateTime)a.get("scheduled_start_at")).isAfter(LocalDateTime.now(clock))) throw conflict("Solo se puede cancelar una cita futura");
        if (count("select count(*) from reschedule_requests r join reschedule_statuses s on s.id=r.status_id where r.appointment_id=? and s.code='PENDING'", appointment) > 0) throw conflict("La cita tiene una reprogramación pendiente");
        jdbc.update("update appointments set status_id=? where id=?", appointmentStatus("CANCELLED"), appointment);
        jdbc.update("update professional_slots set appointment_id=null where appointment_id=?", appointment);
        history(appointment, "CANCELLED", user, "USER", null);
        notifier.notifyChange(appointment,"CANCELLED",null);
    }

    @Transactional public Map<String,Object> requestReschedule(Long user, Long appointment, LocalDate date, LocalTime start) {
        Map<String,Object> a = jdbc.queryForMap("select a.id,a.professional_id,a.location_id,a.specialty_id,a.scheduled_start_at,a.scheduled_end_at,st.code from appointments a join appointment_statuses st on st.id=a.status_id where a.id=? and a.patient_user_id=? for update", appointment, user);
        if (!"APPROVED".equals(a.get("code")) || !((LocalDateTime)a.get("scheduled_start_at")).isAfter(LocalDateTime.now(clock))) throw conflict("Solo se reprograma una cita aprobada futura");
        if (count("select count(*) from reschedule_requests r join reschedule_statuses s on s.id=r.status_id where r.appointment_id=? and s.code='PENDING'", appointment) > 0) throw conflict("Ya existe una reprogramación pendiente");
        LocalDateTime newStart = LocalDateTime.of(date, start);
        if (!newStart.isAfter(LocalDateTime.now(clock))) throw conflict("La nueva franja debe ser futura");
        int minutes = ((LocalDateTime)a.get("scheduled_end_at")).compareTo((LocalDateTime)a.get("scheduled_start_at")) == 0 ? 30 : (int)Duration.between((LocalDateTime)a.get("scheduled_start_at"),(LocalDateTime)a.get("scheduled_end_at")).toMinutes();
        LocalDateTime newEnd = newStart.plusMinutes(minutes);
        List<Long> slots = freeSlots(number(a.get("professional_id")), number(a.get("location_id")), newStart, newEnd);
        if (slots.size() != minutes / 30 || !consecutive(slots)) throw conflict("Franja no disponible");
        Long id = insert("insert into reschedule_requests(appointment_id,status_id,requested_start_at,requested_end_at,requested_by_user_id) values (?,?,?,?,?)", appointment, rescheduleStatus("PENDING"), newStart, newEnd, user);
        int reserved = jdbc.update("update professional_slots set reschedule_request_id=? where id in (" + placeholders(slots.size()) + ") and appointment_id is null and reschedule_request_id is null", prepend(id, slots));
        if (reserved != slots.size()) throw conflict("Franja ya reservada");
        rescheduleHistory(id,"PENDING",user,"USER",null);
        return jdbc.queryForMap("select id,appointment_id as appointmentId,requested_start_at as startAt,requested_end_at as endAt from reschedule_requests where id=?", id);
    }

    public List<Map<String,Object>> pendingReschedules(Long location, Long professional, Long specialty, LocalDate date) {
        String sql = "select r.id,r.appointment_id as appointmentId,r.requested_start_at as startAt,r.requested_end_at as endAt,concat(pu.first_name,' ',pu.last_name) as patientName,concat(pru.first_name,' ',pru.last_name) as professionalName,s.name as specialtyName,l.name as locationName from reschedule_requests r join reschedule_statuses rs on rs.id=r.status_id join appointments a on a.id=r.appointment_id join users pu on pu.id=a.patient_user_id join professionals p on p.id=a.professional_id join users pru on pru.id=p.user_id join specialties s on s.id=a.specialty_id join locations l on l.id=a.location_id where rs.code='PENDING'";
        List<Object> args = new ArrayList<>();
        if(location != null){ sql += " and a.location_id=?"; args.add(location); } if(professional != null){sql += " and a.professional_id=?";args.add(professional);} if(specialty != null){sql += " and a.specialty_id=?";args.add(specialty);} if(date != null){sql += " and date(r.requested_start_at)=?";args.add(date);}
        return jdbc.queryForList(sql + " order by r.requested_start_at", args.toArray());
    }

    @Transactional public Map<String,Object> decideReschedule(Long admin, Long request, String decision, String reason) {
        if (!Set.of("APPROVE", "REJECT").contains(decision)) throw new IllegalArgumentException("Decisión inválida");
        if ("REJECT".equals(decision) && (reason == null || reason.isBlank())) throw new IllegalArgumentException("Motivo de rechazo obligatorio");
        Map<String,Object> r = jdbc.queryForMap("select r.id,r.appointment_id,r.requested_start_at,r.requested_end_at,a.scheduled_start_at,a.scheduled_end_at from reschedule_requests r join reschedule_statuses rs on rs.id=r.status_id join appointments a on a.id=r.appointment_id where r.id=? and rs.code='PENDING' for update", request);
        Long appointment = number(r.get("appointment_id"));
        int expected=(int)Duration.between((LocalDateTime)r.get("requested_start_at"),(LocalDateTime)r.get("requested_end_at")).toMinutes()/30;
        if (count("select count(*) from professional_slots where reschedule_request_id=? and appointment_id is null",request)!=expected) throw conflict("La retención de reprogramación ya no está disponible");
        if ("APPROVE".equals(decision)) {
            jdbc.update("update professional_slots set appointment_id=null where appointment_id=?", appointment);
            jdbc.update("update professional_slots set appointment_id=?,reschedule_request_id=null where reschedule_request_id=?", appointment, request);
            jdbc.update("update appointments set scheduled_start_at=?,scheduled_end_at=? where id=?", r.get("requested_start_at"),r.get("requested_end_at"),appointment);
            history(appointment,"APPROVED",admin,"ADMIN","Reprogramación aprobada");
        } else jdbc.update("update professional_slots set reschedule_request_id=null where reschedule_request_id=?", request);
        jdbc.update("update reschedule_requests set status_id=?,reviewed_by_user_id=?,reviewed_at=?,decision_reason=? where id=?", rescheduleStatus("APPROVE".equals(decision)?"APPROVED":"REJECTED"),admin,LocalDateTime.now(clock),reason,request);
        String result="APPROVE".equals(decision)?"APPROVED":"REJECTED";
        rescheduleHistory(request,result,admin,"ADMIN",reason);
        notifier.notifyChange(appointment,result,reason);
        return Map.of("id", request, "status", result, "appointmentId", appointment);
    }

    public List<Map<String,Object>> professionalAgenda(Long user, LocalDate date, Long location) {
        Long professional = professionalForUser(user);
        String sql = appointmentProjection()+" where a.professional_id=? and st.code='APPROVED'"; List<Object> args=new ArrayList<>(List.of(professional));
        if(date!=null){sql+=" and date(a.scheduled_start_at)=?";args.add(date);} if(location!=null){sql+=" and a.location_id=?";args.add(location);} return jdbc.queryForList(sql+" order by a.scheduled_start_at",args.toArray());
    }

    @Transactional public void close(Long user, Long appointment, String status) {
        if(!Set.of("COMPLETED","NO_SHOW").contains(status)) throw new IllegalArgumentException("Estado de cierre inválido");
        Long professional=professionalForUser(user); Map<String,Object> a=jdbc.queryForMap("select a.id,a.scheduled_end_at,st.code from appointments a join appointment_statuses st on st.id=a.status_id where a.id=? and a.professional_id=? for update",appointment,professional);
        if(!"APPROVED".equals(a.get("code")) || ((LocalDateTime)a.get("scheduled_end_at")).isAfter(LocalDateTime.now(clock))) throw conflict("La cita aún no es aplicable para cierre");
        jdbc.update("update appointments set status_id=? where id=?",appointmentStatus(status),appointment); history(appointment,status,user,"USER",null);
        notifier.notifyChange(appointment,status,null);
    }

    public List<Map<String,Object>> history(Long actor, Set<String> roles, Long appointment) {
        String scope = roles.contains("ADMIN") ? "" : roles.contains("PROFESSIONAL") ? " and a.professional_id=(select id from professionals where user_id=?)" : " and a.patient_user_id=?";
        List<Object> args=new ArrayList<>(List.of(appointment)); if(!scope.isEmpty())args.add(actor);
        String sql="select h.id,st.code as status,h.changed_by_user_id as actorUserId,h.change_source as source,h.reason,h.changed_at as changedAt from appointment_status_history h join appointment_statuses st on st.id=h.status_id join appointments a on a.id=h.appointment_id where h.appointment_id=?"+scope+" order by h.changed_at";
        return jdbc.queryForList(sql,args.toArray());
    }
    public List<Map<String,Object>> reminders(LocalDate from, LocalDate to) { return jdbc.queryForList("select a.id,concat(u.first_name,' ',u.last_name) as patientName,u.email,a.scheduled_start_at as startAt,s.name as specialtyName,l.name as locationName from appointments a join appointment_statuses st on st.id=a.status_id join users u on u.id=a.patient_user_id join specialties s on s.id=a.specialty_id join locations l on l.id=a.location_id where st.code='APPROVED' and date(a.scheduled_start_at) between ? and ? order by a.scheduled_start_at",from,to); }
    public List<Map<String,Object>> dailySummary(LocalDate date) { return jdbc.queryForList("select l.id as locationId,l.name as locationName,st.code as status,count(*) as appointments from appointments a join locations l on l.id=a.location_id join appointment_statuses st on st.id=a.status_id where date(a.scheduled_start_at)=? group by l.id,l.name,st.code order by l.name,st.code",date); }

    private String appointmentProjection(){return "select a.id,st.code as status,a.scheduled_start_at as startAt,a.scheduled_end_at as endAt,timestampdiff(minute,a.scheduled_start_at,a.scheduled_end_at) as durationMinutes,concat(pru.first_name,' ',pru.last_name) as professionalName,s.name as specialtyName,l.name as locationName,(select h.reason from appointment_status_history h join appointment_statuses hs on hs.id=h.status_id where h.appointment_id=a.id and hs.code='REJECTED' order by h.changed_at desc limit 1) as rejectionReason from appointments a join appointment_statuses st on st.id=a.status_id join professionals p on p.id=a.professional_id join users pru on pru.id=p.user_id join specialties s on s.id=a.specialty_id join locations l on l.id=a.location_id";}
    private Map<String,Object> row(java.sql.ResultSet rs){try{Map<String,Object> m=new LinkedHashMap<>();for(int i=1;i<=rs.getMetaData().getColumnCount();i++)m.put(rs.getMetaData().getColumnLabel(i),rs.getObject(i));return m;}catch(Exception e){throw new IllegalStateException(e);}}
    private List<Long> freeSlots(Long professional,Long location,LocalDateTime start,LocalDateTime end){return jdbc.queryForList("select ps.id from professional_slots ps join availability_blocks b on b.id=ps.availability_block_id where b.professional_id=? and b.location_id=? and b.active=true and ps.start_at>=? and ps.end_at<=? and ps.appointment_id is null and ps.reschedule_request_id is null order by ps.start_at for update",Long.class,professional,location,start,end);}
    private boolean consecutive(List<Long> ids){if(ids.isEmpty())return false;List<LocalDateTime> t=jdbc.query("select start_at from professional_slots where id in ("+placeholders(ids.size())+") order by start_at",(r,n)->r.getObject(1,LocalDateTime.class),ids.toArray());for(int i=1;i<t.size();i++)if(!t.get(i-1).plusMinutes(30).equals(t.get(i)))return false;return true;}
    private void history(Long appointment,String status,Long actor,String source,String reason){jdbc.update("insert into appointment_status_history(appointment_id,status_id,changed_by_user_id,change_source,reason) values (?,?,?,?,?)",appointment,appointmentStatus(status),actor,source,reason);}
    private void rescheduleHistory(Long request,String status,Long actor,String source,String reason){jdbc.update("insert into reschedule_status_history(reschedule_request_id,status_id,changed_by_user_id,change_source,reason) values (?,?,?,?,?)",request,rescheduleStatus(status),actor,source,reason);}
    private Long appointmentStatus(String code){return jdbc.queryForObject("select id from appointment_statuses where code=?",Long.class,code);}
    private Long rescheduleStatus(String code){return jdbc.queryForObject("select id from reschedule_statuses where code=?",Long.class,code);}
    private Long professionalForUser(Long user){return jdbc.query("select id from professionals where user_id=? and active=true",(r,n)->r.getLong(1),user).stream().findFirst().orElseThrow(()->notFound("Profesional"));}
    private int count(String sql,Object... args){Integer n=jdbc.queryForObject(sql,Integer.class,args);return n==null?0:n;}
    private Long insert(String sql,Object... args){KeyHolder keys=new GeneratedKeyHolder();jdbc.update(c->{PreparedStatement p=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);for(int i=0;i<args.length;i++)p.setObject(i+1,args[i]);return p;},keys);return Objects.requireNonNull(keys.getKey()).longValue();}
    private String placeholders(int n){return String.join(",",Collections.nCopies(n,"?"));}
    private Long number(Object value){return ((Number)value).longValue();}
    private Object[] prepend(Object first,List<Long> rest){Object[] out=new Object[rest.size()+1];out[0]=first;for(int i=0;i<rest.size();i++)out[i+1]=rest.get(i);return out;}
    private SchedulingFailure notFound(String name){return new SchedulingFailure(SchedulingFailure.Kind.NOT_FOUND,name+" no encontrado");}
    private SchedulingFailure conflict(String message){return new SchedulingFailure(SchedulingFailure.Kind.CONFLICT,message);}
}
