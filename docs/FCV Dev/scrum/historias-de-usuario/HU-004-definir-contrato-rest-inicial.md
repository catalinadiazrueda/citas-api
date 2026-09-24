---
id: HU-004
tipo: historia-de-usuario
titulo: "Definir contrato REST inicial"
estado: En desarrollo
epica: "[[EP-001-fundacion-y-contrato-del-producto]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 1"
dependencias: ["[[HU-001-inicializar-fundacion-tecnica]]"]
relacionadas: ["[[HU-033-integrar-cliente-web-con-api]]"]
---

# HU-004 — Definir contrato REST inicial
## Historia de usuario
**COMO** equipo de producto  
**QUIERO** acordar y documentar el contrato REST de las capacidades aprobadas  
**PARA** que cliente y backend evolucionen sin BFF ni supuestos incompatibles.
## Contexto y descripción
El PRD exige REST/JSON directo, pero no fija rutas, formatos ni códigos. Estos deben diseñarse antes de que una HU consumidora se complete.
## Alcance
- Convenciones de recursos, autenticación, errores, autorización, versionado/compatibilidad y contrato de HU aprobadas.
## Fuera de alcance
- Inventar endpoints para HU no aprobadas o implementar adaptadores HTTP.
## Reglas de negocio
- REST JSON, CORS explícito, JWT access/refresh y contrato coherente con rol/ownership.
## Dependencias y relaciones
- Épica: [[EP-001-fundacion-y-contrato-del-producto]]
- Dependencias: [[HU-001-inicializar-fundacion-tecnica]].
- Relacionadas: [[HU-033-integrar-cliente-web-con-api]].
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** es un acuerdo transversal entre dos repositorios y múltiples roles.
## Tareas de desarrollo
- [ ] **T-01 — Proponer contrato por capacidad.** Dificultad: Alto. Definir recursos/representaciones solo para HU aprobadas.
- [ ] **T-02 — Documentar seguridad y errores.** Dificultad: Alto. Precisar autenticación, autorización, validación y respuestas sin filtrar secretos.
- [ ] **T-03 — Revisar compatibilidad cross-repo.** Dificultad: Alto. Enumerar repositorios, archivos, migración y pruebas antes de cambiar contratos.
## Criterios de aceptación
### CA-01 — Contrato sin ambigüedad operativa
**Dado** una HU aprobada, **cuando** frontend y backend revisan su contrato, **entonces** conocen representación, validaciones, respuestas y reglas de acceso necesarias sin deducirlas de pantallas.
### CA-02 — Consumo directo
**Dado** el contrato, **cuando** el cliente lo integra, **entonces** consume `citas-api` por REST/JSON directo y no requiere Express/BFF.
### CA-03 — Cambio trazable
**Dado** un cambio futuro de contrato, **cuando** se planifica, **entonces** identifica ambos repositorios, compatibilidad, migración y pruebas.
## Definition of Done
- [ ] CA-01 a CA-03 tienen documentación revisable y evidencia de compatibilidad cuando exista implementación.
- [ ] No se fijan rutas/formats de HU no aprobadas.
- [ ] La trazabilidad Scrum y wikilinks cross-repo están actualizados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 / DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
## Notas y decisiones
- No hay contrato final aprobado a la fecha.
- 2026-09-17: aprobado el contrato inicial de HU-005/006/007 documentado en `../../llm-wiki/wiki/contracts.md`. El resto de capacidades se añadirá cuando sus HU se aprueben. El impacto cross-repo está enumerado en esa página; no se edita `citas-web` en este incremento.
