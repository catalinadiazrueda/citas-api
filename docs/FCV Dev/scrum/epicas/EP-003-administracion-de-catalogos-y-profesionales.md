---
id: EP-003
tipo: epica
titulo: "Administración de catálogos y profesionales"
estado: Pendiente de aprobación
historias:
  - "[[HU-012-gestionar-eps]]"
  - "[[HU-013-gestionar-planes-eps]]"
  - "[[HU-014-gestionar-especialidades-y-duracion]]"
  - "[[HU-015-crear-profesional]]"
  - "[[HU-016-asignar-especialidades-al-profesional]]"
  - "[[HU-017-asignar-sedes-y-estado-del-profesional]]"
dependencias:
  - "[[EP-001-fundacion-y-contrato-del-producto]]"
---

# EP-003 — Administración de catálogos y profesionales

## Objetivo

Permitir a ADMIN configurar la oferta sintética que sustenta la disponibilidad y las citas.

## Valor esperado

Las reservas se basan en catálogos consistentes y profesionales habilitados para una especialidad y sede.

## Actores

- ADMIN

## Alcance

- EPS, planes, especialidades/duración y configuración de PROFESSIONAL.

## Fuera de alcance

- Borrado físico de catálogos referenciados y datos reales.

## Reglas de negocio

- Catálogos referenciados se desactivan en lugar de eliminarse físicamente.
- Un profesional puede tener varias especialidades/sedes y una especialidad primaria.

## Dependencias

- [[EP-001-fundacion-y-contrato-del-producto]]

## Historias de usuario

- [[HU-012-gestionar-eps]]
- [[HU-013-gestionar-planes-eps]]
- [[HU-014-gestionar-especialidades-y-duracion]]
- [[HU-015-crear-profesional]]
- [[HU-016-asignar-especialidades-al-profesional]]
- [[HU-017-asignar-sedes-y-estado-del-profesional]]

## Criterio de completitud de la épica

- [ ] Las HU están `Completada` y existe oferta activa elegible para publicar agenda.

## Riesgos e incógnitas

- Debe acordarse la representación final del catálogo de Medicina General sin apartarse del PRD.
