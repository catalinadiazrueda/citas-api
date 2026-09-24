---
id: HU-025
tipo: historia-de-usuario
titulo: "Consultar mis citas"
estado: Pendiente de aprobación
epica: "[[EP-006-ciclo-de-vida-de-citas-y-reprogramaciones]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 5"
dependencias: ["[[HU-022-reservar-cita-general]]", "[[HU-023-solicitar-cita-especializada]]"]
relacionadas: ["[[HU-026-cancelar-cita]]", "[[HU-027-solicitar-reprogramacion]]"]
---
# HU-025 — Consultar mis citas
## Historia de usuario
**COMO** USER  
**QUIERO** consultar y filtrar mis citas por estado y fecha  
**PARA** conocer su detalle y las acciones disponibles.
## Contexto y descripción
Debe mostrar sede, profesional, especialidad, fecha/hora, duración, estado y motivo de rechazo cuando exista.
## Alcance
- Listado/detalle de citas propias y filtros de estado/fecha.
## Fuera de alcance
- Citas de otro USER, información clínica o acciones de ADMIN.
## Reglas de negocio
- Ownership; el motivo de rechazo solo se visualiza si existe.
## Dependencias y relaciones
- Épica: [[EP-006-ciclo-de-vida-de-citas-y-reprogramaciones]]
- Dependencias: [[HU-022-reservar-cita-general]], [[HU-023-solicitar-cita-especializada]].
- Relacionadas: [[HU-026-cancelar-cita]], [[HU-027-solicitar-reprogramacion]].
## Esfuerzo
**Nivel:** Medio. **Justificación de dificultad:** combina filtros, ownership y representación de estados/auditoría.
## Tareas de desarrollo
- [ ] **T-01 — Definir consulta/detalle.** Dificultad: Medio. Acordar filtros y campos obligatorios.
- [ ] **T-02 — Aplicar ownership y composición.** Dificultad: Medio. Obtener relaciones sin exponer citas ajenas.
- [ ] **T-03 — Entregar pantalla/pruebas.** Dificultad: Medio. Cubrir filtros, rechazo y aislamiento.
## Criterios de aceptación
### CA-01 — Datos mínimos
**Dado** citas propias, **cuando** USER las consulta, **entonces** ve sede, profesional, especialidad, fecha/hora, duración y estado.
### CA-02 — Filtros y motivo
**Dado** varias citas, **cuando** filtra por estado/fecha, **entonces** obtiene coincidencias y ve motivo de rechazo cuando exista.
### CA-03 — Ownership
**Dado** un USER, **cuando** intenta consultar detalle de cita ajena, **entonces** no recibe sus datos.
## Definition of Done
- [ ] CA-01 a CA-03 probados en REST/ownership y cliente aplicable.
- [ ] Contrato no expone información fuera del PRD; trazabilidad actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 / DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
## Notas y decisiones
- Las pantallas se incorporan al cliente sin prescribir framework.
