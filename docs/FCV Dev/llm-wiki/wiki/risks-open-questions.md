# Riesgos y preguntas abiertas

## Pendientes para declarar entrega completa

- `HU-033` sigue `En progreso`: el frontend tiene autenticación, reserva, gestión de oferta/profesionales, citas, cancelación, reprogramación, perfil/afiliación, EPS/planes, auditoría visible y agenda por rol; incluye edición de bloques, asignación múltiple de especialidades/sedes y activación de catálogos. Los paneles recientes tienen pruebas de componente y la interfaz se cargó contra la API en un stack aislado, pero falta cobertura manual de los recorridos por rol y aprobación visual frente al diseño. La edición de nombre/código de EPS/planes/especialidades no está expuesta en UI.
- `HU-008/009`: el backend implementa emisión/consumo de tokens y revocación de sesiones, pero el canal de desarrollo sigue pendiente de aprobación y la interfaz de recuperación/restablecimiento no está integrada. El inbox local es temporal en memoria y solo está disponible al ADMIN con perfil `local`; no se debe tratar como correo real. No se usó la cuenta de correo del curso.
- `HU-034/035/036`: los JSON están versionados y no llevan credenciales. La nueva URL del trainer es accesible, pero no se cambió la instancia. La inspección mostró copias de flujos y estados publicados/activos; no se confirmó que correspondan a los JSON versionados ni que hayan entregado mensajes. OAuth/correo, webhook de punta a punta y MCP siguen sin validación del entregable.
- El contrato de las automatizaciones usa valores de configuración para periodo, remitente y destinatario; confirmar esos parámetros de laboratorio y probar con datos sintéticos antes de activar workflows.
- La aprobación del diseño visual de Stitch/AI Studio debe quedar respaldada con evidencia de quien evalúa; no se debe declarar fidelidad visual completa solo por pasar el build.
- La suite backend de 20 pruebas compiló con `--release 21` y se ejecutó con JVM Java 26 sobre MySQL 8.4. La API ya arrancó con Java 21 en el stack aislado, pero falta repetir la suite completa bajo ese runtime.
- El incremento final todavía no está fusionado a `main` ni publicado desde este cierre local. No promoverlo hasta resolver los pendientes externos y aprobar la entrega estable.
- Registrar evidencia auténtica S5/S6: ejecución controlada de los JSON del repositorio, configuración MCP, análisis de contenido no confiable y ciclo Builder/Verifier/GOAL requerido por la guía. No fabricar capturas ni declarar runs que no ocurrieron.

## Decisiones ya documentadas

- Contratos REST de identidad, scheduling, lifecycle y operaciones están descritos en `contracts.md`.
- Las retenciones provisionales y auditoría del ciclo de vida se implementan con V4/V5; los tests de integración vigentes verifican cancelación, preservación/liberación de slots y reprogramación.
- Las rutas de lectura automatizada solo reciben `X-Automation-Key`; el webhook saliente usa un secreto independiente. Ambos secretos residen fuera de Git.
- La Skill `scrum-spec-orchestrator` conserva su allowlist histórica para `docs/wiki/scrum/`; no usarla en `docs/FCV Dev/scrum/` sin actualizarla expresamente.
