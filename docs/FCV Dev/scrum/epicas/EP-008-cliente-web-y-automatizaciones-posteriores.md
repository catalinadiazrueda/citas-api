---
id: EP-008
tipo: epica
titulo: "Cliente web y automatizaciones posteriores"
estado: Pendiente de aprobación
historias:
  - "[[HU-033-integrar-cliente-web-con-api]]"
  - "[[HU-034-automatizar-recordatorios]]"
  - "[[HU-035-notificar-cambios-de-estado]]"
  - "[[HU-036-generar-resumen-operativo-diario]]"
dependencias:
  - "[[EP-001-fundacion-y-contrato-del-producto]]"
  - "[[EP-007-operacion-profesional-y-administrativa]]"
---

# EP-008 — Cliente web y automatizaciones posteriores

## Objetivo

Completar el consumo web directo del producto y preparar automatizaciones S5/S6 sin alterar el núcleo funcional.

## Valor esperado

Las capacidades aprobadas son accesibles por pantallas obligatorias y generan comunicaciones/resúmenes posteriores de forma segura.

## Actores

- USER
- PROFESSIONAL
- ADMIN
- Equipo de operación

## Alcance

- Integración UI/API y workflows n8n de recordatorio, cambio de estado y resumen diario.

## Fuera de alcance

- Express/BFF, SMS/WhatsApp, SMTP obligatorio y credenciales en JSON.

## Reglas de negocio

- La API se consume directamente y la URL se configura por environment.
- n8n no modifica el núcleo; sus exportaciones no incluyen credenciales.

## Dependencias

- [[EP-001-fundacion-y-contrato-del-producto]]
- [[EP-007-operacion-profesional-y-administrativa]]

## Historias de usuario

- [[HU-033-integrar-cliente-web-con-api]]
- [[HU-034-automatizar-recordatorios]]
- [[HU-035-notificar-cambios-de-estado]]
- [[HU-036-generar-resumen-operativo-diario]]

## Criterio de completitud de la épica

- [ ] Las HU están `Completada`; la integración no añade BFF y los workflows versionados no contienen secretos.

## Riesgos e incógnitas

- Falta seleccionar framework y obtener configuración operativa del trainer para n8n/Gmail.
