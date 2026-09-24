---
id: HU-007
tipo: historia-de-usuario
titulo: "Renovar y cerrar sesión"
estado: Completada
epica: "[[EP-002-identidad-y-perfil-del-usuario]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 2"
dependencias: ["[[HU-006-iniciar-sesion]]"]
relacionadas: ["[[HU-009-restablecer-contrasena]]"]
---

# HU-007 — Renovar y cerrar sesión
## Historia de usuario
**COMO** usuario autenticado  
**QUIERO** renovar mi acceso y cerrar mi sesión  
**PARA** continuar de forma segura o revocar el acceso cuando termine.
## Contexto y descripción
La API permite refresh y revocación/logout.
## Alcance
- Renovación con refresh válido y revocación/logout por REST; limpieza de estado visual en HU-033.
## Fuera de alcance
- Gestión multi-dispositivo no definida por el PRD.
## Reglas de negocio
- Refresh inválido/revocado no habilita acceso; logout revoca la sesión correspondiente.
## Dependencias y relaciones
- Épica: [[EP-002-identidad-y-perfil-del-usuario]]
- Dependencias: [[HU-006-iniciar-sesion]].
- Relacionadas: [[HU-009-restablecer-contrasena]].
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** requiere ciclo seguro de token y coherencia servidor/cliente.
## Tareas de desarrollo
- [x] **T-01 — Persistir/validar refresh.** Dificultad: Alto. Aplicar expiración y revocación sin exponer tokens.
- [x] **T-02 — Definir refresh/logout REST.** Dificultad: Medio. Documentar respuestas y errores.
- [x] **T-03 — Documentar ciclo para cliente futuro.** Dificultad: Medio. Exponer renovación y cookie borrada; la integración web queda en HU-033.
- [x] **T-04 — Probar revocación.** Dificultad: Alto. Cubrir válido, expirado y revocado.
## Criterios de aceptación
### CA-01 — Renovación controlada
**Dado** un refresh vigente y no revocado, **cuando** se solicita renovación, **entonces** se recibe un nuevo access conforme al contrato.
### CA-02 — Refresh no reutilizable tras revocación
**Dado** un refresh inválido, vencido o revocado, **cuando** se solicita renovación, **entonces** se deniega el acceso.
### CA-03 — Logout efectivo
**Dado** una sesión activa, **cuando** el usuario cierra sesión, **entonces** su refresh se revoca y la API borra la cookie. El cliente eliminará su estado autenticado en HU-033.
### CA-04 — Rotación atómica
**Dado** un refresh vigente, **cuando** se renueva, **entonces** se emiten access y refresh nuevos y el anterior no puede volver a utilizarse, incluso ante intentos concurrentes.
## Definition of Done
- [x] CA-01 a CA-04 validados con pruebas de seguridad/REST/persistencia; integración de cliente diferida a HU-033.
- [x] No se loguean tokens y la revocación queda persistida/observable según contrato.
- [x] Trazabilidad Scrum actualizada.
- [x] CA-04 y refresh vencido, falsificado, revocado, reutilizado o de tipo incorrecto probados; cookie borrada y `mvn test` exitoso.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Cumple | `AuthService.refresh`, `AuthIntegrationTest.loginRefreshLogoutAndRoles` | Nuevo access y nueva cookie refresh. |
| CA-02 | Cumple | `JwtTokens.readRefresh`, `AuthService.refresh`, `AuthIntegrationTest.invalidCredentialsTokensAndOrigin` | Vencido, falsificado y tipo access rechazados; revocado y reutilizado probados en ciclo. |
| CA-03 | Cumple | `AuthService.logout`, `AuthController.logout`, `AuthIntegrationTest.loginRefreshLogoutAndRoles` | 204, cookie Max-Age=0 y refresh anterior no reutilizable. |
| CA-04 | Cumple | `SessionJpaAdapter.lockByJtiHash`, `AuthIntegrationTest.simultaneousRefreshAllowsOnlyOneRotation` | Bajo concurrencia, una renovación 200 y otra 401. |
| DoD pruebas | Cumple | `target/surefire-reports/*.txt` | `mvn test`: 9 pruebas, 0 fallos/errores. |
| DoD revocación/logs | Cumple | `V1__identity.sql`, `AuthService`, `SessionJpaAdapter` | Se persiste solo hash de jti, vigencia/revocación; no hay logger de tokens. |
| DoD trazabilidad | Cumple | Esta HU, HU-033, `contracts.md`, `traceability.md` | Limpieza visual diferida a HU-033. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
- 2026-09-17 — Corte backend aprobado, validado y completado con `mvn test`; tareas de UI movidas a HU-033.
- 2026-09-22 — Rotación y logout verificados contra MySQL existente y el cliente React; `mvn test` 9/9.
## Notas y decisiones
- No se inventa una política global de revocación fuera del PRD.
- 2026-09-17: usuario aprobó el corte backend con rotación por uso y logout. La limpieza de estado del cliente pasa a HU-033; la DoD backend se valida por cookie eliminada y revocación persistida.
