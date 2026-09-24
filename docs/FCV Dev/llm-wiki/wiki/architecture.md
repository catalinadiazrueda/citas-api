# Arquitectura y límites

## HECHO

El backend debe usar Java 21, Spring Boot 3.5.x, Maven, arquitectura hexagonal, JPA, MySQL 8.4, Flyway, Spring Security/JWT y REST JSON. El frontend usa Node 24, TypeScript y React o Angular, sin Express/BFF.

## PREFERENCIA

Mantener especificaciones, contratos y evidencia cross-repo en la Wiki del backend, sin convertir la raíz en un tercer repositorio.

## DECISIÓN — 2026-09-22

La documentación operativa del proyecto se organiza bajo `citas-api/docs/FCV Dev/`: `scrum/`, `llm-wiki/` y `subagents/`. El orquestador consume el catálogo de subagentes desde esa ubicación.
