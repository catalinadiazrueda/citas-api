package co.com.fcv.training.citas.adapter.web;

import co.com.fcv.training.citas.application.Ports;
import co.com.fcv.training.citas.application.SchedulingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
class SchedulingController {
    record SpecialtyRequest(@NotBlank @Size(max=50) String code,@NotBlank @Size(max=150) String name,@Min(30) @Max(60) int durationMinutes,boolean general) {}
    record SpecialtyPatch(@Size(max=150) String name,Integer durationMinutes,Boolean active) {}
    record ProfessionalRequest(@NotBlank String firstName,@NotBlank String lastName,@NotBlank String documentType,@NotBlank String documentNumber,@Email @NotBlank String email,@NotBlank String phone,@NotBlank String temporaryPassword,@NotBlank String professionalCode,@NotBlank String licenseNumber) {}
    record SpecialtiesRequest(@NotEmpty List<Long> specialtyIds,@NotNull Long primarySpecialtyId) {}
    record LocationsRequest(@NotEmpty @Size(max=2) List<Long> locationIds) {}
    record ActiveRequest(boolean active) {}
    record BlockRequest(@NotNull Long locationId,@NotNull LocalDate date,@NotNull LocalTime startTime,@NotNull LocalTime endTime) {}
    record BlockPatch(Long locationId,LocalDate date,LocalTime startTime,LocalTime endTime) {}
    record AppointmentRequest(@NotNull Long professionalId,@NotNull Long locationId,@NotNull Long specialtyId,@NotNull LocalDate date,@NotNull LocalTime startTime,@Size(max=500) String reason) {}
    record DecisionRequest(@NotBlank String decision,@Size(max=500) String reason) {}
    private final SchedulingService scheduling; private final Ports.Passwords passwords;
    SchedulingController(SchedulingService scheduling, Ports.Passwords passwords){this.scheduling=scheduling;this.passwords=passwords;}
    @GetMapping("/catalogs/{catalog}") List<Map<String,Object>> catalog(@PathVariable String catalog){return scheduling.catalog(catalog);}
    @GetMapping("/admin/specialties") @PreAuthorize("hasRole('ADMIN')") List<SchedulingService.Specialty> allSpecialties(){return scheduling.specialties(false);}
    @GetMapping("/specialties") List<SchedulingService.Specialty> specialties(){return scheduling.specialties(true);}
    @PostMapping("/admin/specialties") @PreAuthorize("hasRole('ADMIN')") ResponseEntity<SchedulingService.Specialty> createSpecialty(@Valid @RequestBody SpecialtyRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(scheduling.createSpecialty(r.code(),r.name(),r.durationMinutes(),r.general()));}
    @PatchMapping("/admin/specialties/{id}") @PreAuthorize("hasRole('ADMIN')") SchedulingService.Specialty patchSpecialty(@PathVariable Long id,@Valid @RequestBody SpecialtyPatch r){return scheduling.updateSpecialty(id,r.name(),r.durationMinutes(),r.active());}
    @PostMapping("/admin/professionals") @PreAuthorize("hasRole('ADMIN')") ResponseEntity<Map<String,Long>> createProfessional(@Valid @RequestBody ProfessionalRequest r){Long id=scheduling.createProfessional(r.firstName(),r.lastName(),r.documentType(),r.documentNumber(),r.email(),r.phone(),passwords.hash(r.temporaryPassword()),r.professionalCode(),r.licenseNumber());return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id",id));}
    @GetMapping("/admin/professionals") @PreAuthorize("hasRole('ADMIN')") List<Map<String,Object>> professionals(){return scheduling.professionals();}
    @PutMapping("/admin/professionals/{id}/specialties") @PreAuthorize("hasRole('ADMIN')") ResponseEntity<Void> specialties(@PathVariable Long id,@Valid @RequestBody SpecialtiesRequest r){scheduling.setProfessionalSpecialties(id,r.specialtyIds(),r.primarySpecialtyId());return ResponseEntity.noContent().build();}
    @PutMapping("/admin/professionals/{id}/locations") @PreAuthorize("hasRole('ADMIN')") ResponseEntity<Void> locations(@PathVariable Long id,@Valid @RequestBody LocationsRequest r){scheduling.setProfessionalLocations(id,r.locationIds());return ResponseEntity.noContent().build();}
    @PatchMapping("/admin/professionals/{id}/active") @PreAuthorize("hasRole('ADMIN')") ResponseEntity<Void> active(@PathVariable Long id,@RequestBody ActiveRequest r){scheduling.setProfessionalActive(id,r.active());return ResponseEntity.noContent().build();}
    @GetMapping("/professional/availability-blocks") @PreAuthorize("hasRole('PROFESSIONAL')") List<SchedulingService.Block> blocks(Authentication a,@RequestParam(required=false) LocalDate date,@RequestParam(required=false) Long locationId){return scheduling.blocks(user(a),date,locationId);}
    @PostMapping("/professional/availability-blocks") @PreAuthorize("hasRole('PROFESSIONAL')") ResponseEntity<SchedulingService.Block> createBlock(Authentication a,@Valid @RequestBody BlockRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(scheduling.createBlock(user(a),r.locationId(),r.date(),r.startTime(),r.endTime()));}
    @PatchMapping("/professional/availability-blocks/{id}") @PreAuthorize("hasRole('PROFESSIONAL')") SchedulingService.Block updateBlock(Authentication a,@PathVariable Long id,@Valid @RequestBody BlockPatch r){return scheduling.updateBlock(user(a),id,r.locationId(),r.date(),r.startTime(),r.endTime());}
    @DeleteMapping("/professional/availability-blocks/{id}") @PreAuthorize("hasRole('PROFESSIONAL')") ResponseEntity<Void> deleteBlock(Authentication a,@PathVariable Long id){scheduling.deleteBlock(user(a),id);return ResponseEntity.noContent().build();}
    @GetMapping("/availability") @PreAuthorize("hasRole('USER')") List<SchedulingService.Available> availability(@RequestParam Long locationId,@RequestParam Long specialtyId,@RequestParam(required=false) Long professionalId,@RequestParam LocalDate date){return scheduling.availability(locationId,specialtyId,professionalId,date);}
    @PostMapping("/appointments") @PreAuthorize("hasRole('USER')") ResponseEntity<SchedulingService.Appointment> reserve(Authentication a,@Valid @RequestBody AppointmentRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(scheduling.reserve(user(a),r.professionalId(),r.locationId(),r.specialtyId(),LocalDateTime.of(r.date(),r.startTime()),r.reason()));}
    @GetMapping("/admin/appointments/pending-specialized") @PreAuthorize("hasRole('ADMIN')") List<Map<String,Object>> pending(){return scheduling.pending();}
    @PostMapping("/admin/appointments/{id}/decision") @PreAuthorize("hasRole('ADMIN')") SchedulingService.Appointment decide(Authentication a,@PathVariable Long id,@Valid @RequestBody DecisionRequest r){return scheduling.decide(user(a),id,r.decision().trim().toUpperCase(Locale.ROOT),r.reason());}
    private Long user(Authentication a){try{return Long.valueOf(a.getName());}catch(Exception e){throw new IllegalStateException("Principal inválido");}}
}
