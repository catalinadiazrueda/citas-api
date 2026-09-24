# Riesgos y preguntas abiertas

- Ambos repositorios trabajan en `develop`; `citas-web` contiene cambios locales aún no versionados que deben preservarse y verificarse.
- No está definido el conjunto completo de estados y transiciones de citas.
- Falta estrategia de exclusión concurrente de slots.
- Falta política de zona horaria y formato temporal.
- Es ambiguo si reservas `REQUESTED` o reprogramaciones `PENDING` bloquean la edición de bloques.
- Falta lista completa de catálogos fijos y semillas.
- Falta política de afiliación activa/histórica.
- Los refresh tokens de HU-007 ya tienen rotación, expiración, revocación y almacenamiento definidos; queda pendiente la política futura para gestión multidispositivo.
- El contrato REST de HU-005/006/007 está aprobado; faltan los contratos de las demás HU.
- React 19 + TypeScript + Vite es el framework frontend detectado; falta evidencia de aprobación visual y cierre cross-repo de HU-033.
- n8n necesita contrato de eventos, idempotencia, reintentos y autenticación de webhook.
- La Skill `scrum-spec-orchestrator` conserva una allowlist interna para `docs/wiki/scrum/`; debe actualizarse explícitamente antes de usarla con `docs/FCV Dev/scrum/`.
