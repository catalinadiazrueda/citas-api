---
id: HU-028
tipo: historia-de-usuario
titulo: "Resolver reprogramación"
estado: Pendiente de aprobación
epica: "[[EP-006-ciclo-de-vida-de-citas-y-reprogramaciones]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 5"
dependencias: ["[[HU-027-solicitar-reprogramacion]]"]
relacionadas: ["[[HU-031-consultar-bandeja-administrativa]]", "[[HU-032-consultar-auditoria-de-estados]]"]
---
# HU-028 — Resolver reprogramación
## Historia de usuario
**COMO** ADMIN  
**QUIERO** aprobar o rechazar una reprogramación pendiente  
**PARA** asignar la nueva franja o mantener segura la cita original.
## Contexto y descripción
Al aprobar libera slots antiguos, asigna nuevos y actualiza cita; al rechazar libera nueva reserva y mantiene la original. Rechazo lleva motivo cuando corresponda.
## Alcance
- Decisión ADMIN sobre `PENDING`, actualización/liberación atómica e historial.
## Fuera de alcance
- Cambio de profesional o cancelación automática de original tras rechazo.
## Reglas de negocio
- Aprobar intercambia franjas; rechazar conserva original y libera provisional; transiciones explícitas.
## Dependencias y relaciones
- Épica: [[EP-006-ciclo-de-vida-de-citas-y-reprogramaciones]]
- Dependencias: [[HU-027-solicitar-reprogramacion]].
- Relacionadas: [[HU-031-consultar-bandeja-administrativa]], [[HU-032-consultar-auditoria-de-estados]].
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** modifica reservas y cita existente de forma transaccional.
## Tareas de desarrollo
- [ ] **T-01 — Definir decisión/motivo.** Dificultad: Medio. Acordar respuesta y validación ADMIN.
- [ ] **T-02 — Aplicar resultado atómico.** Dificultad: Alto. Aprobar intercambiando slots o rechazar liberando provisional.
- [ ] **T-03 — Integrar bandeja/pruebas.** Dificultad: Alto. Cubrir estados, motivo e historial.
## Criterios de aceptación
### CA-01 — Aprobación de cambio
**Dado** una reprogramación `PENDING`, **cuando** ADMIN aprueba, **entonces** la cita usa nueva franja y la franja antigua se libera.
### CA-02 — Rechazo conservador
**Dado** una reprogramación `PENDING`, **cuando** ADMIN rechaza con motivo aplicable, **entonces** se libera la nueva franja y la cita original se mantiene.
### CA-03 — Decisión protegida
**Dado** actor no ADMIN o solicitud no pendiente, **cuando** intenta resolver, **entonces** se rechaza sin alterar franjas/cita.
## Definition of Done
- [ ] CA-01 a CA-03 probados con persistencia, concurrencia, rol y auditoría.
- [ ] Contrato/cliente/migración aplicables y motivo conforme al PRD verificados.
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
- Tras rechazo USER conserva o cancela la cita mediante [[HU-026-cancelar-cita]].
