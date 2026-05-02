# SportPulse — Plataforma de Microservicios de Fútbol

![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.13-6DB33F?logo=springboot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2025.0.2-6DB33F?logo=spring&logoColor=white)
![Build](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Container-Docker-2496ED?logo=docker&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL_15-4169E1?logo=postgresql&logoColor=white)

**Equipo:** Equipo 10   
**Arquitectura:** Microservicios  
**API Externa:** [API-Football v3](https://www.api-football.com/)

---

## Descripción

SportPulse es una plataforma backend de análisis de fútbol en tiempo real. Está construida sobre una arquitectura de
microservicios que se comunican entre sí para recuperar, procesar y exponer datos de la API-Football (ligas, equipos,
partidos, clasificaciones), proporcionando métricas y eventos futbolísticos con caché integrada y autenticación JWT
distribuida.

---

## Microservicios

| Servicio           | Puerto | Base de datos | Responsabilidad                                        |
|--------------------|--------|---------------|--------------------------------------------------------|
| `ms-gateway`       | 8080   | Redis         | Punto de entrada único, enrutamiento, JWT y rate limit |
| `ms-auth`          | 8081   | PostgreSQL    | Registro, login y emisión de tokens JWT                |
| `ms-leagues`       | 8082   | —             | Ligas, países y temporadas (API-Football)              |
| `ms-teams`         | 8083   | —             | Equipos, escudos e información general (API-Football)  |
| `ms-fixtures`      | 8085   | —             | Partidos, resultados, eventos en vivo (API-Football)   |
| `ms-standings`     | 8086   | —             | Clasificaciones por liga y temporada                   |
| `ms-notifications` | 8088   | PostgreSQL    | Suscripciones y alertas de eventos                     |
| `ms-dashboard`     | 8089   | —             | Resumen ejecutivo agregado                             |

---

## Arquitectura

```
Cliente
  │
  ▼
ms-gateway (:8080) ──── Redis (rate limiting)
  │  JWT validation + header propagation (X-User-Id, X-User-Role)
  │  Circuit Breaker per route (Resilience4j)
  │
  ├── ms-auth (:8081)
  ├── ms-leagues (:8082)
  ├── ms-teams (:8083)
  ├── ms-fixtures (:8085)
  ├── ms-standings (:8086)
  ├── ms-notifications (:8088)
  └── ms-dashboard (:8089)
```

---

## Estado de implementación

| Servicio           | Estado         | Detalle                                                                       |
|--------------------|----------------|-------------------------------------------------------------------------------|
| `ms-gateway`       | ✅ Completo     | Enrutamiento, JWT filter, rate limiting Redis, circuit breaker, health checks |
| `ms-auth`          | ✅ Completo     | Registro/login con validaciones, JWT HS256 (JJWT 0.12.6), PostgreSQL, BCrypt  |
| `ms-leagues`       | ✅ Implementado | Feign → API-Football, Caffeine cache, mapper, endpoints GET /leagues          |
| `ms-teams`         | ✅ Implementado | Feign → API-Football, Caffeine cache (`teams`, `teamDetail`), mapper          |
| `ms-fixtures`      | ✅ Implementado | Feign → API-Football + ms-teams, `TeamLogoService`, live matches, events      |
| `ms-standings`     | 🔧 En Proceso  | Estructura base + Feign client API-Football; lógica de negocio pendiente      |
| `ms-notifications` | 🔧 En Proceso  | Estructura base + PostgreSQL; lógica pendiente                                |
| `ms-dashboard`     | 🔧 En Proceso  | Estructura base; agregación de servicios pendiente                            |

---

## Stack tecnológico

| Capa          | Tecnología                                    |
|---------------|-----------------------------------------------|
| Lenguaje      | Java 17                                       |
| Framework     | Spring Boot 3.5.13                            |
| Cloud         | Spring Cloud 2025.0.2                         |
| Gateway       | Spring Cloud Gateway (WebFlux reactivo)       |
| Seguridad     | Spring Security + JJWT 0.12.6 (HS256)         |
| Resiliencia   | Resilience4j (Circuit Breaker + Time Limiter) |
| Rate Limiting | Redis 7 + RedisRateLimiter                    |
| HTTP client   | OpenFeign                                     |
| Caché         | Caffeine                                      |
| Persistencia  | Spring Data JPA + PostgreSQL 15               |
| Mapeo         | MapStruct 1.6.3                               |
| Build         | Maven 3.9 + Maven Wrapper                     |
| Contenedores  | Docker (multi-stage, Alpine) + Docker Compose |

---

## Variables de entorno

Copiar `.env.example` a `.env` y completar los valores antes de levantar los servicios:

```bash
cp .env.example .env
```

| Variable                          | Descripción                                                   |
|-----------------------------------|---------------------------------------------------------------|
| `API_KEY`                         | Clave de acceso a API-Football (RapidAPI)                     |
| `JWT_SECRET`                      | Clave secreta Base64 para firmar JWT (compartida entre todos) |
| `JWT_EXPIRATION`                  | Expiración del token en milisegundos (ej. `3600000` = 1h)     |
| `POSTGRES_AUTH_DB`                | Nombre de la BD de autenticación                              |
| `POSTGRES_AUTH_USER`              | Usuario de la BD de autenticación                             |
| `POSTGRES_AUTH_PASSWORD`          | Contraseña de la BD de autenticación                          |
| `POSTGRES_NOTIFICATIONS_DB`       | Nombre de la BD de notificaciones                             |
| `POSTGRES_NOTIFICATIONS_USER`     | Usuario de la BD de notificaciones                            |
| `POSTGRES_NOTIFICATIONS_PASSWORD` | Contraseña de la BD de notificaciones                         |

> **Generar JWT_SECRET:** `openssl rand -base64 32` - puede usarse git bash.

---

## Puesta en marcha

### Requisitos previos

- Docker y Docker Compose instalados
- Archivo `.env` configurado (ver sección anterior)

### Levantar todos los servicios

```bash
docker-compose up -d --build
```

### Levantar servicios individuales

```bash
# Solo gateway + auth
docker-compose up ms-gateway ms-auth postgres-auth redis

# Gateway + servicios de datos
docker-compose up ms-gateway ms-auth ms-leagues ms-teams ms-fixtures postgres-auth redis
```

### Parar y limpiar

```bash
docker compose down        # parar contenedores
docker compose down -v     # parar y eliminar volúmenes (borra datos de BD)
```

---

## Endpoints disponibles

El gateway escucha en `http://localhost:8080`. Todas las rutas protegidas requieren el header
`Authorization: Bearer <token>`.

### Autenticación (públicas)

| Método | Ruta                 | Descripción               |
|--------|----------------------|---------------------------|
| POST   | `/api/auth/register` | Registro de nuevo usuario |
| POST   | `/api/auth/login`    | Login → devuelve JWT      |

---

### Ligas `🔒`

| Método | Ruta                      | Params                | Descripción                |
|--------|---------------------------|-----------------------|----------------------------|
| GET    | `/api/leagues`            | `country?`, `season?` | Lista de ligas (filtrable) |
| GET    | `/api/leagues/{leagueId}` | —                     | Detalle de una liga        |

---

### Equipos `🔒`

| Método | Ruta                  | Params                           | Descripción                       |
|--------|-----------------------|----------------------------------|-----------------------------------|
| GET    | `/api/teams`          | `league` (req.), `season` (req.) | Equipos de una liga por temporada |
| GET    | `/api/teams/{teamId}` | —                                | Detalle completo de un equipo     |

---

### Partidos `🔒`

| Método | Ruta                               | Params                                 | Descripción                   |
|--------|------------------------------------|----------------------------------------|-------------------------------|
| GET    | `/api/fixtures`                    | `league?`, `team?`, `date?`, `status?` | Lista de partidos (filtrable) |
| GET    | `/api/fixtures/live`               | —                                      | Partidos en vivo              |
| GET    | `/api/fixtures/{fixtureId}/events` | —                                      | Eventos de un partido         |

Valores de `status`: `NS` (por jugar), `LIVE` (en vivo), `FT` (finalizado). Sin filtros de liga/equipo, `date` por
defecto es dia actual.

---

### Clasificaciones `🔒`

| Método | Ruta                | Descripción                                      |
|--------|---------------------|--------------------------------------------------|
| GET    | `/api/standings/**` | Clasificación por liga/temporada (en desarrollo) |

---

### Notificaciones `🔒`

| Método | Ruta                    | Descripción                             |
|--------|-------------------------|-----------------------------------------|
| GET    | `/api/notifications/**` | Suscripciones y alertas (en desarrollo) |

---

### Dashboard `🔒`

| Método | Ruta             | Descripción                               |
|--------|------------------|-------------------------------------------|
| GET    | `/api/dashboard` | Health check del servicio (en desarrollo) |

---

## Comunicación entre servicios

Todos los llamados inter-servicio se realizan mediante OpenFeign con `FeignAuthInterceptor`, que propaga el JWT del
contexto de seguridad.

| Consumidor         | Proveedor                                   | Propósito                                      |
|--------------------|---------------------------------------------|------------------------------------------------|
| `ms-fixtures`      | `ms-teams` (via `MsTeamsClient`)            | Enriquecer fixtures con logo del equipo        |
| `ms-standings`     | `ms-teams`                                  | Enriquecer clasificaciones con datos de equipo |
| `ms-notifications` | `ms-fixtures`                               | Monitorizar partidos para disparar alertas     |
| `ms-dashboard`     | `ms-fixtures`, `ms-standings`, `ms-leagues` | Resumen ejecutivo agregado                     |

---

## Gateway: resiliencia y rate limiting

- **Rate limiting:** 60 requests/minuto por IP usando `RedisRateLimiter`
- **Circuit Breaker** (Resilience4j) por ruta:
    - Ventana deslizante: 10 llamadas
    - Apertura al 50% de fallos (mínimo 3 llamadas)
    - Recuperación automática tras 10 segundos
    - Timeout por petición: 5 segundos
- **Fallback:** respuesta JSON estructurada con código `SERVICE_UNAVAILABLE` cuando el circuito está abierto

---

## Health check

El gateway expone endpoint de salud de los microservicios:

```
GET http://localhost:8080/health
```

Cada microservicio expone su propio `/api/actuator/health`.

---

## Equipo de desarrollo

| Integrante | GitHub                                       |
|------------|----------------------------------------------|
| BeckanG728 | [@BeckanG728](https://github.com/BeckanG728) |
| owenwilson | [@owenwilson](https://github.com/owenwilson) |

<br>

---

<div align="center">

![BytesColaborativos](https://img.shields.io/badge/BytesColaborativos-Equipo_10-4A90D9?style=for-the-badge)

</div>
