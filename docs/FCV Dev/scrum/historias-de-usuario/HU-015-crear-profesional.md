---
id: HU-015
tipo: historia-de-usuario
titulo: "Crear profesional"
estado: Aprobada
epica: "[[EP-003-administracion-de-catalogos-y-profesionales]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 3"
dependencias: ["[[HU-006-iniciar-sesion]]", "[[HU-002-modelar-persistencia-3fn]]", "[[HU-004-definir-contrato-rest-inicial]]"]
relacionadas: ["[[HU-016-asignar-especialidades-al-profesional]]", "[[HU-017-asignar-sedes-y-estado-del-profesional]]"]
---
# HU-015 — Crear profesional
## Historia de usuario
**COMO** ADMIN  
**QUIERO** crear un usuario PROFESSIONAL con código y matrícula ficticia  
**PARA** habilitarlo posteriormente para prestar disponibilidad.
## Contexto y descripción
Los profesionales son creados por ADMIN y todos los datos del laboratorio son sintéticos.
## Alcance
- Alta de identidad PROFESSIONAL, código profesional y matrícula ficticia con validaciones aprobadas.
## Fuera de alcance
- Auto-registro PROFESSIONAL, matrícula real o asignaciones de especialidad/sede.
## Reglas de negocio
- Solo ADMIN crea PROFESSIONAL; no usar PII ni credenciales reales.
## Dependencias y relaciones
- Épica: [[EP-003-administracion-de-catalogos-y-profesionales]]
- Dependencias: [[HU-006-iniciar-sesion]], [[HU-002-modelar-persistencia-3fn]], [[HU-004-definir-contrato-rest-inicial]].
- Relacionadas: [[HU-016-asignar-especialidades-al-profesional]], [[HU-017-asignar-sedes-y-estado-del-profesional]].
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** combina identidad, privilegios, unicidad y datos sintéticos.
## Tareas de desarrollo
- [ ] **T-01 — Definir contrato de alta ADMIN.** Dificultad: Medio. Documentar campos sin revelar credenciales.
- [ ] **T-02 — Modelar/validar profesional.** Dificultad: Alto. Relacionarlo con usuario y preservar unicidad necesaria.
- [ ] **T-03 — Integrar UI y pruebas.** Dificultad: Medio. Probar ADMIN/no ADMIN y datos sintéticos.
## Criterios de aceptación
### CA-01 — Alta autorizada
**Dado** ADMIN y datos sintéticos válidos, **cuando** crea un profesional, **entonces** se crea una identidad con rol PROFESSIONAL y sus datos profesionales.
### CA-02 — Restricción de rol
**Dado** un actor sin rol ADMIN, **cuando** intenta crear profesional, **entonces** se deniega la acción.
### CA-03 — Integridad de identidad
**Dado** datos que violan unicidad/validación, **cuando** se crea el profesional, **entonces** no se persiste un registro inconsistente.
## Definition of Done
- [ ] CA-01 a CA-03 probados con autorización y persistencia.
- [ ] Migración 3FN aplicable, datos sintéticos y cliente ADMIN verificados.
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
- Los campos de identidad se concretan en contrato, sin contradecir RF-01/RF-07.
