---
id: HU-005
tipo: historia-de-usuario
titulo: "Registrar usuario"
estado: Completada
epica: "[[EP-002-identidad-y-perfil-del-usuario]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 2"
dependencias: ["[[HU-001-inicializar-fundacion-tecnica]]", "[[HU-002-modelar-persistencia-3fn]]", "[[HU-004-definir-contrato-rest-inicial]]"]
relacionadas: ["[[HU-006-iniciar-sesion]]"]
---

# HU-005 — Registrar usuario
## Historia de usuario
**COMO** visitante  
**QUIERO** crear una cuenta USER con mis datos mínimos  
**PARA** acceder al agendamiento con una identidad propia.
## Contexto y descripción
Datos mínimos: nombres, apellidos, tipo/número de documento, email, teléfono y contraseña.
## Alcance
- Contrato REST de registro, validación server-side, unicidad y persistencia segura. El formulario queda en HU-033.
## Fuera de alcance
- Registro de ADMIN/PROFESSIONAL, validación externa de identidad o datos reales.
## Reglas de negocio
- Email y documento únicos; contraseña nunca en texto plano; el visitante solo obtiene rol USER.
## Dependencias y relaciones
- Épica: [[EP-002-identidad-y-perfil-del-usuario]]
- Dependencias: [[HU-001-inicializar-fundacion-tecnica]], [[HU-002-modelar-persistencia-3fn]], [[HU-004-definir-contrato-rest-inicial]].
- Relacionadas: [[HU-006-iniciar-sesion]].
## Esfuerzo
**Nivel:** Alto. **Justificación de dificultad:** coordina identidad, seguridad, validación, datos y cliente.
## Tareas de desarrollo
- [x] **T-01 — Diseñar entrada/salida de registro.** Dificultad: Medio. Acordar campos y errores del contrato.
- [x] **T-02 — Aplicar reglas de dominio/persistencia.** Dificultad: Alto. Validar requeridos, formato y unicidad.
- [x] **T-03 — Proteger credencial.** Dificultad: Alto. Aplicar hash adaptativo y exclusión de logs/respuestas.
- [x] **T-04 — Probar contrato backend.** Dificultad: Medio. Verificar validaciones observables, duplicados y ausencia de persistencia parcial; el flujo web queda en HU-033.
## Criterios de aceptación
### CA-01 — Registro válido
**Dado** datos mínimos válidos y únicos, **cuando** el visitante se registra, **entonces** se crea una cuenta con rol USER sin exponer la contraseña.
### CA-02 — Duplicados rechazados
**Dado** email o documento existente, **cuando** se intenta registrar, **entonces** se rechaza con error del contrato sin crear otra cuenta.
### CA-03 — Datos inválidos rechazados
**Dado** campos obligatorios o formatos inválidos, **cuando** se envía el registro, **entonces** se informa validación y no se persiste una cuenta parcial.
### CA-04 — Identidad única y credencial protegida
**Dado** un email con diferencias de mayúsculas/espacios o un documento del mismo tipo y número, **cuando** se registra, **entonces** se rechaza el duplicado sin crear otra cuenta; el password solo se almacena con BCrypt y no aparece en respuesta ni logs.
## Definition of Done
- [x] CA-01 a CA-04 validados con pruebas de dominio/REST/persistencia; integración de cliente diferida a HU-033.
- [x] Hay migración/coherencia 3FN si el esquema cambia.
- [x] Hash adaptativo, validación server-side y ausencia de credenciales en logs verificados.
- [x] Trazabilidad Scrum actualizada.
- [x] CA-04 y restricciones únicas Flyway probadas en REST/persistencia; `mvn test` pasa en Java 21/MySQL 8.4.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Cumple | `AuthIntegrationTest.registrationUsesUniqueIdentityAndBcrypt`, `AuthService.register`, `AuthController.register` | 201, rol USER y respuesta sin password. |
| CA-02 | Cumple | `V1__identity.sql` UK email/documento; `AuthIntegrationTest.registrationUsesUniqueIdentityAndBcrypt` | 409 sin segunda cuenta. |
| CA-03 | Cumple | `AuthController.RegisterRequest`, `Identity.email`, `AuthIntegrationTest.registrationUsesUniqueIdentityAndBcrypt` | Campo obligatorio ausente, email inválido y password fuera del límite técnico producen 400. |
| CA-04 | Cumple | `Identity.email`, `Identity.documentType`, `AuthService.register`, `AuthIntegrationTest.registrationUsesUniqueIdentityAndBcrypt` | Normalización, unicidad y BCrypt comprobados. |
| DoD pruebas | Cumple | `target/surefire-reports/*.txt` | `mvn test`: 9 pruebas, 0 fallos/errores en MySQL 8.4. |
| DoD esquema/seguridad | Cumple | `V1__identity.sql`, `data-integrity.md`, `AuthController`, `AuthService` | Flyway 3FN, validación server-side, sin password en respuesta ni logger de credenciales. |
| DoD trazabilidad | Cumple | Esta HU, HU-033, `contracts.md`, `traceability.md` | UI diferida a HU-033. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
- 2026-09-17 — Corte backend aprobado, validado y completado con `mvn test`; tareas de UI movidas a HU-033.
- 2026-09-22 — Verificación cruzada con esquema 3FN existente y cliente React: `mvn test` 9/9.
## Notas y decisiones
- El diseño visual requiere aprobación fuera de esta especificación.
- 2026-09-17: usuario aprobó el corte backend. Formulario y flujo visual pasan a HU-033; su ausencia no bloquea la DoD backend de HU-005. Documento único por tipo+número, email normalizado sin distinguir mayúsculas. No se implementa registro de otros roles.
