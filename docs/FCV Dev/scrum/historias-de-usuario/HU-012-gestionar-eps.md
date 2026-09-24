---
id: HU-012
tipo: historia-de-usuario
titulo: "Gestionar EPS"
estado: Pendiente de aprobación
epica: "[[EP-003-administracion-de-catalogos-y-profesionales]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 3"
dependencias: ["[[HU-006-iniciar-sesion]]", "[[HU-002-modelar-persistencia-3fn]]", "[[HU-004-definir-contrato-rest-inicial]]"]
relacionadas: ["[[HU-013-gestionar-planes-eps]]", "[[HU-011-gestionar-afiliacion]]"]
---
# HU-012 — Gestionar EPS
## Historia de usuario
**COMO** ADMIN  
**QUIERO** crear, consultar, actualizar y activar/desactivar EPS  
**PARA** mantener el catálogo disponible para afiliaciones.
## Contexto y descripción
Es catálogo configurable; no se borra físicamente si está referenciado.
## Alcance
- CRUD lógico de EPS con autorización ADMIN y consulta de catálogo aplicable.
## Fuera de alcance
- Borrado físico de EPS referenciada o integración con EPS real.
## Reglas de negocio
- Referencias transaccionales se preservan mediante activación/desactivación.
## Dependencias y relaciones
- Épica: [[EP-003-administracion-de-catalogos-y-profesionales]]
- Dependencias: [[HU-006-iniciar-sesion]], [[HU-002-modelar-persistencia-3fn]], [[HU-004-definir-contrato-rest-inicial]].
- Relacionadas: [[HU-013-gestionar-planes-eps]], [[HU-011-gestionar-afiliacion]].
## Esfuerzo
**Nivel:** Medio. **Justificación de dificultad:** combina CRUD, referencias, seguridad y contrato.
## Tareas de desarrollo
- [ ] **T-01 — Modelar operaciones ADMIN.** Dificultad: Medio. Definir validaciones y contrato.
- [ ] **T-02 — Aplicar baja lógica.** Dificultad: Medio. Impedir borrado físico cuando hay referencias.
- [ ] **T-03 — Integrar UI y pruebas.** Dificultad: Medio. Probar ADMIN/no ADMIN y catálogo activo.
## Criterios de aceptación
### CA-01 — CRUD autorizado
**Dado** un ADMIN, **cuando** gestiona una EPS válida, **entonces** puede crearla, consultarla o actualizarla según contrato.
### CA-02 — Protección de referencias
**Dado** una EPS referenciada, **cuando** ADMIN intenta retirarla, **entonces** no se borra físicamente y puede desactivarse.
### CA-03 — Restricción de rol
**Dado** un actor distinto de ADMIN, **cuando** intenta gestionar EPS, **entonces** se deniega.
## Definition of Done
- [ ] CA-01 a CA-03 probados por rol y persistencia.
- [ ] Migración/índices aplicables y cliente ADMIN verificados.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 / DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
## Notas y decisiones
- Los campos concretos del catálogo deben aprobarse en el contrato.
