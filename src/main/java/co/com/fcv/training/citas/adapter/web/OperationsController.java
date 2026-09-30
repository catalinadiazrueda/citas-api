package co.com.fcv.training.citas.adapter.web;

import co.com.fcv.training.citas.application.OperationsService;
import co.com.fcv.training.citas.application.ProfileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.util.*;

@RestController @RequestMapping("/api/v1")
class OperationsController {
    record ProfileRequest(@Size(max=120) String firstName,@Size(max=120) String lastName,@Email @Size(max=254) String email,@Size(max=40) String phone) {}
    record AffiliationRequest(@NotNull Long planId,@NotBlank @Size(max=80) String membershipNumber) {}
    record RescheduleRequest(@NotNull LocalDate date,@NotNull LocalTime startTime) {}
    record Decision(@NotBlank String decision,@Size(max=500) String reason) {}
    record CloseRequest(@NotBlank String status) {}
    record EpsRequest(@NotBlank @Size(max=30) String code,@NotBlank @Size(max=150) String name,boolean active) {}
    record PlanRequest(@NotNull Long epsId,@NotNull Long regimeId,@NotBlank @Size(max=50) String code,@NotBlank @Size(max=150) String name,boolean active) {}
    private final OperationsService operations; private final ProfileService profiles;
    OperationsController(OperationsService operations,ProfileService profiles){this.operations=operations;this.profiles=profiles;}
    @GetMapping("/me") @PreAuthorize("hasRole('USER')") Map<String,Object> me(Authentication a){return profiles.profile(user(a));}
    @PutMapping("/me") @PreAuthorize("hasRole('USER')") Map<String,Object> updateMe(Authentication a,@Valid @RequestBody ProfileRequest r){return profiles.update(user(a),r.firstName(),r.lastName(),r.email(),r.phone());}
    @GetMapping("/me/affiliation") @PreAuthorize("hasRole('USER')") Map<String,Object> affiliation(Authentication a){return profiles.affiliation(user(a));}
    @PutMapping("/me/affiliation") @PreAuthorize("hasRole('USER')") Map<String,Object> affiliation(Authentication a,@Valid @RequestBody AffiliationRequest r){return profiles.updateAffiliation(user(a),r.planId(),r.membershipNumber());}
    @GetMapping("/appointments/mine") @PreAuthorize("hasRole('USER')") List<Map<String,Object>> mine(Authentication a,@RequestParam(required=false) String status,@RequestParam(required=false) LocalDate date){return operations.mine(user(a),status,date);}
    @GetMapping("/appointments/{id}") @PreAuthorize("hasRole('USER')") Map<String,Object> mineOne(Authentication a,@PathVariable Long id){return operations.mineOne(user(a),id);}
    @PostMapping("/appointments/{id}/cancel") @PreAuthorize("hasRole('USER')") ResponseEntity<Void> cancel(Authentication a,@PathVariable Long id){operations.cancel(user(a),id);return ResponseEntity.noContent().build();}
    @PostMapping("/appointments/{id}/reschedules") @PreAuthorize("hasRole('USER')") ResponseEntity<Map<String,Object>> reschedule(Authentication a,@PathVariable Long id,@Valid @RequestBody RescheduleRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(operations.requestReschedule(user(a),id,r.date(),r.startTime()));}
    @GetMapping("/professional/agenda") @PreAuthorize("hasRole('PROFESSIONAL')") List<Map<String,Object>> agenda(Authentication a,@RequestParam(required=false) LocalDate date,@RequestParam(required=false) Long locationId){return operations.professionalAgenda(user(a),date,locationId);}
    @PostMapping("/professional/appointments/{id}/close") @PreAuthorize("hasRole('PROFESSIONAL')") ResponseEntity<Void> close(Authentication a,@PathVariable Long id,@Valid @RequestBody CloseRequest r){operations.close(user(a),id,r.status().trim().toUpperCase(Locale.ROOT));return ResponseEntity.noContent().build();}
    @GetMapping("/appointments/{id}/history") List<Map<String,Object>> history(Authentication a,@PathVariable Long id){return operations.history(user(a),roles(a),id);}
    @GetMapping("/admin/reschedules/pending") @PreAuthorize("hasRole('ADMIN')") List<Map<String,Object>> reschedules(@RequestParam(required=false) Long locationId,@RequestParam(required=false) Long professionalId,@RequestParam(required=false) Long specialtyId,@RequestParam(required=false) LocalDate date){return operations.pendingReschedules(locationId,professionalId,specialtyId,date);}
    @PostMapping("/admin/reschedules/{id}/decision") @PreAuthorize("hasRole('ADMIN')") Map<String,Object> decideReschedule(Authentication a,@PathVariable Long id,@Valid @RequestBody Decision r){return operations.decideReschedule(user(a),id,r.decision().trim().toUpperCase(Locale.ROOT),r.reason());}
    @GetMapping("/admin/eps") @PreAuthorize("hasRole('ADMIN')") List<Map<String,Object>> eps(){return profiles.eps();}
    @PostMapping("/admin/eps") @PreAuthorize("hasRole('ADMIN')") ResponseEntity<Map<String,Object>> eps(@Valid @RequestBody EpsRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(profiles.saveEps(null,r.code(),r.name(),r.active()));}
    @PatchMapping("/admin/eps/{id}") @PreAuthorize("hasRole('ADMIN')") Map<String,Object> eps(@PathVariable Long id,@Valid @RequestBody EpsRequest r){return profiles.saveEps(id,r.code(),r.name(),r.active());}
    @GetMapping("/admin/plans") @PreAuthorize("hasRole('ADMIN')") List<Map<String,Object>> plans(){return profiles.plans();}
    @PostMapping("/admin/plans") @PreAuthorize("hasRole('ADMIN')") ResponseEntity<Map<String,Object>> plan(@Valid @RequestBody PlanRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(profiles.savePlan(null,r.epsId(),r.regimeId(),r.code(),r.name(),r.active()));}
    @PatchMapping("/admin/plans/{id}") @PreAuthorize("hasRole('ADMIN')") Map<String,Object> plan(@PathVariable Long id,@Valid @RequestBody PlanRequest r){return profiles.savePlan(id,r.epsId(),r.regimeId(),r.code(),r.name(),r.active());}
    @GetMapping("/automation/reminders") @PreAuthorize("hasRole('ADMIN')") List<Map<String,Object>> reminders(@RequestParam LocalDate from,@RequestParam LocalDate to){return operations.reminders(from,to);}
    @GetMapping("/automation/daily-summary") @PreAuthorize("hasRole('ADMIN')") List<Map<String,Object>> summary(@RequestParam LocalDate date){return operations.dailySummary(date);}
    private Long user(Authentication a){return Long.valueOf(a.getName());}
    private Set<String> roles(Authentication a){Set<String> result=new HashSet<>();a.getAuthorities().forEach(x->result.add(x.getAuthority().replace("ROLE_","")));return result;}
}
