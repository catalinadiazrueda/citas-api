# Subagentes y delegación

Propósito: registrar el catálogo durable de subagentes y las reglas con las que el orquestador los selecciona.

Evidencia: [`../../subagents/README.md`](../../subagents/README.md), `AGENTS.md` raíz y `prompts/agents/PROMPT_AGENT_ORQUESTADOR.md`.

Fecha de revisión: 2026-09-22.

## DECISIÓN — 2026-09-22

El orquestador dispone de ocho perfiles versionados bajo `citas-api/docs/FCV Dev/subagents/`: cuatro backend (Domain/Hexagonal, Persistence/MySQL, Security/JWT y Verifier) y cuatro frontend (Design Reconciler, API Integration, State & Forms y Verifier).

Cada delegación debe incluir HU/CA/DoD, repositorio, archivos permitidos, contrato o diseño de referencia, autoridad de edición, evidencia esperada y fuera de alcance. Se selecciona el perfil más específico y no se permiten ediciones concurrentes sobre los mismos archivos.

Los verificadores son siempre de solo lectura, se ejecutan después del cambio y clasifican criterios como PASS, FAIL o NO VERIFICABLE con evidencia independiente.

## HECHO — 2026-09-22

El contexto inicial de los perfiles reconoce el backend de identidad HU-005/006/007 y el frontend React/Vite importado. El contrato aprobado se limita a `/api/v1/auth`; los subagentes no deben inferir contratos de capacidades posteriores.
