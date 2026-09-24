---
id: HU-016
tipo: historia-de-usuario
titulo: "Asignar especialidades al profesional"
estado: Aprobada
epica: "[[EP-003-administracion-de-catalogos-y-profesionales]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 3"
dependencias: ["[[HU-014-gestionar-especialidades-y-duracion]]", "[[HU-015-crear-profesional]]"]
relacionadas: ["[[HU-021-buscar-disponibilidad]]"]
---
# HU-016 — Asignar especialidades al profesional
## Historia de usuario
**COMO** ADMIN  
**QUIERO** asignar una o más especialidades y una primaria a PROFESSIONAL  
**PARA** habilitar reservas solo en su oferta autorizada.
## Contexto y descripción
La relación es N:M; se requiere exactamente la marcación primaria que indique el PRD cuando exista asignación.
## Alcance
- Gestionar asociaciones a especialidades activas y designar/cambiar primaria.
## Fuera de alcance
- Modificar duración por profesional o asignar especialidad inexistente/inactiva.
## Reglas de negocio
- Una especialidad debe estar activa y asociada al profesional para reservarse.
## Dependencias y relaciones
- Épica: [[EP-003-administracion-de-catalogos-y-profesionales]]
- Dependencias: [[HU-014-gestionar-especialidades-y-duracion]], [[HU-015-crear-profesional]].
- Relacionadas: [[HU-021-buscar-disponibilidad]].
## Esfuerzo
**Nivel:** Medio. **Justificación de dificultad:** aplica N:M, vigencia y una regla de primariedad.
## Tareas de desarrollo
- [ ] **T-01 — Modelar asociación N:M.** Dificultad: Medio. Garantizar integridad y primaria aprobada.
- [ ] **T-02 — Exponer gestión ADMIN.** Dificultad: Medio. Validar especialidad activa y rol.
- [ ] **T-03 — Probar y reflejar en UI.** Dificultad: Medio. Cubrir múltiple, primaria y reserva no habilitada.
## Criterios de aceptación
### CA-01 — Múltiples especialidades
**Dado** ADMIN, profesional y especialidades activas, **cuando** realiza asignaciones válidas, **entonces** el profesional puede tener una o más asociaciones.
### CA-02 — Especialidad primaria
**Dado** asociaciones del profesional, **cuando** ADMIN define la primaria, **entonces** la selección queda identificada de forma consistente.
### CA-03 — Reserva restringida
**Dado** una especialidad no asociada o inactiva, **cuando** se busca/reserva con ese profesional, **entonces** no se ofrece como opción válida.
## Definition of Done
- [ ] CA-01 a CA-03 validados con pruebas de relación, rol y disponibilidad.
- [ ] Persistencia 3FN/migración aplicable y cliente ADMIN verificados.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 / DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
## Notas y decisiones
- La unicidad exacta de primaria se justificará en modelo/contrato.
