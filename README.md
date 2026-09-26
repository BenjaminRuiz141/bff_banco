# Arquitectura de Microservicios y Seguridad en la Nube con Spring Cloud

**Asignatura:** Desarrollo Backend III (PBY2203 - Semana 6)  
**Institución:** DUOC UC  
**Dominio:** Banca Digital & Canales Backend for Frontend (BFF)

---

## 1. Descripción de la Arquitectura

Este proyecto implementa una solución de **microservicios bancarios en la nube** utilizando **Spring Boot 3.4.1** y **Spring Cloud 2024.0.0 (Moorgate)**. La arquitectura desacopla la infraestructura del dominio bancario mediante los siguientes patrones y servicios:

1. **Configuración Centralizada (Spring Cloud Config Server):** Servidor centralizado (`config-server`, puerto `8888`) que gestiona las propiedades de configuración en modo `native` desde un repositorio central (`config-repo/`). Los microservicios consumen dinámicamente sus credenciales de base de datos, puertos y umbrales de resiliencia al arrancar.
2. **Descubrimiento de Servicios (Netflix Eureka Server):** Servidor de registro y descubrimiento (`eureka-server`, puerto `8761`) donde los microservicios se autorregistran con latidos de vida (*heartbeats*) y reportan estado de salud `UP`.
3. **Ecosistema de Microservicios de Negocio (3 Microservicios BFF):**
   - **`bff-web` (Puerto 8081):** Canal Web para navegadores de escritorio.
   - **`bff-mobile` (Puerto 8082):** Canal Móvil optimizado para bajo consumo de datos.
   - **`bff-atm` (Puerto 8083):** Canal Cajeros Automáticos para operaciones transaccionales y de retiro.
4. **Tolerancia a Fallos (Resilience4j Circuit Breaker & Fallback):** Protección contra caídas en cascada mediante `@CircuitBreaker` y métodos `@Fallback` ante fallas de servicios externos o alta latencia, con monitorización en tiempo real vía Spring Boot Actuator.
5. **Seguridad Integral (Spring Security 6 & JWT):** Arquitectura *stateless* protegida por JSON Web Tokens (JJWT), con autorización por roles (RBAC) y control de acceso estricto con códigos `401 Unauthorized` y `403 Forbidden`.

---

## 2. Diagrama Arquitectónico

```mermaid
graph TD
    subgraph "Servidores de Infraestructura Spring Cloud"
        CS["Spring Cloud Config Server\n(Puerto 8888)"]
        ES["Netflix Eureka Server\n(Puerto 8761)"]
    end

    subgraph "Repositorio Central de Configuraciones"
        REPO[("config-repo/\n- application.yml\n- bff-web.yml\n- bff-mobile.yml\n- bff-atm.yml")]
    end

    subgraph "Microservicios Registrados en Eureka (UP)"
        BW["bff-web :8081\n(ROLE_WEB)"]
        BM["bff-mobile :8082\n(ROLE_MOBILE)"]
        BA["bff-atm :8083\n(ROLE_ATM)"]
    end

    subgraph "Tolerancia a Fallos (Resilience4j)"
        EXT_DIV["API Divisas Externa\n(Banco Central / UF / USD)"]
        EXT_FCM["Gateway Push Móvil\n(Firebase Cloud Messaging)"]
        EXT_FRAUD["Motor Central Antifraude"]
    end

    REPO -->|Lectura native| CS
    CS -.->|Configura| ES
    CS -->|Inyecta propiedades| BW
    CS -->|Inyecta propiedades| BM
    CS -->|Inyecta propiedades| BA

    BW -->|Heartbeat UP| ES
    BM -->|Heartbeat UP| ES
    BA -->|Heartbeat UP| ES

    BW -->|@CircuitBreaker + Fallback| EXT_DIV
    BM -->|@CircuitBreaker + Fallback| EXT_FCM
    BA -->|@CircuitBreaker + Fallback| EXT_FRAUD
```

---

## 3. Matriz de Servicios, Puertos y Roles

