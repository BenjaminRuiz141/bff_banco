# Ecosistema Bancario de Microservicios BFF

Arquitectura de microservicios bancarios desarrollada con **Spring Boot 3.4.1**, **Spring Cloud 2024.0.0**, **Apache ActiveMQ Artemis**, **Resilience4j** y **Spring Security (OAuth2 / JWT)**.

---

## 1. Servicios y Puertos

| Componente | Puerto | Tipo | Función Principal | Credenciales / Acceso |
| :--- | :--- | :--- | :--- | :--- |
| **ActiveMQ Artemis** | `61616` / `8161` | Middleware | Broker JMS & Consola Web de Colas | `admin` / `admin` |
| **Config Server** | `8888` | Infraestructura | Repositorio Central de Configuración | Público |
| **Eureka Server** | `8761` | Infraestructura | Service Discovery & Health Dashboard | [http://localhost:8761](http://localhost:8761) |
| **auth-service** | `8084` | Seguridad & OAuth2 | Autenticación con GitHub OAuth2 y Emisión JWT | [http://localhost:8084](http://localhost:8084) |
| **bff-web** | `8081` | Microservicio | Canal Web, Libro Mayor y Cartola Contable | `ROLE_WEB` |
| **bff-mobile** | `8082` | Microservicio | Canal Móvil y Notificaciones Push | `ROLE_MOBILE` |
| **bff-atm** | `8083` | Microservicio | Canal Cajero, Retiros y Transacciones Saga | `ROLE_ATM` |

---

## 2. Arquitectura del Sistema

![Arquitectura de Eventos](docs/arquitectura-eventos.png)

El ecosistema implementa comunicación asíncrona mediante el **Patrón Saga (coreografiado)** sobre **Apache ActiveMQ Artemis** con tolerancia a fallos vía **Resilience4j**:

- **`banco.saga.transacciones.queue`**: Eventos de transacciones exitosas (Retiros ATM, Transferencias Web). Consumida por `bff-web` (actualización de saldos) y `bff-mobile` (notificaciones push).
- **`banco.saga.compensaciones.queue`**: Eventos de compensación/reversa automática ante fallos transaccionales (ej. fallas en hardware dispensador ATM).
- **Tolerancia a fallos**: Mecanismos de *Circuit Breaker* y *Retry* protegen la comunicación con el broker JMS y APIs externas.

> Diagrama fuente en formato Mermaid disponible en [`docs/arquitectura-eventos.mmd`](docs/arquitectura-eventos.mmd).

---

## 3. Configuración Inicial (.env)

Crea el archivo `.env` a partir de la plantilla:

```bash
cp .env.example .env
```

Variables requeridas en `.env`:
```env
GITHUB_CLIENT_ID=tu_github_client_id
GITHUB_CLIENT_SECRET=tu_github_client_secret
JWT_SECRET=BancoDuocUcBffSecretKey2026SecureJwtTokenKeyMustBe256BitsLong!
AUTH_PORT=8084
```

---

## 4. Despliegue del Ecosistema

### Opción A: Orquestación con Docker Compose (Recomendada)

> [!NOTE]
> **Base de Datos MySQL Local**: Los contenedores se conectan a la base de datos `batchdb` en el host mediante `host.docker.internal:3306`.

```bash
# 1. Compilar los paquetes JAR de todos los microservicios:
mvn clean package -DskipTests

# 2. Construir imágenes y levantar contenedores:
docker compose up --build -d

# 3. Verificar estado de los contenedores:
docker compose ps

# 4. Detener el ecosistema:
docker compose down
```

### Opción B: Ejecución Local en Desarrollo (Terminales separadas)

```bash
# 1. Iniciar Broker ActiveMQ en Docker:
docker compose up -d activemq-artemis

# 2. Iniciar microservicios en orden:
cd config-server && mvn spring-boot:run
cd eureka-server && mvn spring-boot:run
cd auth-service  && mvn spring-boot:run
cd bff-web       && mvn spring-boot:run
cd bff-mobile    && mvn spring-boot:run
cd bff-atm       && mvn spring-boot:run
```

---