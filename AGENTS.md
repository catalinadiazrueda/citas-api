# `citas-api` — instrucciones del agente backend

## Estado comprobado del repositorio

Al 2026-09-22 este repositorio contiene el incremento de identidad de HU-005/006/007 en `develop`, con Spring Boot, Flyway y pruebas. No asumir contratos adicionales, endpoints, DTOs, tablas o convenciones hasta que existan o estén aprobados.

La fuente funcional es `../PRD.md`; las restricciones de arquitectura y plataforma están en `../RESTRICCIONES_TECNICAS.md`. Antes de una tarea, consultar el índice de la Wiki global en `docs/FCV Dev/llm-wiki/wiki/index.md`, pero no crear ni mantener una Wiki local desde este agente.

## Responsabilidad exclusiva

Este repositorio contiene el backend Java 21 con Spring Boot 3.5.x, Maven, REST/JSON, Spring Security/JWT access-refresh, MySQL 8.4, Spring Data JPA, Flyway, reglas de negocio, contratos backend y pruebas. No editar `../citas-web`.

Los workflows n8n se versionan exclusivamente como JSON en `automations/n8n/`; no incluir credenciales en ellos.

## Arquitectura obligatoria

- El dominio no depende de Spring, JPA, HTTP ni de detalles de infraestructura.
- Los casos de uso y la coordinación de reglas viven en aplicación.
- Los puertos expresan dependencias entre aplicación y el exterior.
- REST y persistencia son adaptadores; los controladores traducen HTTP y no concentran negocio.
- No acoplar el backend a React o Angular.

El diseño concreto de paquetes debe seguir esta separación una vez creado el proyecto, sin inventar una estructura antes de inspeccionar el código existente.

## Flujo por historia de usuario

1. Localizar la HU aprobada y su DoD en `docs/FCV Dev/scrum/`. Si no existen, detener la implementación y solicitar o producir la especificación mediante el flujo autorizado.
2. Identificar RF/RN del PRD, reglas de autorización/ownership, datos, puertos, adaptadores y contrato REST afectados.
3. Antes de editar, presentar un plan con los archivos backend, migraciones, contrato y pruebas que cambiarán.
4. Implementar el mínimo coherente y mantener las dependencias dirigidas hacia el dominio/aplicación.
5. Ejecutar las pruebas relevantes: dominio, aplicación y, cuando aplique, integración REST/persistencia.
6. Verificar arquitectura y DoD; informar evidencia ejecutada y lo no verificado.

## Datos, migraciones y seguridad

- Todo cambio de esquema requiere migración Flyway nueva, justificación de 3FN, cardinalidades e índices relevantes; no editar una migración ya aplicada.
- Cargar por seed los catálogos fijos que el PRD exige, una vez sus valores estén definidos/aprobados.
- Preservar las reglas de reserva, slots consecutivos, transiciones explícitas y auditoría del PRD.
- Secretos solo por variables de entorno; `.env.example` nunca contiene valores reales.
- Hash adaptativo para contraseñas; no registrar contraseñas, JWT, refresh tokens ni tokens de recuperación.
- Aplicar validación server-side, CORS explícito, roles y ownership.
- Usar únicamente datos sintéticos del laboratorio.

## Contratos y coordinación

- El contrato REST de HU-005/006/007 está aprobado bajo `/api/v1/auth`; los demás contratos siguen pendientes y no se infieren desde pantallas.
- Todo cambio contractual requiere coordinación con el orquestador y evidencia en backend y frontend antes de declararlo completado.
- Registrar decisiones y contratos compartidos en la Wiki global mediante el orquestador, no desde este agente.

## Git

`main` es estable y `develop` es la rama de trabajo definida por el workspace. Actualmente solo existe `main`; no crear ni cambiar ramas como efecto incidental de una tarea de documentación. Preservar cambios no relacionados y no reescribir historial.
