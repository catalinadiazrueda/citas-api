---
id: HU-018
tipo: historia-de-usuario
titulo: "Crear bloques de disponibilidad"
estado: Aprobada
epica: "[[EP-004-disponibilidad-del-profesional]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 4"
dependencias: ["[[HU-016-asignar-especialidades-al-profesional]]", "[[HU-017-asignar-sedes-y-estado-del-profesional]]"]
relacionadas: ["[[HU-019-modificar-bloques-futuros]]", "[[HU-021-buscar-disponibilidad]]"]
---
# HU-018 — Crear bloques de disponibilidad
## Historia de usuario
**COMO** PROFESSIONAL  
**QUIERO** crear bloques futuros por día y sede  
**PARA** publicar horarios reservables de mi agenda.
## Contexto y descripción
Puede crear múltiples bloques (por ejemplo mañana/tarde); cada uno se discretiza en slots de 30 min.
## Alcance
- Crear bloque futuro con inicio/fin/sede y generar disponibilidad discreta según diseño aprobado.
## Fuera de alcance
- Bloques pasados, solapados o en sedes no asignadas.
## Reglas de negocio
- Sin pasado/solapamiento; profesional activo y habilitado en sede; slots de 30 min.
## Dependencias y relaciones
- Épica: [[EP-004-disponibilidad-del-profesional]]
- Dependencias: [[HU-016-asignar-especialidades-al-profesional]], [[HU-017-asignar-sedes-y-estado-del-profesional]].
- Relacionadas: [[HU-019-modificar-bloques-futuros]], [[HU-021-buscar-disponibilidad]].
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** exige validación temporal, autorización, sede y base para concurrencia de reservas.
## Tareas de desarrollo
- [ ] **T-01 — Modelar bloque y slots.** Dificultad: Alto. Preservar 3FN e índices de agenda.
- [ ] **T-02 — Validar publicación.** Dificultad: Alto. Aplicar future-only, no solapamiento y sede/estado.
- [ ] **T-03 — Entregar calendario/formulario y pruebas.** Dificultad: Alto. Cubrir casos válidos e inválidos.
## Criterios de aceptación
### CA-01 — Bloque futuro válido
**Dado** PROFESSIONAL activo asignado a una sede, **cuando** crea un bloque futuro válido, **entonces** queda disponible en slots de 30 minutos.
### CA-02 — Restricciones de bloque
**Dado** un bloque pasado, solapado o de sede no asignada, **cuando** intenta crearlo, **entonces** se rechaza sin publicar disponibilidad.
### CA-03 — Múltiples franjas
**Dado** un día sin conflicto, **cuando** crea dos franjas separadas, **entonces** ambas quedan disponibles sin incluir el intervalo intermedio.
## Definition of Done
- [ ] CA-01 a CA-03 tienen pruebas de dominio/aplicación/REST y cliente aplicable.
- [ ] Migración/índices de agenda aplicables y ownership de PROFESSIONAL verificados.
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
- La representación interna de slots se decide en [[HU-002-modelar-persistencia-3fn]].
