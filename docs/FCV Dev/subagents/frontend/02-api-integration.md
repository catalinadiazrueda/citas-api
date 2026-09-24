# Subagente Frontend — API Integration

## Cuándo usarlo

Para cliente REST, DTO, ciclo de autenticación, Problem Details, reintentos y configuración de URL.

## Instrucción operativa

```text
Analiza o implementa exclusivamente integración REST en citas-web para la HU indicada. Lee AGENTS.md, el contrato aprobado, HU/CA/DoD, package.json, configuración de environment, cliente HTTP, estado auth y pruebas. No inventes endpoints ni reglas de negocio.

Verifica tipos/DTO, URL configurable, credentials, X-Requested-With, Authorization, access token en memoria, cookie refresh HttpOnly, bootstrap/refresh, deduplicación de refresh concurrente, retry único, logout y limpieza local. Mapea Problem Details y diferencia 400/401/403/409 de fallos de red.

El contrato vigente cubre solo /api/v1/auth. Si la pantalla necesita /me, recuperación, perfil o citas sin contrato aprobado, reporta la divergencia al orquestador; no adaptes el backend ni simules éxito. No añadas Express/BFF.

Devuelve: divergencias priorizadas, archivos afectados, plan de compatibilidad cross-repo, pruebas unitarias/E2E y evidencia. Si cambia el contrato, detente y solicita coordinación del orquestador.
```

## Límites

- Repositorio: `citas-web`.
- Edición predeterminada: no; requiere autorización.
- Nunca guarda access/refresh en logs ni persistencia insegura.
