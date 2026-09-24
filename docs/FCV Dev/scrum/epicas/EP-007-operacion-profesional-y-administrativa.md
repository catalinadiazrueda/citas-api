---
id: EP-007
tipo: epica
titulo: "Operación profesional y administrativa"
estado: Pendiente de aprobación
historias:
  - "[[HU-029-consultar-agenda-profesional]]"
  - "[[HU-030-cerrar-atencion]]"
  - "[[HU-031-consultar-bandeja-administrativa]]"
  - "[[HU-032-consultar-auditoria-de-estados]]"
dependencias:
  - "[[EP-005-busqueda-y-reserva-de-citas]]"
  - "[[EP-006-ciclo-de-vida-de-citas-y-reprogramaciones]]"
---

# EP-007 — Operación profesional y administrativa

## Objetivo

Dar a PROFESSIONAL y ADMIN visibilidad y acciones limitadas por rol sobre la operación de citas.

## Valor esperado

La atención se cierra correctamente y las decisiones/estados se consultan sin exponer citas ajenas.

## Actores

- PROFESSIONAL
- ADMIN

## Alcance

- Agenda propia, cierre, bandeja administrativa y auditoría.

## Fuera de alcance

- Historia clínica, edición normal de auditoría o visibilidad de agenda ajena.

## Reglas de negocio

- PROFESSIONAL ve solo sus `APPROVED`; cierre `COMPLETED`/`NO_SHOW` aplicable a cita pasada.
- Auditoría es inmutable y registra actor/fuente/fecha/motivo cuando aplique.

## Dependencias

- [[EP-005-busqueda-y-reserva-de-citas]]
- [[EP-006-ciclo-de-vida-de-citas-y-reprogramaciones]]

## Historias de usuario

- [[HU-029-consultar-agenda-profesional]]
- [[HU-030-cerrar-atencion]]
- [[HU-031-consultar-bandeja-administrativa]]
- [[HU-032-consultar-auditoria-de-estados]]

## Criterio de completitud de la épica

- [ ] Las HU están `Completada` con autorización y trazabilidad de los cambios.

## Riesgos e incógnitas

- La política visual de datos mínimos del paciente debe derivarse del PRD y ownership, sin añadir PII.
