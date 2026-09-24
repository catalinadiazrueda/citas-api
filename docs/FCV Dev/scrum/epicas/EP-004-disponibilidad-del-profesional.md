---
id: EP-004
tipo: epica
titulo: "Disponibilidad del profesional"
estado: Pendiente de aprobación
historias:
  - "[[HU-018-crear-bloques-de-disponibilidad]]"
  - "[[HU-019-modificar-bloques-futuros]]"
  - "[[HU-020-consultar-calendario-de-disponibilidad]]"
dependencias:
  - "[[EP-003-administracion-de-catalogos-y-profesionales]]"
---

# EP-004 — Disponibilidad del profesional

## Objetivo

Permitir a PROFESSIONAL publicar y mantener bloques futuros utilizables para reservas.

## Valor esperado

La disponibilidad no se solapa, respeta sedes asignadas y se expresa en slots de 30 minutos.

## Actores

- PROFESSIONAL

## Alcance

- Crear, editar/eliminar bloques futuros y consultar calendario propio.

## Fuera de alcance

- Reserva por profesional de otro usuario o edición de bloques con citas comprometidas.

## Reglas de negocio

- No pasado, no solapamiento y solo sedes asignadas.
- Cada bloque se discretiza en slots de 30 min.

## Dependencias

- [[EP-003-administracion-de-catalogos-y-profesionales]]

## Historias de usuario

- [[HU-018-crear-bloques-de-disponibilidad]]
- [[HU-019-modificar-bloques-futuros]]
- [[HU-020-consultar-calendario-de-disponibilidad]]

## Criterio de completitud de la épica

- [ ] Las HU están `Completada` y las búsquedas pueden partir de bloques válidos.

## Riesgos e incógnitas

- La visualización concreta de calendario depende del diseño aprobado del cliente.
