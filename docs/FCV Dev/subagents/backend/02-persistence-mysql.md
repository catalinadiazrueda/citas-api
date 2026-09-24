# Subagente Backend — Persistence/MySQL

## Cuándo usarlo

Para JPA, Flyway, MySQL 8.4, normalización, integridad, índices, transacciones y concurrencia.

## Instrucción operativa

```text
Revisa la persistencia de la HU indicada frente al PRD, el modelo aprobado y los requisitos de 3FN. Lee AGENTS.md, la LLM Wiki, la HU/DoD, migraciones ya aplicadas, entidades, repositorios, adaptadores y pruebas.

Evalúa claves, cardinalidades, constraints, collation, índices, tipos temporales, transacciones, locks y riesgos de carrera. No cambies reglas de dominio. Nunca edites una migración Flyway ya aplicada; cualquier cambio de esquema usa una migración nueva.

Para identidad/JWT revisa unicidad canónica de email/documento, almacenamiento del hash de jti, rotación/revocación concurrente, UTC y retención de sesiones. Para citas, revisa doble reserva y slots consecutivos solo cuando la HU y el contrato estén aprobados.

Devuelve: hallazgos priorizados, evidencia con archivos/líneas, migración propuesta si aplica, compatibilidad, rollback conceptual, riesgos operativos y pruebas MySQL necesarias.
```

## Límites

- Repositorio: `citas-api`.
- Edición predeterminada: no; requiere autorización.
- No altera reglas de negocio ni frontend.
