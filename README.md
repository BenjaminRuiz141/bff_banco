# Arquitectura Backend for Frontend (BFF) - Banca Digital

Proyecto desarrollado para la asignatura de Desarrollo Backend III (Semana 5, DUOC UC).

## Descripcion del Proyecto

El sistema implementa el patron arquitectonico Backend for Frontend (BFF) para una entidad bancaria. Esta compuesto por tres microservicios Spring Boot totalmente independientes que consumen una base de datos MySQL compartida (`batchdb`), asegurados mediante HTTPS (TLS) y control de acceso basado en roles (RBAC) con Spring Security.

## Justificacion del Patron BFF

En lugar de utilizar una API monolitica y generica, el patron BFF permite adaptar el contrato de datos a las necesidades reales de cada cliente:

- **Canal Web:** Entrega informacion completa del cliente y cuenta (`cuentaId`, `nombre`, `edad`, `saldo`, `tipo`) para navegacion en pantalla amplia.
- **Canal Movil:** Entrega un DTO ligero (`cuentaId`, `nombre`, `saldo`, `tipo`), omitiendo la edad para reducir el consumo de ancho de banda y latencia en dispositivos celulares.
- **Canal Cajeros (ATM):** Entrega exclusivamente datos operacionales (`cuentaId`, `saldo`, `tipo`), protegiendo la privacidad del titular en la via publica (sin nombre ni edad), e incorpora operaciones de retiro de efectivo (`POST /retiro`).

## Estructura del Proyecto

```text
bff_proyecto/
├── bff-web/        # Servicio Web (Puerto 8081)
│   ├── src/main/java/cl/duoc/bff/web/ (config, controller, dto, entity, repository, service)
│   ├── src/main/resources/ (application.properties, keystore.p12)
│   └── pom.xml
├── bff-mobile/     # Servicio Móvil (Puerto 8082)
│   ├── src/main/java/cl/duoc/bff/mobile/ (config, controller, dto, entity, repository, service)
│   ├── src/main/resources/ (application.properties, keystore.p12)
│   └── pom.xml
└── bff-atm/        # Servicio Cajeros Automáticos (Puerto 8083)
    ├── src/main/java/cl/duoc/bff/atm/ (config, controller, dto, entity, repository, service)
    ├── src/main/resources/ (application.properties, keystore.p12)
    └── pom.xml
```

## Mapa de Servicios, Puertos y Roles

| Servicio | Puerto | Rol Requerido | Endpoint Base | Enfoque DTO |
| :--- | :--- | :--- | :--- | :--- |
| `bff-web` | 8081 (HTTPS) | `ROLE_WEB` | `GET /api/web/cuentas/{cuentaId}` | Completo: `CuentaWebDTO` (`cuentaId`, `nombre`, `edad`, `saldo`, `tipo`) |
| `bff-mobile` | 8082 (HTTPS) | `ROLE_MOBILE` | `GET /api/mobile/cuentas/{cuentaId}` | Ligero: `CuentaMobileDTO` (`cuentaId`, `nombre`, `saldo`, `tipo`) |
| `bff-atm` | 8083 (HTTPS) | `ROLE_ATM` | `GET /api/atm/cuentas/{cuentaId}`<br>`POST /api/atm/cuentas/{cuentaId}/retiro` | Transaccional: `CuentaAtmDTO` (`cuentaId`, `saldo`, `tipo`) |

## Requisitos Tecnicos

- Java 17 o superior.
- Spring Boot 3.4.x / Spring Security 6 / Spring Data JPA.
- MySQL Server 8.x en puerto 3306 (Base de datos: `batchdb`, tabla: `interes`).
- Certificado SSL PKCS12 (`keystore.p12`) configurado localmente en cada servicio.

## Compilacion y Ejecucion

### Compilacion:
mvn clean compile -f bff-web/pom.xml
mvn clean compile -f bff-mobile/pom.xml
mvn clean compile -f bff-atm/pom.xml

### Ejecucion (en terminales independientes):
# Terminal 1: Canal Web
cd bff-web
mvn spring-boot:run

# Terminal 2: Canal Movil
cd bff-mobile
mvn spring-boot:run

# Terminal 3: Canal ATM
cd bff-atm
mvn spring-boot:run
