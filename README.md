## SportPulse — Plataforma de Microservicios de Fútbol

![Java](https://img.shields.io/badge/Java-17+-ED8B00?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=springboot)
![Microservices](https://img.shields.io/badge/Architecture-Microservices-orange)
![Build](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Container-Docker-2496ED?logo=docker&logoColor=white)
![Database](https://img.shields.io/badge/Database-PostgreSQL-4169E1?logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Cache-Redis-DC382D?logo=redis&logoColor=white)

**Equipo:** Equipo 10  
**Tipo:** Backend con arquitectura de microservicios  
**API Externa:** API-Football (RapidAPI)

---

## Descripción del proyecto

SportPulse es una plataforma de análisis de fútbol en tiempo real, basada en una
arquitectura de microservicios que se comunican entre si para recuperar, procesar y exponer
datos provenientes de la API-Football de RapidAPI, permitiendo entregar métricas,
eventos y visualizaciones actualizadas de forma continua.

---

## Lista de microservicios

| Microservicio      | Puerto | Responsabilidad                                      | 
|--------------------|--------|------------------------------------------------------|
| `ms-gateway`       | 8080   | Punto de entrada único, enrutamiento y rate limiting |
| `ms-auth`          | 8081   | Registro, login y emisión/validación de tokens JWT   |
| `ms-leagues`       | 8082   | Información de ligas, países y temporadas            |
| `ms-teams`         | 8083   | Equipos, escudos e información general               |
| `ms-fixtures`      | 8085   | Partidos, calendarios y resultados                   |
| `ms-standings`     | 8086   | Clasificaciones por liga y temporada                 |
| `ms-notifications` | 8088   | Suscripciones y alertas de eventos                   |
| `ms-dashboard`     | 8089   | Resumen ejecutivo                                    |

---

## Arquitectura

    Cliente
    │
    ▼
    ms-gateway (8080) ─── Redis (rate limiting)
    │
    ├── ms-auth (8081) ─── PostgreSQL (auth_db)
    ├── ms-leagues (8082)
    ├── ms-teams (8083)
    ├── ms-fixtures (8085) ◄─── ms-teams
    ├── ms-standings (8086) ◄─── ms-teams
    ├── ms-notifications (8088) ◄─── ms-fixtures ─── PostgreSQL (notifications_db)
    └── ms-dashboard (8089) ◄─── ms-fixtures, ms-standings, ms-leagues

Todos los microservicios protegidos reciben un JWT validado por el gateway, que inyecta `X-User-Id` y `X-User-Role` en
los headers de la petición interna.

---

## Implementación actual detallada

### ms-gateway

El gateway está completamente implementado con las siguientes capacidades:

- **Enrutamiento** de todas las rutas hacia sus microservicios correspondientes
- **Autenticación JWT** vía filtro reactivo (`JwtGatewayFilter`): valida el token y propaga el userId y rol como headers
  internos
- **Rate limiting** por IP usando Redis (`RedisRateLimiter`): 60 requests por minuto con burst de hasta 60 requests
- **Circuit breaker** con Resilience4j: ventana deslizante de 10 llamadas, apertura al 50% de fallos, recuperación
  automática tras 10s
- **Fallback** por servicio: devuelve un error estructurado con código `SERVICE_UNAVAILABLE` cuando un circuito está
  abierto
- **Manejo global de errores** reactivo: respuestas JSON consistentes con código, mensaje, metadata y timestamp
- Rutas públicas (`/api/auth/login`, `/api/auth/register`) sin autenticación; el resto requieren `Bearer <token>`

### ms-auth

Microservicio de autenticación completamente implementado:

- **Registro** (`POST /api/auth/register`): validación de username (3–20 chars, alfanumérico+guion bajo), email y
  contraseña (min 8 chars, al menos una mayúscula y un número); codificación con BCrypt
- **Login** (`POST /api/auth/login`): autenticación por email/contraseña; devuelve JWT con `userId`, `username` y `role`
- **Validación de token** (`POST /api/auth/validate`): extrae y devuelve claims del JWT; usado internamente por el
  gateway
- **JWT** generado con JJWT 0.12.6 (HS256), configurable por variables de entorno (`JWT_SECRET`, `JWT_EXPIRATION`)
- **Persistencia** en PostgreSQL vía JPA/Hibernate con entidad `User` (UUID como PK, roles `USER`/`ADMIN`)
- **Seguridad** con Spring Security en modo stateless; el filtro `JwtAuthorizationFilter` protege rutas no públicas
- **Mapeo** con MapStruct entre DTOs y entidades
- **Manejo de errores**: `UserAlreadyExists` (409), `InvalidCredentialsException` (401), `UserNotFoundException` (404),
  errores de validación (400)

### ms-leagues / ms-teams

Microservicios en fase inicial. Tienen configurada la estructura base:

- Seguridad configurada con todas las rutas permitidas (sin autenticación propia; la validación la delega al gateway)
- Endpoints de prueba de conectividad con el gateway (`/gateway-test-*`)
- Configuración de JWT pendiente de uso real
- Lógica de negocio (integración con API-Football, caching) pendiente de implementar

### ms-fixtures / ms-standings / ms-notifications / ms-dashboard

Microservicios scaffoldeados (estructura Maven + Dockerfile + `application.yaml` mínimo). La lógica de negocio está
pendiente de implementar.

---

## Stack tecnológico

| Capa          | Tecnología                                    |
|---------------|-----------------------------------------------|
| Lenguaje      | Java 17                                       |
| Framework     | Spring Boot 3.5, Spring Cloud 2025.0.2        |
| Gateway       | Spring Cloud Gateway (WebFlux reactivo)       |
| Seguridad     | Spring Security + JWT (JJWT 0.12.6)           |
| Resiliencia   | Resilience4j (Circuit Breaker + Time Limiter) |
| Rate limiting | Redis + RedisRateLimiter                      |
| Persistencia  | Spring Data JPA + PostgreSQL 15               |
| HTTP client   | OpenFeign (comunicación inter-servicios)      |
| Documentación | SpringDoc OpenAPI (Swagger UI)                |
| Mapeo         | MapStruct 1.6.3                               |
| Build         | Maven 3.9 + Maven Wrapper                     |
| Contenedores  | Docker (multi-stage build, imagen Alpine)     |
| Orquestación  | Docker Compose                                |

---

## Variables de entorno

Copiar `.env.example` a `.env` y completar los valores:

```bash
cp .env.example .env
```

| Variable                          | Descripción                                          |
|-----------------------------------|------------------------------------------------------|
| `RAPIDAPI_KEY`                    | Clave de acceso a API-Football (RapidAPI)            |
| `POSTGRES_AUTH_DB`                | Nombre de la base de datos de autenticación          |
| `POSTGRES_AUTH_USER`              | Usuario de la BD de autenticación                    |
| `POSTGRES_AUTH_PASSWORD`          | Contraseña de la BD de autenticación                 |
| `POSTGRES_NOTIFICATIONS_DB`       | Nombre de la BD de notificaciones                    |
| `POSTGRES_NOTIFICATIONS_USER`     | Usuario de la BD de notificaciones                   |
| `POSTGRES_NOTIFICATIONS_PASSWORD` | Contraseña de la BD de notificaciones                |
| `JWT_SECRET`                      | Clave secreta Base64 para firmar JWT (ver nota)      |
| `JWT_EXPIRATION`                  | Tiempo de expiración en milisegundos (ej: `3600000`) |

> **Generar JWT_SECRET:** `openssl rand -base64 32` desde bash

---

## Puesta en marcha

### Requisitos previos

- Docker y Docker Compose instalados
- Archivo `.env` configurado (ver sección anterior)

### Levantar todos los servicios

```bash
docker compose up --build
```

### Levantar servicios individuales

```bash
docker compose up ms-gateway ms-auth postgres-auth redis
```

### Parar y limpiar

```bash
docker compose down          # parar contenedores
docker compose down -v       # parar y eliminar volúmenes
```

---

## Endpoints disponibles (a través del gateway)

El gateway escucha en `http://localhost:8080`.

### Autenticación (públicos)

    POST /api/auth/register Registro de nuevo usuario
    POST /api/auth/login Login → devuelve JWT

**Ejemplo de registro:**

```json
POST /api/auth/register
{
  "username": "usuario1",
  "email": "usuario@ejemplo.com",
  "password": "Contraseña1"
}
```

**Ejemplo de login:**

```json
POST /api/auth/login
{
  "email": "usuario@ejemplo.com",
  "password": "Contraseña1"
}
```

### Rutas protegidas (requieren `Authorization: Bearer <token>`)

    GET /api/leagues/**          Información de ligas
    GET /api/teams/**            Información de equipos
    GET /api/fixtures/**         Partidos y resultados
    GET /api/standings/**        Clasificaciones
    GET /api/notifications/**    Notificaciones y suscripciones
    GET /api/dashboard/**        Resumen ejecutivo

---

## Comunicación entre servicios

| Consumidor         | Proveedor                                   | Propósito                               |
|--------------------|---------------------------------------------|-----------------------------------------|
| `ms-fixtures`      | `ms-teams`                                  | Enriquecer fixtures con datos de equipo |
| `ms-standings`     | `ms-teams`                                  | Enriquecer clasificaciones              |
| `ms-notifications` | `ms-fixtures`                               | Monitorizar partidos para alertas       |
| `ms-dashboard`     | `ms-fixtures`, `ms-standings`, `ms-leagues` | Resumen agregado                        |