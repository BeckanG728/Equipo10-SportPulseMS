## SportPulse — Plataforma de Microservicios de Fútbol

![Java](https://img.shields.io/badge/Java-17+-ED8B00?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=springboot)
![Microservices](https://img.shields.io/badge/Architecture-Microservices-orange)
![Build](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Container-Docker-2496ED?logo=docker&logoColor=white)
![Database](https://img.shields.io/badge/Database-PostgreSQL-4169E1?logo=postgresql&logoColor=white)

**Equipo:** Equipo 07  
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