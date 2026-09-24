---
id: EP-005
tipo: epica
titulo: "Búsqueda y reserva de citas"
estado: Pendiente de aprobación
historias:
  - "[[HU-021-buscar-disponibilidad]]"
  - "[[HU-022-reservar-cita-general]]"
  - "[[HU-023-solicitar-cita-especializada]]"
  - "[[HU-024-resolver-solicitud-especializada]]"
dependencias:
  - "[[EP-002-identidad-y-perfil-del-usuario]]"
  - "[[EP-003-administracion-de-catalogos-y-profesionales]]"
  - "[[EP-004-disponibilidad-del-profesional]]"
---

# EP-005 — Búsqueda y reserva de citas

## Objetivo

Ofrecer al USER horarios válidos y permitir reservar una cita general o solicitar una especializada sin doble reserva.

## Valor esperado

El usuario obtiene un resultado de reserva acorde a la naturaleza general o especializada de la atención.

## Actores

- USER
- ADMIN

## Alcance

- Filtros de disponibilidad, reserva general, solicitud especializada y resolución ADMIN.

## Fuera de alcance

- Pago, facturación o aprobación por PROFESSIONAL.

## Reglas de negocio

- Duración 30/60, slots consecutivos para 60, sin doble reserva.
- General se aprueba automáticamente; especializada nace `REQUESTED` y se retiene.

## Dependencias

- [[EP-002-identidad-y-perfil-del-usuario]]
- [[EP-003-administracion-de-catalogos-y-profesionales]]
- [[EP-004-disponibilidad-del-profesional]]

## Historias de usuario

- [[HU-021-buscar-disponibilidad]]
- [[HU-022-reservar-cita-general]]
- [[HU-023-solicitar-cita-especializada]]
- [[HU-024-resolver-solicitud-especializada]]

## Criterio de completitud de la épica

- [ ] Las HU están `Completada` y las reservas liberan/retienen slots conforme a su estado.

## Riesgos e incógnitas

- El mecanismo transaccional concreto para impedir carreras debe diseñarse y verificarse durante implementación.
