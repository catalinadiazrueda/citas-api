---
id: HU-008
tipo: historia-de-usuario
titulo: "Solicitar recuperación de contraseña"
estado: Pendiente de aprobación
epica: "[[EP-002-identidad-y-perfil-del-usuario]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 2"
dependencias: ["[[HU-005-registrar-usuario]]", "[[HU-004-definir-contrato-rest-inicial]]"]
relacionadas: ["[[HU-009-restablecer-contrasena]]"]
---

# HU-008 — Solicitar recuperación de contraseña
## Historia de usuario
**COMO** usuario que no recuerda su contraseña  
**QUIERO** solicitar un token temporal de recuperación por email  
**PARA** restablecer mi acceso de forma segura.
## Contexto y descripción
SMTP real es opcional; en desarrollo el token solo puede exponerse por vía segura controlada.
## Alcance
- Solicitud por email, emisión temporal/de único uso y canal controlado de desarrollo aprobado.
## Fuera de alcance
- SMTP obligatorio, SMS/WhatsApp o revelar tokens en logs públicos.
## Reglas de negocio
- Token temporal y de único uso; secretos/tokens no deben registrarse.
## Dependencias y relaciones
- Épica: [[EP-002-identidad-y-perfil-del-usuario]]
- Dependencias: [[HU-005-registrar-usuario]], [[HU-004-definir-contrato-rest-inicial]].
- Relacionadas: [[HU-009-restablecer-contrasena]].
## Esfuerzo
**Nivel:** Medio. **Justificación de dificultad:** exige token seguro y una decisión de canal de desarrollo.
## Tareas de desarrollo
- [ ] **T-01 — Definir token temporal.** Dificultad: Alto. Establecer expiración, consumo y almacenamiento seguro.
- [ ] **T-02 — Diseñar solicitud y respuesta segura.** Dificultad: Medio. No filtrar datos o token sin mecanismo aprobado.
- [ ] **T-03 — Integrar pantalla/feedback.** Dificultad: Bajo. Informar resultado sin revelar información sensible.
- [ ] **T-04 — Probar ciclo de emisión.** Dificultad: Medio. Cubrir email existente/no existente sin enumeración indebida.
## Criterios de aceptación
### CA-01 — Token temporal
**Dado** una solicitud válida, **cuando** se procesa, **entonces** se genera un token temporal asociado al usuario y apto para un solo uso.
### CA-02 — Respuesta segura
**Dado** cualquier email recibido, **cuando** se solicita recuperación, **entonces** la respuesta no expone credenciales, tokens ni información innecesaria de existencia de cuenta.
### CA-03 — Desarrollo controlado
**Dado** que no hay SMTP real, **cuando** se ejecuta en desarrollo, **entonces** el token solo se entrega mediante mecanismo seguro previamente aprobado.
## Definition of Done
- [ ] CA-01 a CA-03 tienen pruebas/evidencia y el canal de desarrollo está documentado.
- [ ] Tokens no aparecen en logs ni repositorio; esquema/migración aplicable está verificado.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 / DoD | Pendiente | — | Requiere decisión de canal. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
## Notas y decisiones
- Incógnita abierta: mecanismo seguro de entrega local.
