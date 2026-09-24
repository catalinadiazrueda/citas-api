---
id: HU-010
tipo: historia-de-usuario
titulo: "Gestionar perfil"
estado: Pendiente de aprobación
epica: "[[EP-002-identidad-y-perfil-del-usuario]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 2"
dependencias: ["[[HU-006-iniciar-sesion]]", "[[HU-004-definir-contrato-rest-inicial]]"]
relacionadas: ["[[HU-011-gestionar-afiliacion]]"]
---
# HU-010 — Gestionar perfil
## Historia de usuario
**COMO** USER autenticado  
**QUIERO** consultar y actualizar los datos permitidos de mi perfil  
**PARA** mantener mi información de contacto vigente.
## Contexto y descripción
El PRD no enumera cuáles campos de identidad pueden cambiar; esa limitación debe resolverse en contrato sin inventarla.
## Alcance
- Consulta por ownership, edición de campos permitidos aprobados y validación server-side.
## Fuera de alcance
- Cambio de rol, acceso a perfil ajeno, y campos no autorizados por contrato.
## Reglas de negocio
- Ownership obligatorio; unicidad se preserva si los campos editables incluyen email/documento.
## Dependencias y relaciones
- Épica: [[EP-002-identidad-y-perfil-del-usuario]]
- Dependencias: [[HU-006-iniciar-sesion]], [[HU-004-definir-contrato-rest-inicial]].
- Relacionadas: [[HU-011-gestionar-afiliacion]].
## Esfuerzo
**Nivel:** Medio. **Justificación de dificultad:** combina ownership, validación y ambigüedad de campos permitidos.
## Tareas de desarrollo
- [ ] **T-01 — Acordar campos editables.** Dificultad: Medio. Documentar el límite sin ampliar el PRD.
- [ ] **T-02 — Implementar consulta/actualización propia.** Dificultad: Medio. Aplicar ownership, validación y unicidad.
- [ ] **T-03 — Integrar pantalla y pruebas.** Dificultad: Medio. Mostrar datos y errores de validación autorizados.
## Criterios de aceptación
### CA-01 — Consulta propia
**Dado** un USER autenticado, **cuando** consulta su perfil, **entonces** recibe únicamente sus datos permitidos.
### CA-02 — Actualización válida
**Dado** cambios permitidos y válidos, **cuando** los guarda, **entonces** quedan disponibles al volver a consultar el perfil.
### CA-03 — Protección de datos
**Dado** una solicitud para otro usuario o datos inválidos/duplicados, **cuando** se procesa, **entonces** se deniega o valida sin modificar información no permitida.
## Definition of Done
- [ ] CA-01 a CA-03 validados con pruebas de ownership, validación y cliente aplicable.
- [ ] El contrato enumera los campos permitidos antes de completar la HU.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 / DoD | Pendiente | — | Requiere acuerdo de campos. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
## Notas y decisiones
- Pregunta abierta: campos exactos editables.
