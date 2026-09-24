# WF-001 — Recordatorio de citas próximas

**Trigger:** Schedule.

**Objetivo:** consultar citas `APPROVED` dentro de una ventana configurable (ej. próximas 24 h), enviar Gmail al usuario ficticio/de laboratorio y registrar resultado.

## Requisitos
- no enviar recordatorio a CANCELLED/REJECTED;
- evitar duplicado para la misma cita/ventana según estrategia del estudiante;
- manejar API no disponible;
- credenciales fuera del JSON;
- ejecución de prueba controlada antes de activar.

## Entregable
`WF-001-appointment-reminders.json`.
