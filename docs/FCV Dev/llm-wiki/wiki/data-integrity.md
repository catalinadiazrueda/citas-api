# Datos e integridad

## HECHO

El diseño debe alcanzar 3FN, resolver relaciones N:M con tablas puente, evitar duplicación de catálogos y justificar claves, índices, snapshots, auditoría y prevención de doble reserva.

## PREGUNTA ABIERTA

La estrategia concreta de concurrencia, retención de slots, zona horaria y representación de fechas aún no está aprobada.

## DECISIÓN — 2026-09-22 · Modelo de identidad integrado

Flyway V1 usa el modelo 3FN de referencia: `users`, `roles`, `user_roles` y `refresh_tokens`. La relación usuario–rol es N:M mediante puente y cada refresh referencia exactamente un usuario. Email canónico tiene UK global; documento usa UK `(document_type, document_number)`. `token_hash` tiene UK y nunca se guarda el JWT. El índice de usuario y la búsqueda por hash con bloqueo de fila permiten rotación concurrente.

Los atributos personales dependen solo de `users.id`, nombres de rol solo de `roles.id` y vigencia/revocación solo de `refresh_tokens.id`; no hay listas ni dependencias parciales o transitivas entre atributos no clave en este corte. Flyway registra baseline 0 para adoptar una base de referencia ya cargada y ejecuta V1 idempotente.
