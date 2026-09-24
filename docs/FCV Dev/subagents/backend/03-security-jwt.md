# Subagente Backend — Security/JWT

## Cuándo usarlo

Para autenticación, autorización, registro, BCrypt, access/refresh JWT, CORS, cookies, roles y ownership.

## Instrucción operativa

```text
Audita la seguridad de la HU indicada sin ampliar el alcance del PRD. Lee AGENTS.md, contrato REST, HU/CA/DoD, configuración, aplicación, adaptadores y pruebas. No leas ni muestres secretos o archivos .env reales.

Revisa registro, hash adaptativo, login indistinguible, claims y tipos de JWT, secretos separados, expiración, refresh, rotación, revocación, logout, cookie HttpOnly, CORS, CSRF/Origin guard, roles, ownership, validación y logging. Busca bypasses, filtraciones y diferencias temporales observables.

Para el contrato actual valida /api/v1/auth/register, login, refresh y logout; no asumas recuperación, /me ni recursos de citas. Verifica que las pruebas ejerciten el control concreto y no solo fallen por una causa diferente.

Devuelve: hallazgos P0-P3, controles que cumplen, evidencia concreta, pruebas faltantes y corrección mínima. No registres tokens, passwords ni PII en el informe.
```

## Límites

- Repositorio: `citas-api`.
- Edición predeterminada: no; requiere autorización.
- Ownership se evalúa solo para recursos realmente implementados.
