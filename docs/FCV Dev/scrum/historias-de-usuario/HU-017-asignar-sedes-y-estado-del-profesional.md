---
id: HU-017
tipo: historia-de-usuario
titulo: "Asignar sedes y estado del profesional"
estado: Aprobada
epica: "[[EP-003-administracion-de-catalogos-y-profesionales]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 3"
dependencias: ["[[HU-015-crear-profesional]]", "[[HU-003-publicar-catalogos-fijos]]"]
relacionadas: ["[[HU-018-crear-bloques-de-disponibilidad]]"]
---
# HU-017 — Asignar sedes y estado del profesional
## Historia de usuario
**COMO** ADMIN  
**QUIERO** asignar una o ambas sedes y activar/desactivar a PROFESSIONAL  
**PARA** controlar dónde y cuándo puede publicar disponibilidad.
## Contexto y descripción
Las dos sedes son catálogo fijo. La habilitación es requisito de agenda.
## Alcance
- Asociar sedes fijas y cambiar estado de profesional desde rol ADMIN.
## Fuera de alcance
- Crear sedes, asignar más de las fijas o publicar bloques como profesional inhabilitado.
## Reglas de negocio
- El profesional trabaja en una o ambas sedes; solo publica bloques en sedes asignadas.
## Dependencias y relaciones
- Épica: [[EP-003-administracion-de-catalogos-y-profesionales]]
- Dependencias: [[HU-015-crear-profesional]], [[HU-003-publicar-catalogos-fijos]].
- Relacionadas: [[HU-018-crear-bloques-de-disponibilidad]].
## Esfuerzo
**Nivel:** Medio. **Justificación de dificultad:** combina N:M de sedes, estado y reglas de agenda.
## Tareas de desarrollo
- [ ] **T-01 — Modelar asignación/estado.** Dificultad: Medio. Usar catálogo fijo y relación normalizada.
- [ ] **T-02 — Aplicar gestión ADMIN.** Dificultad: Medio. Validar que solo se asigne sede fija.
- [ ] **T-03 — Integrar/verificar agenda.** Dificultad: Medio. Probar inhabilitado o sede no asignada.
## Criterios de aceptación
### CA-01 — Sedes permitidas
**Dado** ADMIN y un profesional, **cuando** asigna sedes, **entonces** puede asociar HIC, ICV o ambas, sin valores externos.
### CA-02 — Estado administrable
**Dado** un profesional, **cuando** ADMIN lo activa o desactiva, **entonces** el estado queda aplicado para las reglas de disponibilidad.
### CA-03 — Publicación restringida
**Dado** un profesional inactivo o una sede no asignada, **cuando** intenta crear disponibilidad, **entonces** la aplicación lo impide.
## Definition of Done
- [ ] CA-01 a CA-03 probados por rol, relación y regla de agenda.
- [ ] Persistencia/migración aplicable y cliente ADMIN verificados.
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
- No se define el efecto retroactivo sobre citas existentes al desactivar; debe mantener integridad PRD.
