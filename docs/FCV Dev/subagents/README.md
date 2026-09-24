# Catálogo de subagentes del workspace `citas`

Este directorio contiene las instrucciones operativas de los subagentes que el orquestador puede delegar. Los archivos no sustituyen las HU, el PRD, los `AGENTS.md` ni la LLM Wiki: cada delegación debe incluir esas fuentes y un alcance concreto.

## Contexto vigente

- `citas-api`: Spring Boot 3.5/Java 21 en `develop`; HU-005/006/007 implementadas para registro, login, refresh y logout JWT.
- `citas-web`: React 19 + TypeScript + Vite importado en `develop`; existe trabajo local de integración de autenticación pendiente de validación y commit.
- Contrato aprobado: solo `/api/v1/auth` para HU-005/006/007. No inferir contratos para perfil, recuperación o citas.

## Catálogo

| Subagente | Archivo | Uso principal | Puede editar |
|---|---|---|---|
| Backend Domain/Hexagonal | [backend/01-domain-hexagonal.md](backend/01-domain-hexagonal.md) | Invariantes, casos de uso, puertos y dependencias | Solo con autorización explícita |
| Backend Persistence/MySQL | [backend/02-persistence-mysql.md](backend/02-persistence-mysql.md) | JPA, Flyway, 3FN, constraints, índices y concurrencia | Solo con autorización explícita |
| Backend Security/JWT | [backend/03-security-jwt.md](backend/03-security-jwt.md) | Autenticación, autorización y ciclo JWT | Solo con autorización explícita |
| Backend Verifier | [backend/04-backend-verifier.md](backend/04-backend-verifier.md) | Verificación independiente de CA/DoD | No |
| Frontend Design Reconciler | [frontend/01-design-reconciler.md](frontend/01-design-reconciler.md) | Fidelidad al diseño aprobado y responsive | Solo con autorización explícita |
| Frontend API Integration | [frontend/02-api-integration.md](frontend/02-api-integration.md) | REST, DTO, sesión, errores y environment | Solo con autorización explícita |
| Frontend State & Forms | [frontend/03-state-forms.md](frontend/03-state-forms.md) | Formularios, estados UI, roles y accesibilidad | Solo con autorización explícita |
| Frontend Verifier | [frontend/04-frontend-verifier.md](frontend/04-frontend-verifier.md) | Verificación independiente de CA/DoD | No |

## Protocolo de delegación

1. Confirmar la HU, CA y DoD aplicables en `../scrum/`.
2. Elegir el subagente más específico; no delegar el mismo archivo a dos agentes con permiso de edición.
3. Entregar un paquete con objetivo, repositorio, archivos permitidos, contrato afectado, evidencia esperada y autoridad de edición.
4. Ejecutar especialistas en paralelo solo cuando sus lecturas o ediciones no se solapen.
5. Ejecutar el verificador correspondiente después de la implementación; nunca pedirle que corrija lo que verifica.
6. Consolidar hallazgos sin convertir recomendaciones en requisitos no aprobados.
7. Si cambia un contrato REST, producir primero el plan cross-repo exigido por `AGENTS.md`.

## Formato mínimo del encargo

```text
Objetivo:
HU / CA / DoD:
Repositorio y rama:
Archivos o carpetas permitidos:
Contrato o diseño de referencia:
Autoridad: solo lectura | puede editar
Pruebas/evidencia esperada:
Fuera de alcance:
```
