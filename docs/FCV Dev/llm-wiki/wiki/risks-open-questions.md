# Riesgos y preguntas abiertas

## Pendientes para declarar entrega completa

- `HU-033` sigue `En progreso`: el frontend ya tiene autenticación, reserva, gestión inicial de oferta/profesionales, citas, cancelación, reprogramación, perfil/afiliación, EPS/planes, auditoría visible y agenda por rol; ahora incluye edición de bloques, asignación múltiple de especialidades/sedes y activación de catálogos. Los paneles recientes tienen pruebas de componente, pero aún falta verificarlos contra API/MySQL y validar visualmente todas las pantallas frente al diseño aprobado. La edición de nombre/código de EPS/planes/especialidades no está expuesta en UI.
- `HU-008/009`: el backend implementa emisión y consumo de tokens y revoca sesiones, pero el canal controlado de desarrollo está pendiente de aprobación y la interfaz de recuperación/restablecimiento no está integrada. El inbox local es temporal en memoria y solo está disponible al ADMIN con perfil `local`; no se debe tratar como entrega real de correo.
- `HU-034/035/036`: los JSON están versionados y no llevan credenciales. No hay evidencia de importación ni ejecución en n8n, OAuth Gmail, webhook real o MCP contra la instancia del trainer. El navegador de trabajo rechazó la instancia compartida por certificado TLS no coincidente; pedir URL/certificado corregidos al trainer.
- El contrato de las automatizaciones usa valores de configuración para periodo, remitente y destinatario; confirmar esos parámetros de laboratorio y probar con datos sintéticos antes de activar workflows.
- La aprobación del diseño visual de Stitch/AI Studio debe quedar respaldada con evidencia de quien evalúa; no se debe declarar fidelidad visual completa solo por pasar el build.
- La validación backend compiló con `--release 21` y ejecutó 20 pruebas sobre MySQL 8.4, pero la JVM disponible para Maven en Windows fue Java 26. Falta repetirla con runtime Java 21 para verificar la plataforma exacta.
- El incremento final todavía no está fusionado a `main` ni publicado desde este cierre local. No promoverlo hasta resolver los pendientes externos y aprobar la entrega estable.
- Registrar evidencia auténtica S5/S6: ejecuciones n8n controladas, configuración MCP, análisis de contenido no confiable y ciclo Builder/Verifier/GOAL requerido por la guía. No fabricar capturas ni declarar runs que no ocurrieron.

## Decisiones ya documentadas

- Contratos REST de identidad, scheduling, lifecycle y operaciones están descritos en `contracts.md`.
- Las retenciones provisionales y auditoría del ciclo de vida se implementan con V4/V5; los tests de integración vigentes verifican cancelación, preservación/liberación de slots y reprogramación.
- Las rutas de lectura automatizada solo reciben `X-Automation-Key`; el webhook saliente usa un secreto independiente. Ambos secretos residen fuera de Git.
- La Skill `scrum-spec-orchestrator` conserva su allowlist histórica para `docs/wiki/scrum/`; no usarla en `docs/FCV Dev/scrum/` sin actualizarla expresamente.
