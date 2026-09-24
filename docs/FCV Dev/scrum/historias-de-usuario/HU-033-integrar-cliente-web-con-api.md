---
id: HU-033
tipo: historia-de-usuario
titulo: "Integrar cliente web con API"
estado: En progreso
epica: "[[EP-008-cliente-web-y-automatizaciones-posteriores]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 7"
dependencias: ["[[HU-004-definir-contrato-rest-inicial]]", "[[HU-032-consultar-auditoria-de-estados]]"]
relacionadas: []
---
# HU-033 — Integrar cliente web con API
## Historia de usuario
**COMO** usuario de cualquiera de los roles  
**QUIERO** acceder por web a las pantallas obligatorias conectadas directamente a la API  
**PARA** completar los flujos aprobados del producto.
## Contexto y descripción
La estética se deriva del prototipo React/Vite importado desde AI Studio. Esta HU consolida integración, no duplica reglas backend.
## Alcance
- Rutas/pantallas obligatorias de registro, sesión, perfil, disponibilidad/citas, dashboards profesional y ADMIN, y CRUD ADMIN mediante contrato aprobado.
## Fuera de alcance
- Elegir framework sin aprobación, Express/BFF o inventar comportamiento no disponible en API.
## Reglas de negocio
- REST directo a `citas-api`; URL por environment; CORS explícito; UI respeta roles/ownership del backend.
## Dependencias y relaciones
- Épica: [[EP-008-cliente-web-y-automatizaciones-posteriores]]
- Dependencias: [[HU-004-definir-contrato-rest-inicial]], [[HU-032-consultar-auditoria-de-estados]].
- Relacionadas: Ninguna.
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** integra múltiples flujos/roles y depende de decisión visual/framework.
## Tareas de desarrollo
- [x] **T-01 — Acordar framework/diseño.** Dificultad: Alto. Prototipo React/Vite importado; se conserva su composición visual para el corte de autenticación.
- [ ] **T-02 — Implementar navegación/estado por rol.** Dificultad: Alto. El estado USER de autenticación está integrado; faltan pantallas obligatorias de historias posteriores.
- [x] **T-03 — Conectar cliente REST.** Dificultad: Alto. Registro, login, refresh y logout consumen el contrato versionado directo, por environment y sin BFF.
- [x] **T-04 — Verificar cross-repo.** Dificultad: Alto. Typecheck, pruebas, build y flujo visual USER comprobados contra API/MySQL.
## Criterios de aceptación
### CA-01 — Pantallas obligatorias
**Dado** el diseño y las HU aprobadas, **cuando** cada rol navega, **entonces** puede llegar a las pantallas obligatorias pertinentes del PRD.
### CA-02 — Consumo directo
**Dado** una acción web de una HU aprobada, **cuando** se ejecuta, **entonces** el cliente llama directamente a `citas-api` por REST/JSON usando URL configurable.
### CA-03 — Manejo de seguridad
**Dado** una respuesta de autorización/validación, **cuando** el cliente la recibe, **entonces** muestra un resultado acorde al contrato sin exponer tokens o secretos.
## Definition of Done
- [ ] CA-01 a CA-03 validados para los flujos aprobados en ambos repositorios.
- [ ] Framework y diseño visual aprobados; build/typecheck y pruebas aplicables con evidencia.
- [ ] No existe Express/BFF; contrato/cors/environment y Scrum están actualizados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Parcial | `LoginScreen`, `RegisterScreen`, `App`, validación visual 2026-09-22 | Registro, login, restauración y logout USER listos; las demás pantallas dependen de HU posteriores. |
| CA-02 | Cumple para autenticación | `src/auth/authApi.ts`, `.env.example`, CORS de `SecurityConfig` | REST directo a `/api/v1/auth`, `credentials: include` y `X-Requested-With`; sin Express/BFF. |
| CA-03 | Cumple para autenticación | `authApi.test.ts`, `authScreens.test.tsx`, validación visual | Access solo en memoria; refresh cookie HttpOnly; errores 400/401/403/409 mapeados sin exponer tokens. |
| DoD del corte auth | Cumple | `npm run lint`, `npm test` (8/8), `npm run build`; `mvn test` (9/9) | Preflight CORS y login USER comprobados contra MySQL persistente. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
- 2026-09-22 — Corte React de autenticación implementado y verificado cross-repo; la HU queda en progreso hasta cubrir las pantallas de sus dependencias posteriores.
## Notas y decisiones
- React + TypeScript + Vite se adopta para este corte a partir del prototipo entregado `portal-de-citas.zip`.
- 2026-09-17: aquí quedan las tareas visuales diferidas de HU-005/006/007: formulario de registro, feedback de login, renovación desde navegador y limpieza de estado autenticado al salir. Integrar `credentials`, `X-Requested-With` y el contrato de cookie cuando se aborde la UI.
