---
id: HU-026
tipo: historia-de-usuario
titulo: "Cancelar cita"
estado: Pendiente de aprobación
epica: "[[EP-006-ciclo-de-vida-de-citas-y-reprogramaciones]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 5"
dependencias: ["[[HU-025-consultar-mis-citas]]"]
relacionadas: ["[[HU-032-consultar-auditoria-de-estados]]"]
---
# HU-026 — Cancelar cita
## Historia de usuario
**COMO** USER  
**QUIERO** cancelar una cita futura no terminal  
**PARA** liberar su franja cuando ya no la necesito.
## Contexto y descripción
Una cancelada no se reactiva directamente y debe registrarse historial.
## Alcance
- Validar ownership/estado/futuro, transición `CANCELLED`, liberación y auditoría.
## Fuera de alcance
- Reactivación directa o cancelación de cita terminal/no futura.
## Reglas de negocio
- `CANCELLED` libera slots; transiciones explícitas e historial obligatorio.
## Dependencias y relaciones
- Épica: [[EP-006-ciclo-de-vida-de-citas-y-reprogramaciones]]
- Dependencias: [[HU-025-consultar-mis-citas]].
- Relacionadas: [[HU-032-consultar-auditoria-de-estados]].
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** cambia estado y disponibilidad sin permitir transiciones inválidas.
## Tareas de desarrollo
- [ ] **T-01 — Definir elegibilidad.** Dificultad: Alto. Validar futuro, no terminal y ownership.
- [ ] **T-02 — Aplicar cancelación atómica.** Dificultad: Alto. Actualizar estado, liberar slots y auditar.
- [ ] **T-03 — Integrar acción/pruebas.** Dificultad: Medio. Cubrir éxito, inválida y visibilidad posterior.
## Criterios de aceptación
### CA-01 — Cancelación permitida
**Dado** una cita propia futura no terminal, **cuando** USER cancela, **entonces** cambia a `CANCELLED` y libera sus slots.
### CA-02 — Cancelación restringida
**Dado** cita ajena, pasada o terminal, **cuando** USER intenta cancelar, **entonces** se rechaza sin modificarla.
### CA-03 — No reactivación directa
**Dado** una cita `CANCELLED`, **cuando** se intenta restaurarla por la misma capacidad, **entonces** no se permite y el historial conserva la cancelación.
## Definition of Done
- [ ] CA-01 a CA-03 probados con estado, slots, ownership y auditoría.
- [ ] Contrato/cliente y persistencia/índices aplicables verificables.
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
- El catálogo determina cuáles estados son terminales.
