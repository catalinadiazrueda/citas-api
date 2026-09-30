# citas-api

Repositorio backend del proyecto. **No contiene implementación de negocio inicial**.

## Debe ser construido por el estudiante
- Java 21 + Spring Boot 3.5.x + Maven.
- Arquitectura hexagonal.
- MySQL + Flyway.
- Spring Security + JWT access/refresh.
- REST.
- Pruebas.

## Documentación compartida
- `docs/FCV Dev/scrum/`: épicas/HU del proyecto.
- `docs/FCV Dev/llm-wiki/`: única LLM Wiki global del workspace.
- `docs/FCV Dev/subagents/`: catálogo operativo de subagentes del orquestador.
- `automations/n8n/`: JSON exportados en S5/S6.

Lee el PRD en la carpeta raíz antes de continuar Spring Boot.

## Estado actual

El repositorio contiene los incrementos de identidad, catálogos, profesionales,
disponibilidad y reserva inicial. También incluye la migración V3 y los
endpoints de solicitud/restablecimiento de contraseña. Las capacidades de
perfil, mis citas, cancelación, reprogramación, agenda profesional, auditoría
y automatizaciones n8n todavía no deben declararse completadas hasta contar con
sus contratos, pantallas y pruebas correspondientes.

## Incremento de identidad backend

Este incremento implementa HU-005/006/007 por REST. El contrato base está en `docs/FCV Dev/llm-wiki/wiki/contracts.md`; la recuperación usa la migración V3 y requiere cerrar su HU específica antes de considerarse entrega final.

Para desarrollo local, configura las variables de `.env.example` con valores propios fuera de Git y activa el perfil `local`. Los secretos JWT deben ser distintos y tener al menos 32 bytes. El perfil local usa cookie HTTP `SameSite=Lax`; el predeterminado requiere HTTPS y usa `SameSite=None; Secure`.

En Windows con Docker Desktop, ejecuta las pruebas desde este directorio:

```powershell
docker compose -f compose.test.yml run --rm api-test mvn test
```

El contenedor Maven usa Java 21; Testcontainers crea un MySQL 8.4 temporal. No requiere Java/Maven instalados en el host.
