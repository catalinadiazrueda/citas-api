---
id: HU-027
tipo: historia-de-usuario
titulo: "Solicitar reprogramación"
estado: Pendiente de aprobación
epica: "[[EP-006-ciclo-de-vida-de-citas-y-reprogramaciones]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 5"
dependencias: ["[[HU-021-buscar-disponibilidad]]", "[[HU-025-consultar-mis-citas]]"]
relacionadas: ["[[HU-028-resolver-reprogramacion]]"]
---
# HU-027 — Solicitar reprogramación
## Historia de usuario
**COMO** USER  
**QUIERO** solicitar una nueva fecha/hora disponible para mi cita aprobada  
**PARA** cambiarla sin perder la franja original antes de la decisión ADMIN.
## Contexto y descripción
Conserva profesional/especialidad; cambiar profesional es una nueva cita. La solicitud nace `PENDING` y retiene nueva franja.
## Alcance
- Elegibilidad, nueva franja válida, retención y solicitud `PENDING` que conserva cita original.
## Fuera de alcance
- Cambiar profesional/especialidad, aprobar automáticamente o liberar franja original al solicitar.
## Reglas de negocio
- Solo `APPROVED` futura; nueva reserva completa; original se preserva hasta decisión.
## Dependencias y relaciones
- Épica: [[EP-006-ciclo-de-vida-de-citas-y-reprogramaciones]]
- Dependencias: [[HU-021-buscar-disponibilidad]], [[HU-025-consultar-mis-citas]].
- Relacionadas: [[HU-028-resolver-reprogramacion]].
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** requiere doble reserva coordinada y conserva el estado original.
## Tareas de desarrollo
- [ ] **T-01 — Validar cita/elegibilidad.** Dificultad: Alto. Exigir propia, futura y `APPROVED`.
- [ ] **T-02 — Retener nueva franja.** Dificultad: Alto. Mantener profesional/especialidad y evitar doble reserva.
- [ ] **T-03 — Persistir solicitud/pruebas.** Dificultad: Alto. Conservar original y auditar `PENDING`.
## Criterios de aceptación
### CA-01 — Solicitud válida
**Dado** una cita propia futura `APPROVED`, **cuando** USER selecciona nueva franja disponible del mismo profesional/especialidad, **entonces** se crea reprogramación `PENDING` y se retiene esa franja.
### CA-02 — Cita original preservada
**Dado** la solicitud pendiente, **cuando** se consulta la cita original, **entonces** conserva su franja hasta decisión ADMIN.
### CA-03 — Restricciones
**Dado** cita no elegible, cambio de profesional/especialidad o franja no disponible, **cuando** se solicita, **entonces** se rechaza sin alterar citas/reservas.
## Definition of Done
- [ ] CA-01 a CA-03 probados con concurrencia/persistencia, ownership y auditoría.
- [ ] Contrato/cliente y migración/índices aplicables verificables.
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
- Se conserva el significado exacto de `PENDING` del catálogo fijo.
