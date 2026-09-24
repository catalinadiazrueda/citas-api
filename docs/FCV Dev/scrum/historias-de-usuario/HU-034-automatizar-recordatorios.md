---
id: HU-034
tipo: historia-de-usuario
titulo: "Automatizar recordatorios"
estado: Pendiente de aprobación
epica: "[[EP-008-cliente-web-y-automatizaciones-posteriores]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 7"
dependencias: ["[[HU-025-consultar-mis-citas]]", "[[HU-032-consultar-auditoria-de-estados]]"]
relacionadas: ["[[HU-036-generar-resumen-operativo-diario]]"]
---
# HU-034 — Automatizar recordatorios
## Historia de usuario
**COMO** operación del laboratorio  
**QUIERO** ejecutar recordatorios de citas próximas mediante n8n y Gmail  
**PARA** comunicar oportunamente las citas sin cambiar el núcleo funcional.
## Contexto y descripción
Automatización prevista para S5/S6; usa instancia central del trainer y credenciales configuradas por estudiante, nunca versionadas.
## Alcance
- Workflow n8n exportado en `automations/n8n/`, selección de próximas citas y envío Gmail configurado externamente.
## Fuera de alcance
- SMTP propio, SMS/WhatsApp, credenciales en JSON o cambio de reglas de cita.
## Reglas de negocio
- n8n no altera núcleo; datos/credenciales se tratan de forma segura y sintética para pruebas.
## Dependencias y relaciones
- Épica: [[EP-008-cliente-web-y-automatizaciones-posteriores]]
- Dependencias: [[HU-025-consultar-mis-citas]], [[HU-032-consultar-auditoria-de-estados]].
- Relacionadas: [[HU-036-generar-resumen-operativo-diario]].
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** integra automatización externa, datos de agenda y gestión segura de credenciales.
## Tareas de desarrollo
- [ ] **T-01 — Definir fuente/criterio de próximas citas.** Dificultad: Alto. Acordar contrato de lectura sin cambiar núcleo.
- [ ] **T-02 — Configurar workflow en n8n trainer.** Dificultad: Alto. Usar credenciales externas y manejo de fallos.
- [ ] **T-03 — Exportar/verificar JSON.** Dificultad: Medio. Versionar sin credenciales y probar con datos sintéticos.
## Criterios de aceptación
### CA-01 — Recordatorio ejecutable
**Dado** citas próximas elegibles y configuración externa válida, **cuando** corre el workflow, **entonces** prepara/envía el recordatorio por Gmail según el diseño aprobado.
### CA-02 — Núcleo intacto
**Dado** la ejecución, **cuando** se revisan citas/estados, **entonces** el workflow no altera el núcleo funcional.
### CA-03 — Exportación segura
**Dado** el workflow terminado, **cuando** se revisa su JSON versionado, **entonces** está en `automations/n8n/` y no contiene credenciales.
## Definition of Done
- [ ] CA-01 a CA-03 tienen evidencia de ejecución controlada, JSON y revisión de seguridad.
- [ ] Credenciales permanecen solo en n8n/environment y se usan datos sintéticos.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | Depende del trainer. |
| CA-02 | Pendiente | — | — |
| CA-03 / DoD | Pendiente | — | — |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
## Notas y decisiones
- No se presupone cuándo se considera “próxima”; requiere configuración aprobada.
