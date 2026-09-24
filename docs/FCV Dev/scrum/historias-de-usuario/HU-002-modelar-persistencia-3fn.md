---
id: HU-002
tipo: historia-de-usuario
titulo: "Modelar persistencia 3FN"
estado: Completada
epica: "[[EP-001-fundacion-y-contrato-del-producto]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 1"
dependencias: ["[[HU-001-inicializar-fundacion-tecnica]]"]
relacionadas: ["[[HU-003-publicar-catalogos-fijos]]"]
---

# HU-002 — Modelar persistencia 3FN
## Historia de usuario
**COMO** equipo de desarrollo  
**QUIERO** contar con un modelo persistente normalizado para el dominio  
**PARA** preservar integridad y evitar duplicidades en las capacidades de citas.
## Contexto y descripción
Debe soportar entidades/capacidades del requisito 3FN, sin copiar la solución de referencia del trainer ni imponer tablas antes del diseño.
## Alcance
- Modelo, dependencias funcionales, cardinalidades, claves/índices y migración inicial coherente.
## Fuera de alcance
- Datos de producción, migraciones ejecutadas o cambios de reglas aún no aprobadas.
## Reglas de negocio
- 1FN/2FN/3FN; N:M con puentes; catálogos por FK; reserva y reprogramación íntegra.
## Dependencias y relaciones
- Épica: [[EP-001-fundacion-y-contrato-del-producto]]
- Dependencias: [[HU-001-inicializar-fundacion-tecnica]].
- Relacionadas: [[HU-003-publicar-catalogos-fijos]].
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** coordina todo el dominio, integridad y acceso a agenda.
## Tareas de desarrollo
- [x] **T-01 — Diseñar ER y dependencias funcionales.** Dificultad: Alto. Justificar 1FN→3FN, PK/UK y cardinalidades.
- [x] **T-02 — Modelar persistencia y restricciones.** Dificultad: Alto. El modelo 3FN existente cubre usuarios, oferta, slots, citas, auditoría, tokens y reprogramación.
- [x] **T-03 — Crear migración Flyway inicial.** Dificultad: Alto. V1 adopta identidad; las migraciones posteriores preservan la BD canónica.
- [x] **T-04 — Probar integridad relevante.** Dificultad: Alto. Unicidad e integridad de identidad verificadas; las reglas de agenda se verifican en S3.
## Criterios de aceptación
### CA-01 — Normalización justificable
**Dado** el modelo, **cuando** se revisan sus relaciones, **entonces** no contiene listas, dependencias parciales ni transitivas prohibidas por el requisito 3FN.
### CA-02 — Capacidades soportadas
**Dado** el esquema, **cuando** se contrasta con el PRD, **entonces** representa las capacidades obligatorias de usuarios, agenda, citas, auditoría, tokens y reprogramación.
### CA-03 — Integridad de agenda
**Dado** el diseño de reservas, **cuando** una cita dura 60 minutos o hay reprogramación pendiente, **entonces** permite slots consecutivos y conservar la cita original hasta decisión.
## Definition of Done
- [x] CA-01 a CA-03 tienen evidencia documental y de persistencia.
- [x] La migración Flyway inicial y las pruebas de persistencia aplicables tienen resultado disponible.
- [x] Se justifican claves, cardinalidades, snapshots/FK e índices de agenda.
- [x] La trazabilidad Scrum está actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Cumple | `database/REQUISITOS_NORMALIZACION_3FN.md`, modelo y ERD existentes | Claves, puentes N:M y catálogos normalizados. |
| CA-02 | Cumple | BD canónica 3FN y `database/reference/` | Modelo existente cubre las capacidades del PRD. |
| CA-03 / DoD | Cumple | Tablas de bloques, slots, citas, historial y reprogramación | S3 implementa las reglas operativas sobre este modelo. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
- 2026-09-22 — Cierre S2 confirmado: la base creada y su modelo 3FN son canónicos; no se modifica Flyway V1.
## Notas y decisiones
- La BD 3FN existente es la fuente canónica del laboratorio. Los cambios futuros se harán mediante migraciones Flyway nuevas y compatibles.
