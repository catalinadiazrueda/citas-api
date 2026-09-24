---
id: HU-020
tipo: historia-de-usuario
titulo: "Consultar calendario de disponibilidad"
estado: Aprobada
epica: "[[EP-004-disponibilidad-del-profesional]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 4"
dependencias: ["[[HU-018-crear-bloques-de-disponibilidad]]"]
relacionadas: ["[[HU-029-consultar-agenda-profesional]]"]
---
# HU-020 — Consultar calendario de disponibilidad
## Historia de usuario
**COMO** PROFESSIONAL  
**QUIERO** consultar mi calendario de bloques  
**PARA** conocer la disponibilidad que publiqué.
## Contexto y descripción
La agenda de disponibilidad no sustituye la agenda visible de citas aprobadas.
## Alcance
- Consulta del calendario/bloques propios con sede y fecha.
## Fuera de alcance
- Datos de citas de otros profesionales o modificación desde esta consulta.
## Reglas de negocio
- Solo ownership; mostrar bloques/sedes sin exponer información de USER.
## Dependencias y relaciones
- Épica: [[EP-004-disponibilidad-del-profesional]]
- Dependencias: [[HU-018-crear-bloques-de-disponibilidad]].
- Relacionadas: [[HU-029-consultar-agenda-profesional]].
## Esfuerzo
**Nivel:** Medio. **Justificación de dificultad:** requiere filtros y aislamiento de datos en una vista de calendario.
## Tareas de desarrollo
- [ ] **T-01 — Definir consulta/filtros.** Dificultad: Medio. Acordar fecha/sede y representación sin imponer UI.
- [ ] **T-02 — Aplicar ownership.** Dificultad: Medio. Restringir al calendario del autenticado.
- [ ] **T-03 — Entregar calendario y pruebas.** Dificultad: Medio. Probar filtro y ausencia de agenda ajena.
## Criterios de aceptación
### CA-01 — Visualización propia
**Dado** bloques propios publicados, **cuando** PROFESSIONAL consulta su calendario, **entonces** ve fecha, franja y sede de sus bloques.
### CA-02 — Filtro aplicable
**Dado** bloques en diversas fechas/sedes, **cuando** aplica los filtros del contrato, **entonces** recibe solo los bloques coincidentes.
### CA-03 — Aislamiento
**Dado** otro profesional, **cuando** intenta consultar calendario ajeno, **entonces** no obtiene esos bloques.
## Definition of Done
- [ ] CA-01 a CA-03 probados en autorización/REST y cliente aplicable.
- [ ] No se exponen datos de USER ni información ajena; trazabilidad actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 / DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
## Notas y decisiones
- El formato visual queda bajo el diseño aprobado.
