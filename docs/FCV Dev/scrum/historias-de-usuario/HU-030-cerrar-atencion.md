---
id: HU-030
tipo: historia-de-usuario
titulo: "Cerrar atención"
estado: Pendiente de aprobación
epica: "[[EP-007-operacion-profesional-y-administrativa]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 6"
dependencias: ["[[HU-029-consultar-agenda-profesional]]"]
relacionadas: ["[[HU-032-consultar-auditoria-de-estados]]"]
---
# HU-030 — Cerrar atención
## Historia de usuario
**COMO** PROFESSIONAL  
**QUIERO** marcar una cita pasada/aplicable como `COMPLETED` o `NO_SHOW`  
**PARA** cerrar su atención con trazabilidad.
## Contexto y descripción
El PRD no permite que PROFESSIONAL haga otras decisiones de cita.
## Alcance
- Validar cita propia y aplicable, transición explícita e historial.
## Fuera de alcance
- Cerrar cita ajena/no aplicable, cancelar o aprobar solicitudes.
## Reglas de negocio
- Solo `COMPLETED`/`NO_SHOW`; debe registrarse historial.
## Dependencias y relaciones
- Épica: [[EP-007-operacion-profesional-y-administrativa]]
- Dependencias: [[HU-029-consultar-agenda-profesional]].
- Relacionadas: [[HU-032-consultar-auditoria-de-estados]].
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** requiere transición de estado, temporalidad y ownership rigurosos.
## Tareas de desarrollo
- [ ] **T-01 — Definir aplicabilidad.** Dificultad: Alto. Traducir “pasada/aplicable” a condición verificable aprobada.
- [ ] **T-02 — Aplicar transición/auditoría.** Dificultad: Alto. Restringir actor/estado y guardar fuente/fecha.
- [ ] **T-03 — Integrar acción/pruebas.** Dificultad: Medio. Cubrir ambos resultados y casos denegados.
## Criterios de aceptación
### CA-01 — Cierre permitido
**Dado** una cita propia pasada/aplicable, **cuando** PROFESSIONAL selecciona `COMPLETED` o `NO_SHOW`, **entonces** se registra el estado elegido.
### CA-02 — Restricción de propiedad/aplicabilidad
**Dado** una cita ajena o no aplicable, **cuando** PROFESSIONAL intenta cerrarla, **entonces** la aplicación lo impide.
### CA-03 — Auditoría
**Dado** un cierre exitoso, **cuando** se consulta historial, **entonces** consta cita, estado nuevo, actor/fuente y fecha/hora aplicables.
## Definition of Done
- [ ] CA-01 a CA-03 probados en dominio/REST, autorización y auditoría.
- [ ] La definición operativa de “aplicable” queda acordada/documentada antes de completar.
- [ ] Cliente y trazabilidad Scrum actualizados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 / DoD | Pendiente | — | Requiere regla aplicable. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
## Notas y decisiones
- Pregunta abierta: definición de condición “aplicable”.
