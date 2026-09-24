---
id: EP-006
tipo: epica
titulo: "Ciclo de vida de citas y reprogramaciones"
estado: Pendiente de aprobación
historias:
  - "[[HU-025-consultar-mis-citas]]"
  - "[[HU-026-cancelar-cita]]"
  - "[[HU-027-solicitar-reprogramacion]]"
  - "[[HU-028-resolver-reprogramacion]]"
dependencias:
  - "[[EP-005-busqueda-y-reserva-de-citas]]"
---

# EP-006 — Ciclo de vida de citas y reprogramaciones

## Objetivo

Permitir al USER consultar, cancelar y reprogramar una cita sin perder el estado ni la franja original de forma indebida.

## Valor esperado

El ciclo posterior a la reserva protege la agenda y deja decisiones administrativas auditables.

## Actores

- USER
- ADMIN

## Alcance

- Mis citas, cancelación y decisión de reprogramación.

## Fuera de alcance

- Reactivación directa de una cita cancelada o cambio de profesional mediante reprogramación.

## Reglas de negocio

- Cancelar libera slots; reprogramar solo una cita futura `APPROVED`.
- La cita original se conserva hasta aprobar; rechazo libera solo la nueva franja provisional.

## Dependencias

- [[EP-005-busqueda-y-reserva-de-citas]]

## Historias de usuario

- [[HU-025-consultar-mis-citas]]
- [[HU-026-cancelar-cita]]
- [[HU-027-solicitar-reprogramacion]]
- [[HU-028-resolver-reprogramacion]]

## Criterio de completitud de la épica

- [ ] Las HU están `Completada` y no se destruye una cita original mientras hay reprogramación pendiente.

## Riesgos e incógnitas

- Las transiciones terminales exactas deben respetar los estados fijos aprobados.
