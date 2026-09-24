---
id: HU-011
tipo: historia-de-usuario
titulo: "Gestionar afiliación"
estado: Aprobada
epica: "[[EP-002-identidad-y-perfil-del-usuario]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 2"
dependencias: ["[[HU-003-publicar-catalogos-fijos]]"]
relacionadas: []
---
# HU-011 — Gestionar afiliación
## Historia de usuario
**COMO** visitante que se registra  
**QUIERO** seleccionar opcionalmente un plan activo  
**PARA** guardar una afiliación normalizada como dato administrativo de mi cuenta.
## Contexto y descripción
EPS y planes son configurables; régimen es catálogo fijo.
## Alcance
- Crear una afiliación inicial opcional desde el registro usando un plan activo.
## Fuera de alcance
- CRUD ADMIN de EPS/planes, gestión posterior de la afiliación y uso de la afiliación en reglas de agenda.
## Reglas de negocio
- La afiliación usa FKs; no afecta disponibilidad, precio, aprobación ni reserva.
## Dependencias y relaciones
- Épica: [[EP-002-identidad-y-perfil-del-usuario]]
- Dependencias: [[HU-003-publicar-catalogos-fijos]].
- Relacionadas: Ninguna.
## Esfuerzo
**Nivel:** Medio. **Justificación de dificultad:** enlaza catálogos configurables/fijos, integridad y ownership.
## Tareas de desarrollo
- [ ] **T-01 — Publicar planes activos.** Dificultad: Medio. Usar la relación normalizada EPS/plan/régimen.
- [ ] **T-02 — Extender registro opcional.** Dificultad: Alto. Validar plan activo y crear la afiliación sin duplicar textos de catálogo.
- [ ] **T-03 — Probar selección u omisión.** Dificultad: Medio. La omisión no impide el registro ni afecta agenda.
## Criterios de aceptación
### CA-01 — Asociación válida
**Dado** catálogos activos y una combinación válida, **cuando** USER guarda afiliación, **entonces** queda asociada a su perfil.
### CA-02 — Sin duplicidad
**Dado** una afiliación existente, **cuando** USER repite EPS, régimen o plan dentro de su afiliación, **entonces** la aplicación evita la duplicación definida.
### CA-03 — Aislamiento por usuario
**Dado** un USER autenticado, **cuando** consulta o modifica afiliación, **entonces** solo opera sobre su propia afiliación.
## Definition of Done
- [ ] CA-01 a CA-03 probados en dominio/REST y cliente aplicable.
- [ ] Persistencia 3FN y migración aplicable verificadas; no hay textos de catálogo duplicados.
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
- La regla de vigencia de una EPS/plan se abordará con sus HU administrativas.
