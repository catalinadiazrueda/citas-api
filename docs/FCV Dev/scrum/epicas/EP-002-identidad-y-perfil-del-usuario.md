---
id: EP-002
tipo: epica
titulo: "Identidad y perfil del usuario"
estado: Pendiente de aprobación
historias:
  - "[[HU-005-registrar-usuario]]"
  - "[[HU-006-iniciar-sesion]]"
  - "[[HU-007-renovar-y-cerrar-sesion]]"
  - "[[HU-008-solicitar-recuperacion-de-contrasena]]"
  - "[[HU-009-restablecer-contrasena]]"
  - "[[HU-010-gestionar-perfil]]"
  - "[[HU-011-gestionar-afiliacion]]"
dependencias:
  - "[[EP-001-fundacion-y-contrato-del-producto]]"
---

# EP-002 — Identidad y perfil del usuario

## Objetivo

Permitir que un visitante se convierta en USER autenticado y gestione sus datos permitidos sin exponer credenciales.

## Valor esperado

Cada usuario tiene una identidad única, sesiones controlables y una afiliación consistente para usar el servicio.

## Actores

- USER
- Visitante

## Alcance

- Registro, JWT access/refresh, logout, recuperación, perfil y afiliación.

## Fuera de alcance

- Verificación externa de identidad, correo SMTP obligatorio o gestión de roles por USER.

## Reglas de negocio

- Email y documento son únicos; contraseñas con hash adaptativo.
- Refresh y recuperación son temporales; el token de recuperación es de un uso.
- No se duplican EPS, régimen o plan dentro de una afiliación de USER.

## Dependencias

- [[EP-001-fundacion-y-contrato-del-producto]]

## Historias de usuario

- [[HU-005-registrar-usuario]]
- [[HU-006-iniciar-sesion]]
- [[HU-007-renovar-y-cerrar-sesion]]
- [[HU-008-solicitar-recuperacion-de-contrasena]]
- [[HU-009-restablecer-contrasena]]
- [[HU-010-gestionar-perfil]]
- [[HU-011-gestionar-afiliacion]]

## Criterio de completitud de la épica

- [ ] Todas las HU están `Completada` y las rutas protegidas aplican rol/ownership.

## Riesgos e incógnitas

- Debe aprobarse la modalidad de desarrollo para entregar el token de recuperación sin filtrarlo indebidamente.
