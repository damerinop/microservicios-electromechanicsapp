# Electromechanics Backend

API REST para el sistema móvil de registro de jornada y seguimiento de tareas de mantención de Electromechanics Ingeniería SpA. Es el backend de la app Android (roles Técnico y Supervisor) y está formado por tres microservicios Spring Boot que comparten una base PostgreSQL, todo orquestado con Docker Compose.

## Arquitectura

```
App Android ──HTTP──► empleados-api    (:8084) ─┐
                 ├──► actividades-api  (:8088) ─┼──► PostgreSQL (red interna de Docker)
                 └──► jornada-api      (:8086) ─┘
```

| Servicio | Carpeta | Puerto externo | Responsabilidad |
|---|---|---|---|
| empleados-api | `empleados/` | 8084 | Cuentas de técnicos y supervisores |
| actividades-api | `actividades/` | 8088 | Órdenes de trabajo y bitácora de actividades con evidencia fotográfica |
| jornada-api | `registrarjornada/` | 8086 | Marcaciones de entrada y salida con GPS |

Cada contenedor escucha en el puerto 8080 internamente. PostgreSQL no se publica al exterior.

## Tecnologías

- Java 21
- Spring Boot 4.0.9 (Web MVC, Data JPA)
- PostgreSQL 17
- Lombok
- JUnit 5 y Mockito
- Docker y Docker Compose

## Endpoints

### Empleados (`:8084`)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/empleados` | Lista los empleados |
| GET | `/api/empleados/{id}` | Obtiene un empleado |
| POST | `/api/empleados` | Crea un empleado (rol por defecto `TECNICO`) |

### Órdenes de trabajo (`:8088`)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/ordenes-trabajo` | Lista las órdenes |
| GET | `/api/ordenes-trabajo/{id}` | Obtiene una orden |
| POST | `/api/ordenes-trabajo` | Crea una orden (estado `Pendiente` y fecha automáticos) |
| PATCH | `/api/ordenes-trabajo/{id}/estado` | Cambia el estado. Body: `{"estado":"En Proceso"}` |

### Actividades (`:8088`)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/actividades` | Lista las actividades |
| GET | `/api/actividades/{id}` | Obtiene una actividad |
| POST | `/api/actividades` | Registra una actividad (fecha automática) |

### Jornada (`:8086`)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/registrarjornadas` | Lista las marcaciones |
| GET | `/api/registrarjornadas/{id}` | Obtiene una marcación |
| POST | `/api/registrarjornadas` | Registra una marcación (fecha automática) |

### Ejemplos de body

Empleado:

```json
{
  "rutTrabajador": "11111111-1",
  "nombreCompleto": "Supervisor Demo",
  "rol": "SUPERVISOR",
  "especialidadTecnica": "Electromecánica",
  "correoCorporativo": "sup@demo.cl",
  "contrasena": "supervisor1234"
}
```

Orden de trabajo:

```json
{
  "idSupervisor": "UUID-del-supervisor",
  "idTecnicoAsignado": "UUID-del-tecnico",
  "cliente": "Industria Manufacturera S.A.",
  "tarea": "Mantención preventiva de tablero eléctrico"
}
```

Marcación de jornada:

```json
{
  "idEmpleado": "UUID-del-tecnico",
  "tipoMarcacion": "ENTRADA",
  "latitudMarcacion": -33.5123,
  "longitudMarcacion": -70.7561
}
```

`evidenciaFotografica` en una actividad es una imagen JPEG codificada en Base64 (columna `TEXT`).

## Estructura del repositorio

```
.
├── docker-compose.yml
├── .env.example
├── empleados/
├── actividades/
└── registrarjornada/
```

Cada microservicio tiene su `pom.xml`, su `Dockerfile` (compilación en dos etapas) y su carpeta `src`.

## Variables de entorno

Se definen en un archivo `.env` en la raíz, que no se versiona. Se crea a partir de `.env.example`.

| Variable | Descripción |
|---|---|
| `DB_USER` | Usuario de PostgreSQL |
| `DB_PASSWORD` | Contraseña de PostgreSQL |

Los servicios leen además `PORT`, `DB_HOST`, `DB_PORT`, `DB_NAME`, `DDL_AUTO` y `SHOW_SQL`, que `docker-compose.yml` ya define.

## Ejecución local con Docker

Requisitos: Docker Desktop.

```bash
cp .env.example .env
docker compose up -d --build
docker compose ps
```

Prueba rápida:

```bash
curl http://localhost:8084/api/empleados
curl http://localhost:8088/api/ordenes-trabajo
curl http://localhost:8086/api/registrarjornadas
```

Cada una debe responder `[]` en una base nueva. Para ver los logs: `docker compose logs -f empleados-api`. Para detener todo: `docker compose down` (agregar `-v` borra también los datos).

## Pruebas unitarias

Las pruebas usan JUnit 5 y Mockito. No necesitan base de datos ni contexto de Spring.

```bash
cd empleados && ./mvnw test
cd ../actividades && ./mvnw test
cd ../registrarjornada && ./mvnw test
```

En Windows se usa `.\mvnw.cmd test`.

| Servicio | Clase de prueba | Qué verifica |
|---|---|---|
| empleados | `EmpleadoServiceImplTest` | Rol `TECNICO` por defecto, conservación de `SUPERVISOR`, consultas |
| empleados | `EmpleadoControllerTest` | Códigos 200, 201 y 404 |
| actividades | `OrdenTrabajoServiceImplTest` | Estado `Pendiente` y fecha por defecto, actualización de estado, orden inexistente |
| actividades | `OrdenTrabajoControllerTest` | Códigos 200, 201, 400 y 404, incluido el PATCH de estado |
| actividades | `RegistroActividadServiceImplTest` | Fecha por defecto, conservación de comentario y evidencia |
| actividades | `RegistroActividadControllerTest` | Códigos 200, 201 y 404 |
| registrarjornada | `JornadaServiceImplTest` | Fecha por defecto, conservación de tipo y coordenadas |
| registrarjornada | `JornadaControllerTest` | Códigos 200, 201 y 404 |


## Seguridad

Este es un MVP académico y tiene limitaciones conocidas:

- Los endpoints no tienen autenticación ni autorización. La restricción por rol (solo el supervisor crea técnicos) se aplica en la app, no en el servidor.
- `GET /api/empleados` devuelve las contraseñas guardadas, y estas se almacenan en texto plano.
- El tráfico va por HTTP sin cifrar.
- Los puertos de la API están abiertos a internet.