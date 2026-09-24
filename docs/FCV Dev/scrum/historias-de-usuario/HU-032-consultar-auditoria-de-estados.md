---
id: HU-032
tipo: historia-de-usuario
titulo: "Consultar auditoría de estados"
estado: Pendiente de aprobación
epica: "[[EP-007-operacion-profesional-y-administrativa]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 6"
dependencias: ["[[HU-022-reservar-cita-general]]", "[[HU-024-resolver-solicitud-especializada]]", "[[HU-026-cancelar-cita]]", "[[HU-028-resolver-reprogramacion]]", "[[HU-030-cerrar-atencion]]"]
relacionadas: []
---
# HU-032 — Consultar auditoría de estados
## Historia de usuario
**COMO** actor autorizado  
**QUIERO** consultar el historial de estados de una cita dentro de mi alcance  
**PARA** entender decisiones y transiciones sin editar la auditoría.
## Contexto y descripción
Cada cambio guarda cita, estado nuevo, actor cuando existe, fuente SYSTEM/USER/ADMIN, fecha/hora y motivo opcional.
## Alcance
- Registro inmutable en cada transición y lectura protegida según ownership/rol aprobado.
## Fuera de alcance
- CRUD normal de auditoría o acceso sin autorización.
## Reglas de negocio
- Auditoría no modificable; cambios de estado explícitos y verificables.
## Dependencias y relaciones
- Épica: [[EP-007-operacion-profesional-y-administrativa]]
- Dependencias: [[HU-022-reservar-cita-general]], [[HU-024-resolver-solicitud-especializada]], [[HU-026-cancelar-cita]], [[HU-028-resolver-reprogramacion]], [[HU-030-cerrar-atencion]].
- Relacionadas: Ninguna.
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** es transversal a transiciones, fuentes, seguridad e inmutabilidad.
## Tareas de desarrollo
- [ ] **T-01 — Centralizar registro de transición.** Dificultad: Alto. Evitar cambios sin historial.
- [ ] **T-02 — Definir consulta autorizada.** Dificultad: Alto. Aplicar ownership/rol y campos del PRD.
- [ ] **T-03 — Probar inmutabilidad/cobertura.** Dificultad: Alto. Comprobar todas las fuentes/estados aplicables.
## Criterios de aceptación
### CA-01 — Datos de auditoría completos
**Dado** un cambio de estado, **cuando** se registra, **entonces** incluye cita, estado nuevo, actor cuando existe, fuente, fecha/hora y motivo opcional.
### CA-02 — Inmutabilidad
**Dado** un registro histórico, **cuando** se intenta modificarlo por operaciones normales, **entonces** no es posible.
### CA-03 — Lectura restringida
**Dado** una consulta de historial, **cuando** el actor no tiene ownership/rol suficiente, **entonces** no obtiene la auditoría fuera de su alcance.
## Definition of Done
- [ ] CA-01 a CA-03 probados con cada transición relevante, persistencia y autorización.
- [ ] Migración/índices aplicables y contrato de lectura verificados.
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
- El rol exacto de lectura administrativa se concreta con el contrato aprobado.
