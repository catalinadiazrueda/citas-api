---
id: HU-029
tipo: historia-de-usuario
titulo: "Consultar agenda profesional"
estado: Pendiente de aprobación
epica: "[[EP-007-operacion-profesional-y-administrativa]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 6"
dependencias: ["[[HU-022-reservar-cita-general]]", "[[HU-024-resolver-solicitud-especializada]]"]
relacionadas: ["[[HU-030-cerrar-atencion]]"]
---
# HU-029 — Consultar agenda profesional
## Historia de usuario
**COMO** PROFESSIONAL  
**QUIERO** consultar mis citas `APPROVED` por día/semana y sede  
**PARA** organizar mi atención sin ver citas ajenas.
## Contexto y descripción
Es distinta del calendario de bloques de disponibilidad.
## Alcance
- Vista propia de citas aprobadas con filtros día/semana/sede y datos mínimos autorizados.
## Fuera de alcance
- Citas no aprobadas, agenda de otro profesional o historia clínica.
## Reglas de negocio
- Solo `APPROVED` de profesional autenticado; no revelar datos de usuarios fuera de sus citas.
## Dependencias y relaciones
- Épica: [[EP-007-operacion-profesional-y-administrativa]]
- Dependencias: [[HU-022-reservar-cita-general]], [[HU-024-resolver-solicitud-especializada]].
- Relacionadas: [[HU-030-cerrar-atencion]].
## Esfuerzo
**Nivel:** Medio. **Justificación de dificultad:** aplica filtros temporales, rol y restricción de PII.
## Tareas de desarrollo
- [ ] **T-01 — Definir representación mínima.** Dificultad: Medio. Acordar campos necesarios para atención.
- [ ] **T-02 — Aplicar consulta por ownership.** Dificultad: Medio. Filtrar estado, periodo y sede.
- [ ] **T-03 — Entregar agenda/pruebas.** Dificultad: Medio. Probar aislamiento y filtros.
## Criterios de aceptación
### CA-01 — Agenda aprobada propia
**Dado** citas aprobadas asignadas, **cuando** PROFESSIONAL consulta, **entonces** ve solo las propias en estado `APPROVED`.
### CA-02 — Filtros operativos
**Dado** citas en distintas fechas/sedes, **cuando** filtra por día, semana o sede, **entonces** se muestran solo coincidencias.
### CA-03 — Privacidad
**Dado** otro profesional o cita ajena, **cuando** se consulta, **entonces** sus datos no se exponen.
## Definition of Done
- [ ] CA-01 a CA-03 probados con rol/ownership, REST y cliente aplicable.
- [ ] Campos mínimos y trazabilidad Scrum verificados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 / DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
## Notas y decisiones
- Los campos visibles no amplían el PRD ni contienen historia clínica.
