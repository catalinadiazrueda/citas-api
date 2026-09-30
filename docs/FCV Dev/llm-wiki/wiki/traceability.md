# Trazabilidad

## HECHO

Las sesiones S2-S6 requieren commits y evidencias específicas. El backend y frontend deben mantener historial trazable; las pruebas y la evidencia cross-repo son parte de la evaluación.

## HECHO — 2026-09-17

Las HU-001 a HU-036 existen en `docs/FCV Dev/scrum/`. HU-001/002/003 tienen avance parcial; HU-004 define por ahora solo el contrato de identidad. La implementación backend de HU-005/006/007 cuenta con `AuthIntegrationTest`, `IdentityTest` y `AuthRequestGuardTest`; la integración web se controla mediante HU-033.

## HECHO — 2026-09-22

El catálogo de ocho subagentes fue versionado en `docs/FCV Dev/subagents/` y enlazado desde el orquestador. `citas-web` contiene trabajo local React/Vite de autenticación y pruebas; no debe declararse completado hasta ejecutar build, typecheck, tests y verificación cross-repo.

## HECHO — 2026-09-22 · Integración de autenticación

El prototipo `citas-web/portal-de-citas.zip` se importó como React/Vite y se integró con HU-005/006/007. La comprobación usa MySQL persistente, CORS explícito, registro/login/refresh/logout reales y pruebas de frontend. HU-033 permanece en progreso porque las pantallas de perfil, agenda y roles posteriores siguen fuera del corte de autenticación.

## HECHO — 2026-09-30 · Verificación local de entrega

- Backend: `mvn test` completo pasó 20/20 pruebas, incluidos Testcontainers con MySQL 8.4 y aplicación de las cinco migraciones. Maven compiló con `--release 21`; el proceso de pruebas usó Java 26 porque Java 21 no está instalado localmente.
- Frontend: `npm run lint`, `npm test` (13/13) y `npm run build` pasaron tras alinear el fallback de API con el puerto `8080` publicado por Docker Compose. Se añadieron paneles de perfil/afiliación y de administración EPS/planes con pruebas de componente; la verificación todavía no es una prueba cross-repo de esos paneles.
- Infraestructura/automatización: `docker compose config --quiet` pasó; los tres JSON WF-001/002/003 parsean y no activan el detector de patrones de secretos. Esto no demuestra que la instancia n8n los importe o ejecute.
- Pendiente externo: no se validó el n8n del trainer por error de certificado TLS; faltan las credenciales Gmail/MCP, las ejecuciones controladas y evidencia de GOAL/loops.

## HECHO — 2026-09-30 · Prueba aislada y revisión final de interfaz

- Se inició un stack Compose separado (`citas-audit`) en puertos `13308`, `18080` y `15173`, con volúmenes propios, sin detener ni reemplazar otras instalaciones Docker. MySQL 8.4 quedó saludable; la API arrancó en Java 21, validó las cinco migraciones y `GET /api/v1/catalogs/plans` respondió HTTP 200 con el seed sintético. El primer intento con origen web `127.0.0.1:15173` falló CORS por conservar el origen predeterminado `localhost:5173`; al alinear el origen solo en el override de auditoría, la preflight de registro respondió 200 con credenciales permitidas.
- La interfaz React/Vite cargó HTTP 200 desde la copia del workspace y apuntó a la API aislada. No se creó una cuenta ni se enviaron datos/correos durante esta prueba manual.
- Se corrigieron afirmaciones no respaldadas por el PRD (SMS, cifrado de extremo a extremo y certificación médica), el ejemplo telefónico quedó en formato colombiano y el consentimiento de registro ahora inicia desmarcado. `authScreens.test.tsx` comprueba que sin consentimiento no se llama al API.
- Frontend volvió a pasar `npm run lint`, `npm test` (13/13) y `npm run build`. La suite Maven 20/20 previamente pasó con JVM 26 compilando para Java 21; la ejecución de la aplicación dentro del contenedor confirmó Java 21 y migraciones, no una segunda suite Maven completa.
- La instancia n8n nueva fue accesible, pero no se modificó. La inspección visual mostró flujos duplicados y algunos publicados/activos; no se verificó la correspondencia con los JSON del repositorio ni entrega de correo.
