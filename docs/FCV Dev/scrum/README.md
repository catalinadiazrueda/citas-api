---
tipo: indice-scrum
estado: Pendiente de aprobación
---

# Mapa Scrum / Spec-Driven Development — Sistema de citas

## Propósito y límites

Este mapa convierte el PRD v1 y las restricciones autorizadas en trabajo secuencial y verificable. El usuario aprobó el 2026-09-17 el corte backend de HU-001/002/004/005/006/007 y el seed parcial de roles de HU-003; las demás HU requieren revisión explícita. El contrato de autenticación está en la LLM Wiki; el framework web sigue sin aprobación.

## Arquitectura y supuestos constatados

- Backend requerido: Java 21, Spring Boot 3.5.x, Maven, arquitectura hexagonal, JPA, Flyway, MySQL 8.4 y REST/JSON.
- Cliente requerido: TypeScript con React o Angular por decidir, consume REST directo; no hay Express ni BFF.
- Los catálogos fijos se cargan por seed; los datos del laboratorio son sintéticos.
- El repositorio contiene la aplicación backend y el contrato inicial de identidad. HU-005/006/007 están `Completada` para el corte backend con evidencia de `mvn test` (8 pruebas, 0 fallos); HU-001/002/003 y las épicas EP-001/002 permanecen parciales.

## Épicas

- [[EP-001-fundacion-y-contrato-del-producto]]
- [[EP-002-identidad-y-perfil-del-usuario]]
- [[EP-003-administracion-de-catalogos-y-profesionales]]
- [[EP-004-disponibilidad-del-profesional]]
- [[EP-005-busqueda-y-reserva-de-citas]]
- [[EP-006-ciclo-de-vida-de-citas-y-reprogramaciones]]
- [[EP-007-operacion-profesional-y-administrativa]]
- [[EP-008-cliente-web-y-automatizaciones-posteriores]]

## Incrementos sugeridos

Los sprints son incrementos funcionales secuenciales, no estimaciones de duración ni capacidad.

1. **Incremento 1 — Fundaciones trazables:** [[HU-001-inicializar-fundacion-tecnica]], [[HU-002-modelar-persistencia-3fn]], [[HU-003-publicar-catalogos-fijos]], [[HU-004-definir-contrato-rest-inicial]]. Resultado: base verificable para construir y consumir el producto.
2. **Incremento 2 — Acceso y datos del usuario:** [[HU-005-registrar-usuario]], [[HU-006-iniciar-sesion]], [[HU-007-renovar-y-cerrar-sesion]], [[HU-008-solicitar-recuperacion-de-contrasena]], [[HU-009-restablecer-contrasena]], [[HU-010-gestionar-perfil]], [[HU-011-gestionar-afiliacion]]. Resultado: USER autenticado y con perfil/afiliación coherentes.
3. **Incremento 3 — Oferta clínica administrable:** [[HU-012-gestionar-eps]], [[HU-013-gestionar-planes-eps]], [[HU-014-gestionar-especialidades-y-duracion]], [[HU-015-crear-profesional]], [[HU-016-asignar-especialidades-al-profesional]], [[HU-017-asignar-sedes-y-estado-del-profesional]]. Resultado: oferta sintética y habilitable para agenda.
4. **Incremento 4 — Disponibilidad y reserva:** [[HU-018-crear-bloques-de-disponibilidad]], [[HU-019-modificar-bloques-futuros]], [[HU-020-consultar-calendario-de-disponibilidad]], [[HU-021-buscar-disponibilidad]], [[HU-022-reservar-cita-general]], [[HU-023-solicitar-cita-especializada]], [[HU-024-resolver-solicitud-especializada]]. Resultado: citas generales aprobadas y especializadas bajo decisión ADMIN.
5. **Incremento 5 — Continuidad de la cita:** [[HU-025-consultar-mis-citas]], [[HU-026-cancelar-cita]], [[HU-027-solicitar-reprogramacion]], [[HU-028-resolver-reprogramacion]]. Resultado: USER administra sus citas sin vulnerar reservas.
6. **Incremento 6 — Operación controlada:** [[HU-029-consultar-agenda-profesional]], [[HU-030-cerrar-atencion]], [[HU-031-consultar-bandeja-administrativa]], [[HU-032-consultar-auditoria-de-estados]]. Resultado: profesionales y ADMIN operan con visibilidad y auditoría.
7. **Incremento 7 — Cliente y automatizaciones posteriores:** [[HU-033-integrar-cliente-web-con-api]], [[HU-034-automatizar-recordatorios]], [[HU-035-notificar-cambios-de-estado]], [[HU-036-generar-resumen-operativo-diario]]. Resultado: flujos web integrados y automatizaciones S5/S6 sin cambiar el núcleo.

## Decisiones e incógnitas que requieren revisión

- Seleccionar React o Angular después del flujo Stitch/AI Studio; las HU de cliente no presuponen uno.
- Diseñar y aprobar el contrato REST antes de que las HU consumidoras lo usen; no se han definido rutas ni formatos.
- Definir de forma consistente los valores concretos de catálogos fijos al realizar el seed, manteniendo lo que el PRD exige.
- Precisar el canal seguro de exposición controlada del token de recuperación en desarrollo antes de implementar [[HU-008-solicitar-recuperacion-de-contrasena]].
- Las automatizaciones de [[EP-008-cliente-web-y-automatizaciones-posteriores]] son posteriores al núcleo y dependen de la instancia/credenciales del trainer; sus JSON vivirán en `automations/n8n/` sin credenciales.

## Regla de selección para S2, S3 y S4

Solo se puede seleccionar una HU cuyo estado sea `Aprobada`, cuyas dependencias estén `Completada` o se incluyan explícitamente en el mismo incremento secuencial, y cuyo contrato/decisión pendiente no altere su alcance. Este índice no concede aprobación.