| Microservicio | Puerto | Tipo de Servicio | Registro Eureka | Rol de Seguridad | Función Principal |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **`config-server`** | `8888` | Infraestructura | N/A | Público | Servidor central de configuraciones (`spring-cloud-config-server`) |
| **`eureka-server`** | `8761` | Infraestructura | Host (8761) | Público | Service Registry & Discovery Dashboard (`@EnableEurekaServer`) |
| **`bff-web`** | `8081` | Negocio / BFF | **`UP`** (`BFF-WEB`) | `ROLE_WEB` | Canal Web, DTO completo, valorización multimoneda con Circuit Breaker |
| **`bff-mobile`** | `8082` | Negocio / BFF | **`UP`** (`BFF-MOBILE`) | `ROLE_MOBILE` | Canal Móvil, DTO ligero, notificaciones push con Circuit Breaker |
| **`bff-atm`** | `8083` | Negocio / BFF | **`UP`** (`BFF-ATM`) | `ROLE_ATM` | Canal Cajero, DTO operacional, retiros y validación de riesgo con Circuit Breaker |

---

## 4. Estructura del Directorio

```text
bff_proyecto/
├── pom.xml                  # POM raíz agregador de compilación Maven
├── config-repo/             # Directorio central de configuraciones YAML
│   ├── application.yml      # Configuración compartida (Eureka, Actuator, Resilience4j)
│   ├── bff-web.yml          # Propiedades de bff-web (puerto 8081, DB, circuit breaker)
│   ├── bff-mobile.yml       # Propiedades de bff-mobile (puerto 8082, DB, circuit breaker)
│   └── bff-atm.yml          # Propiedades de bff-atm (puerto 8083, DB, circuit breaker)
├── config-server/           # Servidor de configuración centralizada (8888)
├── eureka-server/           # Servidor de descubrimiento de servicios Eureka (8761)
├── bff-web/                 # Microservicio Canal Web (8081)
├── bff-mobile/              # Microservicio Canal Móvil (8082)
└── bff-atm/                 # Microservicio Canal Cajeros (8083)
```

---

## 5. Compilación y Orden de Inicio

### Compilación Unificada
Desde la raíz del proyecto (`bff_proyecto/`), compilar todos los módulos simultáneamente:
```bash
mvn clean compile
```

### Orden Estricto de Arranque
Para asegurar que los microservicios obtengan sus configuraciones y se registren correctamente, inicie los servicios en terminales independientes en este orden:

#### Paso 1: Iniciar Servidor de Configuración (Puerto 8888)
```bash
cd config-server
mvn spring-boot:run
```
*(Esperar a que finalice la carga de `ConfigServerApplication`).*

#### Paso 2: Iniciar Servidor Eureka (Puerto 8761)
```bash
cd eureka-server
mvn spring-boot:run
```
*(Verificar dashboard en el navegador: `http://localhost:8761`).*

#### Paso 3: Iniciar los 3 Microservicios
- **Terminal 3 (Canal Web):**
  ```bash
  cd bff-web
  mvn spring-boot:run
  ```
- **Terminal 4 (Canal Móvil):**
  ```bash
  cd bff-mobile
  mvn spring-boot:run
  ```
- **Terminal 5 (Canal Cajero):**
  ```bash
  cd bff-atm
  mvn spring-boot:run
  ```

---

## 6. Guía de Pruebas y Validación de Criterios

### Criterio 1: Spring Cloud Config Server
Validar que el servidor de configuración sirve las propiedades centralizadas:
```bash
# Consultar configuraciones del microservicio bff-web
curl -s http://localhost:8888/bff-web/default

# Consultar configuraciones compartidas globales
curl -s http://localhost:8888/application/default
```
**Respuesta esperada:** JSON con `propertySources` conteniendo `bff-web.yml` y `application.yml`.

---

### Criterio 2: Service Discovery con Netflix Eureka
Validar que los **3 microservicios** se encuentran registrados con estado **`UP`**:

