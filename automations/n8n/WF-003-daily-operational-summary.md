# WF-003 — Caso adicional: resumen operativo diario

**Uso:** actividad extra si el grupo avanza rápido.

**Trigger:** Schedule.

**Flujo:** API → citas del día → agrupar por sede/estado/especialidad → construir resumen → Gmail.

## Resultado esperado
Un correo de laboratorio con métricas simples:
- total por sede;
- APPROVED/COMPLETED/NO_SHOW/CANCELLED;
- distribución por especialidad;
- incidencias de API si existen.

No requiere información privada real.
