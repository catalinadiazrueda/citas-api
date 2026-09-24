---
id: EP-001
tipo: epica
titulo: "Fundación y contrato del producto"
estado: Pendiente de aprobación
historias:
  - "[[HU-001-inicializar-fundacion-tecnica]]"
  - "[[HU-002-modelar-persistencia-3fn]]"
  - "[[HU-003-publicar-catalogos-fijos]]"
  - "[[HU-004-definir-contrato-rest-inicial]]"
dependencias: []
---

# EP-001 — Fundación y contrato del producto

## Objetivo

Establecer una base técnica, de datos y de contrato que permita construir funcionalidades sin inventar acuerdos compartidos.

## Valor esperado

El equipo puede entregar incrementos verificables sobre una base normalizada, segura y consumible directamente por el cliente web.

## Actores

- Equipo de desarrollo
- USER, PROFESSIONAL y ADMIN como consumidores futuros

## Alcance

- Fundación backend, modelo 3FN, catálogos fijos y contrato REST inicial documentado.

## Fuera de alcance

- Implementar una regla funcional de agenda o definir pantallas finales.

## Reglas de negocio

- Datos sintéticos; secretos fuera del repositorio.
- Catálogos fijos por seed y REST directo sin BFF.

## Dependencias

- Ninguna externa al alcance documentado.

## Historias de usuario

- [[HU-001-inicializar-fundacion-tecnica]]
- [[HU-002-modelar-persistencia-3fn]]
- [[HU-003-publicar-catalogos-fijos]]
- [[HU-004-definir-contrato-rest-inicial]]

## Criterio de completitud de la épica

- [ ] Las cuatro HU están `Completada` con evidencia.
- [ ] Las HU posteriores pueden enlazar una base de datos y contrato aprobados.

## Riesgos e incógnitas

- No existe aún contrato REST ni elección de framework web.
