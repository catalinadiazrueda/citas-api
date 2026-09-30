# Configuración segura de n8n

Los tres JSON son plantillas inactivas. Importarlos no los activa; completa primero las credenciales y prueba cada salida con información sintética.

## Secretos

Genera dos valores distintos de al menos 32 bytes aleatorios. En PowerShell:

```powershell
[Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
```

Guarda el primer valor como `CITAS_AUTOMATION_KEY` en el entorno del backend. Guarda el segundo como `STATUS_WEBHOOK_SECRET`; `STATUS_WEBHOOK_URL` será la URL de producción del webhook n8n, por ejemplo `https://n8n.ejemplo/webhook/citas-status-change`. Reinicia el backend tras cambiar variables.

En n8n configura estas variables de instancia:

- `CITAS_API_URL`: URL base accesible desde el servidor n8n, sin `/api/v1` al final.
- `GMAIL_FROM`: dirección Gmail de laboratorio autorizada.
- `OPERATIONS_EMAIL`: destinatario sintético del resumen diario.

No escribas los valores secretos en este archivo, los workflows ni Git.

## Importación y credenciales

Importa `WF-001-appointment-reminders.json`, `WF-002-status-notifications.json` y `WF-003-daily-operational-summary.json` desde **Workflows → Import from File**.

En WF-001 y WF-003, crea una credencial **HTTP Header Auth** con nombre de encabezado `X-Automation-Key` y valor igual a `CITAS_AUTOMATION_KEY`. Selecciónala en el nodo HTTP Request.

En WF-002, crea otra credencial **HTTP Header Auth** con nombre `X-Webhook-Secret` y valor igual a `STATUS_WEBHOOK_SECRET`. Selecciónala en el Webhook Status. La autenticación está configurada como `headerAuth`.

En cada nodo Gmail, selecciona la credencial Gmail OAuth2 del buzón de laboratorio. Habilita Gmail API y configura consentimiento OAuth en el proyecto Google Cloud antes de autorizar la credencial de n8n. No reutilices una credencial personal.

## Prueba segura

1. Mantén los tres workflows inactivos mientras editas y pruebas.
2. Ejecuta manualmente WF-001 y confirma que consulta solo citas `APPROVED` en el periodo previsto y manda a un correo sintético.
3. Ejecuta WF-003 y confirma que el resultado solo agrupa por sede y estado.
4. Ejecuta una transición sintética de cita para WF-002. Debe entrar por la URL de producción del webhook y mostrar autenticación aprobada.
5. Revisa las ejecuciones exitosas y que los correos lleguen al buzón controlado.
6. Activa únicamente los flujos cuya ejecución controlada verificaste.

La API autentica WF-001/WF-003 con una clave dedicada de servidor a servidor; solo habilita las dos rutas de lectura `/api/v1/automation/*`. El webhook de estado lleva un secreto separado. Si se configura `STATUS_WEBHOOK_URL` y falta el secreto, el backend no inicia. Un fallo al entregar el webhook no revierte la transición de cita.
