---
id: HU-024
tipo: historia-de-usuario
titulo: "Resolver solicitud especializada"
estado: Aprobada
epica: "[[EP-005-busqueda-y-reserva-de-citas]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 4"
dependencias: ["[[HU-023-solicitar-cita-especializada]]"]
relacionadas: ["[[HU-031-consultar-bandeja-administrativa]]", "[[HU-032-consultar-auditoria-de-estados]]"]
---
# HU-024 — Resolver solicitud especializada
## Historia de usuario
**COMO** ADMIN  
**QUIERO** aprobar o rechazar una cita especializada solicitada  
**PARA** decidir la atención y liberar la franja cuando corresponda.
## Contexto y descripción
El rechazo exige motivo; aprobar cambia a `APPROVED`, rechazar a `REJECTED` y libera slots.
## Alcance
- Decisión ADMIN sobre `REQUESTED`, validación de transición, motivo de rechazo, slots e historial.
## Fuera de alcance
- Decidir citas generales o modificar selección clínica.
## Reglas de negocio
- Rechazo exige motivo; transiciones explícitas; rechazo libera la reserva.
## Dependencias y relaciones
- Épica: [[EP-005-busqueda-y-reserva-de-citas]]
- Dependencias: [[HU-023-solicitar-cita-especializada]].
- Relacionadas: [[HU-031-consultar-bandeja-administrativa]], [[HU-032-consultar-auditoria-de-estados]].
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** coordina autorización, transición, liberación y auditoría.
## Tareas de desarrollo
- [ ] **T-01 — Definir decisión ADMIN.** Dificultad: Medio. Acordar precondición `REQUESTED`, motivo y errores.
- [ ] **T-02 — Aplicar transición atómica.** Dificultad: Alto. Aprobar o rechazar/liberar sin estados intermedios.
- [ ] **T-03 — Integrar bandeja/pruebas.** Dificultad: Alto. Cubrir rol, motivo obligatorio, slots e historial.
## Criterios de aceptación
### CA-01 — Aprobación
**Dado** una cita `REQUESTED`, **cuando** ADMIN aprueba, **entonces** pasa a `APPROVED` y conserva la reserva.
### CA-02 — Rechazo con motivo
**Dado** una cita `REQUESTED`, **cuando** ADMIN rechaza con motivo, **entonces** pasa a `REJECTED`, registra motivo y libera slots.
### CA-03 — Decisión válida
**Dado** actor no ADMIN, estado distinto de `REQUESTED` o rechazo sin motivo, **cuando** intenta decidir, **entonces** se rechaza sin transición.
## Definition of Done
- [ ] CA-01 a CA-03 probados con persistencia/REST, cliente ADMIN y auditoría.
- [ ] Transición/liberación atómica y contrato cross-repo verificables.
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
- La bandeja se especifica en [[HU-031-consultar-bandeja-administrativa]].