1. **Vía Navegador Web:** Ingrese a [http://localhost:8761](http://localhost:8761).  
   En la sección *"Instances currently registered with Eureka"*, se observarán:
   - **`BFF-WEB`** en `8081` (Status: `UP`)
   - **`BFF-MOBILE`** en `8082` (Status: `UP`)
   - **`BFF-ATM`** en `8083` (Status: `UP`)

2. **Vía API REST:**
   ```bash
   curl -s -H "Accept: application/json" http://localhost:8761/eureka/apps
   ```
   **Respuesta esperada:** JSON con `apps__hashcode: "UP_3_"` y las instancias de los 3 microservicios.

---

### Criterio 3: Tolerancia a Fallos con Resilience4j (`@CircuitBreaker` y `@Fallback`)

Cada microservicio dispone de endpoints protegidos por resiliencia y endpoints de prueba inmediata (`test-fallback`):

#### 1. Canal Web (`bff-web` - Puerto 8081):
- **Ejecución Normal (Circuito CLOSED):**
  ```bash
  curl -s "http://localhost:8081/api/web/public/circuit-breaker/test?cuentaId=1&fail=false"
  ```
  *Respuesta:* `200 OK` con estado `"estadoCircuito": "CLOSED (Llamada Externa Exitosa)"` y conversión a USD.

- **Ejecución Fallback (Contingencia activada ante caída):**
  ```bash
  curl -s "http://localhost:8081/api/web/public/circuit-breaker/test?cuentaId=1&fail=true"
  ```
  *Respuesta:* `200 OK` controlado con `"estadoCircuito": "FALLBACK_ACTIVADO (Resilience4j Contingencia)"` y mensaje informativo de contingencia sin error 500.

- **Monitoreo de Estado en Actuator:**
  ```bash
  curl -s http://localhost:8081/actuator/circuitbreakers
  ```
  *Respuesta:* JSON de Actuator reflejando estado `CLOSED`/`OPEN`, número de llamadas fallidas y umbrales.

#### 2. Canal Cajero (`bff-atm` - Puerto 8083):
- **Ejecución Normal:**
  ```bash
  curl -s "http://localhost:8083/api/atm/public/circuit-breaker/test?cuentaId=1&monto=30000&fail=false"
  ```
- **Disparo de Fallback (Retiro pre-autorizado en contingencia):**
  ```bash
  curl -s "http://localhost:8083/api/atm/public/circuit-breaker/test?cuentaId=1&monto=30000&fail=true"
  ```

#### 3. Canal Móvil (`bff-mobile` - Puerto 8082):
- **Ejecución Normal:**
  ```bash
  curl -s "http://localhost:8082/api/mobile/public/circuit-breaker/test?cuentaId=1&fail=false"
  ```
- **Disparo de Fallback (Encolamiento en SMS de contingencia):**
  ```bash
  curl -s "http://localhost:8082/api/mobile/public/circuit-breaker/test?cuentaId=1&fail=true"
  ```

---

### Criterio 4: Seguridad (Spring Security & JWT)

#### 1. Obtención de Token JWT:
```bash
# Token para Canal Web (ROLE_WEB)
curl -s "http://localhost:8081/api/auth/token?usuario=profesor&rol=ROLE_WEB"
```
*(Copie el valor del campo `token` para las peticiones siguientes).*

#### 2. Petición Exitosa (200 OK con Token y Rol Válido):
```bash
curl -i -H "Authorization: Bearer <TOKEN_WEB>" "http://localhost:8081/api/web/cuentas/1/indicadores"
```
**Respuesta:** `HTTP/1.1 200 OK` con los datos financieros.

#### 3. Petición Rechazada con 401 Unauthorized (Sin Token o Token Inválido):
```bash
curl -i "http://localhost:8081/api/web/cuentas/1/indicadores"
```
**Respuesta esperada:**
```http
HTTP/1.1 401 Unauthorized
Content-Type: application/json

{"error": "Unauthorized", "mensaje": "Acceso no autorizado: Token JWT ausente, invalido o expirado"}
```

#### 4. Petición Rechazada con 403 Forbidden (Rol Incorrecto - RBAC):
Generar un token con rol ajeno (`ROLE_ATM`):
```bash
curl -s "http://localhost:8081/api/auth/token?usuario=profesor&rol=ROLE_ATM"
```
Intentar consumir el endpoint protegido del Canal Web (`/api/web/**`) con dicho token:
```bash
curl -i -H "Authorization: Bearer <TOKEN_ATM>" "http://localhost:8081/api/web/cuentas/1/indicadores"
```
**Respuesta esperada:**
```http
HTTP/1.1 403 Forbidden
Content-Type: application/json

{"error": "Forbidden", "mensaje": "Acceso denegado: El token no posee la autoridad ROLE_WEB requerida para este canal"}
```
