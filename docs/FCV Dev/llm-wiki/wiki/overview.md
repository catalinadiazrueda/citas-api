# Resumen del proyecto

## HECHO

El workspace académico contiene dos repositorios independientes: backend Spring Boot (`citas-api`) y frontend TypeScript (`citas-web`). El frontend consume el backend directamente por REST. La aplicación es un sistema ficticio de agendamiento de citas y no usa datos clínicos reales.

## HECHO — 2026-09-22

El frontend ya usa React 19, TypeScript y Vite. El working tree contiene componentes importados, un cliente de autenticación y pruebas que deben verificarse antes de declarar completa la integración.

El contrato REST aprobado cubre identidad bajo `/api/v1/auth` y el corte S3 de catálogos, oferta, disponibilidad, reservas y decisión ADMIN bajo `/api/v1`. El frontend consume ambos contratos directamente.
