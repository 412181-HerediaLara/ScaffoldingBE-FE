# SDD - Backend (Sistema de Autenticación y Gestión)

## 1. Objetivo del Backend
Construir una API REST segura que maneje autenticación mediante JWT (filtro personalizado + Spring Security) y exponga endpoints CRUD para las entidades del negocio. 
El backend sirve únicamente JSON a través de endpoints versionados (`/api/v1/...`) y públicos (`/api/auth/...`).

---

## 2. Stack Tecnológico (Inamovible)
- **Lenguaje:** Java 21 (LTS).
- **Framework:** Spring Boot 4.1.0.
- **Gestor de dependencias:** Maven.
- **Seguridad:** Spring Security + JWT (JJWT 0.12.6) con filtro personalizado `JwtAuthenticationFilter`.
- **Persistencia:** Spring Data JPA (Hibernate).
- **Base de datos:** H2 (en memoria, consola habilitada en `/h2-console`).
- **Mapping:** ModelMapper 3.2.0 (STRICT, field matching, skip null).
- **Documentación:** SpringDoc OpenAPI 2.8.6 (Swagger UI en `/swagger-ui.html`).
- **Testing:** JUnit 5, Mockito, Spring Boot Test, AssertJ.
- **Cobertura:** JaCoCo 0.8.12 (mínimo 95% LINE, 95% BRANCH).

---

## 3. Estructura de Paquetes (Obligatoria)
El código fuente (`src/main/java/be/parcial/`) sigue esta estructura:

- `config/` → Clases de configuración de Spring (`SecurityConfig`, `CorsConfig`, `MappersConfig`, `OpenApiConfig`).
- `controllers/` → Controladores REST (solo manejan peticiones/respuestas, sin lógica de negocio).
- `dtos/` → Objetos de transferencia de datos (petición y respuesta).
- `entities/` → Entidades JPA (mapeo a tablas).
- `enums/` → Enumeraciones compartidas (`DummyEnum`).
- `exceptions/` → Manejador global de excepciones y excepciones personalizadas.
- `models/` → Modelos intermedios para mapeo Entity↔DTO vía ModelMapper.
- `repositories/` → Interfaces que extienden `JpaRepository`.
- `security/` → Filtros JWT y servicio de tokens (`JwtService`, `JwtAuthenticationFilter`, `CustomUserDetailsService`).
- `services/` → Interfaces de servicios.
- `services/implementations/` → Implementaciones concretas de los servicios (lógica de negocio).
- `domain/` → (Reservado para lógica de dominio futura).

---

## 4. Modelo de Datos (Entidades JPA)

### 4.1. Entidad `UserEntity` (Tabla: `users`)
| Campo | Tipo Java | Anotaciones JPA | Restricciones |
| :--- | :--- | :--- | :--- |
| `id` | `Long` | `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)` | Autoincremental. |
| `email` | `String` | `@Column(nullable = false, unique = true, length = 100)` | Formato email válido. Usado como principal de autenticación. |
| `password` | `String` | `@Column(nullable = false)` | Almacenado con `BCryptPasswordEncoder`. No devolver en respuestas. |
| `name` | `String` | `@Column(nullable = false, length = 100)` | Nombre visible del usuario. |
| `role` | `Enum (Role)` | `@Enumerated(EnumType.STRING)` | `USER` o `ADMIN`. Inner enum de `UserEntity`. |
| `createdAt` | `LocalDateTime` | `@Column(nullable = false, updatable = false)` | `@PrePersist` autogenerado. |
| `updatedAt` | `LocalDateTime` | `@Column(nullable = false)` | `@PrePersist` / `@PreUpdate` autogenerado. |

### 4.2. Entidad `RefreshTokenEntity` (Tabla: `refresh_token`)
| Campo | Tipo Java | Anotaciones JPA | Restricciones |
| :--- | :--- | :--- | :--- |
| `id` | `Long` | `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)` | Autoincremental. |
| `token` | `String` | `@Column(nullable = false, unique = true, length = 500)` | JWT generado por `JwtService`. |
| `user` | `UserEntity` | `@ManyToOne(fetch = FetchType.LAZY)` | Join column `user_id`. |
| `expiryDate` | `LocalDateTime` | `@Column(nullable = false)` | Duración de 7 días. |
| `revoked` | `boolean` | `@Column(nullable = false)` | `false` por defecto. Se marca `true` al hacer logout/refresh. |
| `createdAt` | `LocalDateTime` | `@Column(nullable = false, updatable = false)` | `@PrePersist` autogenerado. |

---

## 5. Contratos de API (Endpoints REST)

### 5.1. Autenticación

**Base Path:** `/api/auth`

| Método | Endpoint | Autenticación | Descripción |
| :--- | :--- | :--- | :--- |
| `POST` | `/register` | Público | Registra un nuevo usuario. Devuelve `AuthResponse` con tokens. |
| `POST` | `/authenticate` | Público | Autentica por email+password. Devuelve `AuthResponse`. |
| `POST` | `/refresh` | Público (requiere refresh token válido) | Renueva access token y refresh token. |
| `POST` | `/logout` | Público (requiere refresh token) | Invalida el refresh token en BD (revocado). |

### 5.2. Entidad de Negocio Dummy

**Base Path:** `/api/v1/dummies`

| Método | Endpoint | Autenticación | Descripción |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | Requiere JWT | Lista todos los dummies. |
| `GET` | `/{id}` | Requiere JWT | Obtiene un dummy por ID. |
| `POST` | `/` | Requiere JWT | Crea un nuevo dummy. |
| `PUT` | `/{id}` | Requiere JWT | Actualiza un dummy existente (merge parcial). |
| `DELETE` | `/{id}` | Requiere JWT | Elimina un dummy físicamente. |

---

## 6. DTOs (Objetos de Transferencia)

**Requests:**
- `RegisterRequestDTO`: `{ email, password, name }` — `password` mínimo 6 caracteres.
- `AuthRequestDTO`: `{ email, password }` — usado en `/authenticate`.
- `RefreshTokenRequestDTO`: `{ refreshToken }`.
- `DummyRequestDTO`: `{ name }`.

**Responses:**
- `AuthResponseDTO`: `{ accessToken, refreshToken, tokenType, email, role }` — **NUNCA incluye password**.
- `DummyResponseDTO`: `{ id, name, dummyEnum }`.
- `ErrorResponse`: `{ status (int), error (string), message (string), path (string), timestamp, details }` — formato obligatorio para todas las respuestas de error.

---

## 7. Capa de Seguridad (JWT + Spring Security)

### 7.1. Configuración (`SecurityConfig`)
- CSRF deshabilitado.
- `SessionCreationPolicy.STATELESS`.
- Endpoints públicos: `/api/auth/**`, `/h2-console/**`, `/swagger-ui/**`, `/v3/api-docs/**`.
- Resto de endpoints requieren autenticación (`authenticated()`).
- `JwtAuthenticationFilter` añadido **antes** de `UsernamePasswordAuthenticationFilter`.
- `DaoAuthenticationProvider` con `BCryptPasswordEncoder`.

### 7.2. Filtro (`JwtAuthenticationFilter`)
- Extrae el token del header `Authorization: Bearer {token}`.
- Extrae el email (subject del JWT) vía `JwtService.extractUsername()`.
- Carga `UserDetails` desde BD vía `CustomUserDetailsService.loadUserByUsername(email)`.
- Si el token es válido, establece `UsernamePasswordAuthenticationToken` en `SecurityContextHolder`.
- No lanza excepciones directamente; delega al `AuthenticationEntryPoint` por defecto de Spring.

### 7.3. Servicio de Tokens (`JwtService`)
- Algoritmo: `HS256` (HMAC-SHA).
- Clave secreta: `jwt.secret` desde `application.properties` (Base64, mínimo 32 bytes).
- Access token: `900000` ms (15 minutos).
- Refresh token: `604800000` ms (7 días).
- Issuer: `parcial-app`.
- Claims: `subject = email`, `issuedAt`, `expiration`, `jti` (solo refresh).

### 7.4. UserDetails personalizado (`CustomUserDetailsService`)
- Implementa `UserDetailsService`.
- Busca por email en `UserRepository`.
- Retorna `User` de Spring Security con `ROLE_USER` o `ROLE_ADMIN`.

### 7.5. Codificación de contraseñas
- Siempre `BCryptPasswordEncoder` (fuerza por defecto = 10).

---

## 8. Manejador Global de Excepciones (`GlobalExceptionHandler`)

Usa `@RestControllerAdvice` para capturar:

| Excepción | Status | Descripción |
| :--- | :--- | :--- |
| `ResourceNotFoundException` | `404` (Not Found) | Entidad no encontrada. |
| `MethodArgumentNotValidException` | `400` (Bad Request) | Errores de validación con detalles campo por campo. |
| `BadCredentialsException` | `401` (Unauthorized) | Credenciales inválidas. |
| `IllegalArgumentException` | `400` (Bad Request) | Errores de negocio (usuario duplicado, token expirado/revocado). |
| `Exception` (genérica) | `500` (Internal Server Error) | Error no esperado (no expone stacktrace). |

Todas devuelven el DTO `ErrorResponse` con estructura `{ status, error, message, path, timestamp, details }`.

---

## 9. Perfiles y Configuración

### `application.properties` (Desarrollo — H2)
```properties
spring.application.name=parcial
server.port=8080
spring.datasource.url=jdbc:h2:mem:parcialdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=never

# JWT
jwt.secret=dGVzdC1zZWNyZXQta2V5LWZvci1kZXZlbG9wbWVudC1vbmx5LW1pbmltdW0tMzItYml0cw==
jwt.access-token-expiration=900000
jwt.refresh-token-expiration=604800000
jwt.issuer=parcial-app

# CORS
cors.allowed-origins=http://localhost:4200,http://localhost:3000

# Swagger / OpenAPI
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### Seed Data
`src/main/resources/data.sql` inserta datos de ejemplo al iniciar la aplicación (vía `spring.sql.init.mode=always` + `spring.jpa.defer-datasource-initialization=true`):
- 3 usuarios (admin@parcial.com, user@parcial.com, jdoe@parcial.com) con contraseñas BCrypt.
- 4 dummies (Alpha, Beta, Gamma, Delta).

**Nota:** En producción, migrar a variables de entorno (`${JWT_SECRET}`, `${DB_URL}`, `${DB_USERNAME}`, `${DB_PASSWORD}`) y usar PostgreSQL con `spring.jpa.hibernate.ddl-auto=validate`.

---

## 10. Dependencias Maven (pom.xml) — Claves

| Dependencia | Versión | Propósito |
| :--- | :--- | :--- |
| `spring-boot-starter-webmvc` | (parent) | REST controllers |
| `spring-boot-starter-data-jpa` | (parent) | JPA + Hibernate |
| `spring-boot-starter-security` | (parent) | Spring Security |
| `spring-boot-starter-validation` | (parent) | `@Valid`, `@NotBlank`, etc. |
| `jjwt-api`, `jjwt-impl`, `jjwt-jackson` | 0.12.6 | JWT generación/validación |
| `modelmapper` | 3.2.0 | Entity↔Model↔DTO mapping |
| `h2` | (parent) | Base de datos en memoria (runtime) |
| `springdoc-openapi-starter-webmvc-ui` | 2.8.6 | Swagger UI |
| `jackson-datatype-jsr310` | (parent) | Serialización `LocalDateTime` |
| `lombok` | (parent) | `@Data`, `@RequiredArgsConstructor`, etc. |
| `spring-boot-starter-test` | (parent) | JUnit 5, Mockito, AssertJ |
| `spring-security-test` | (parent) | `@WithMockUser`, etc. |

---

## 11. Estrategia de Testing

### Unitarios (`/src/test/java/.../services/`)
- Usan `@ExtendWith(MockitoExtension.class)`.
- Mockean repositorios y prueban lógica de negocio en `ServiceImpl`.
- Verifican que las contraseñas se cifran antes de guardar.

### Unitarios de Seguridad (`/src/test/java/.../security/`)
- `JwtServiceTest`: genera tokens, extrae claims, valida expiración.
- `JwtAuthenticationFilterTest`: simula peticiones con/sin token.
- `CustomUserDetailsServiceTest`: carga usuario por email.

### Integración (`/src/test/java/.../controllers/`)
- Usan `@WebMvcTest` y `@SpringBootTest` + `@AutoConfigureMockMvc`.
- Prueban flujo completo: registro → login → acceso protegido.
- Prueban filtro JWT con header `Authorization`.

### Otras capas
- `entities/UserEntityTest`: verifica callbacks `@PrePersist`/`@PreUpdate`.
- `config/ModelMapperConfigTest`: verifica `TypeMap` personalizado y configuración.
- `exceptions/GlobalExceptionHandlerTest`: verifica respuestas de error.
- `ParcialApplicationTests`: verifica que el contexto de Spring carga.

### Cobertura
- Mínimo: **95% LINE**, **95% BRANCH** (vía JaCoCo).
- Objetivo actual: **100% LINE**, **96.4% BRANCH**.

---

## 12. Comandos de Ejecución

```bash
# Levantar en desarrollo (H2)
mvn spring-boot:run

# Ejecutar tests
mvn clean test

# Verificar cobertura
mvn clean verify

# Empaquetar JAR
mvn clean package

# Levantar JAR empaquetado
java -jar target/parcial-0.0.1-SNAPSHOT.jar
```

---

## 13. Restricciones Técnicas NO NEGOCIABLES

- ✅ El `JwtAuthenticationFilter` **NO** lanza excepciones directamente; continúa el chain si no hay token o es inválido.
- ✅ Todas las relaciones JPA son `LAZY` (salvo que se justifique `EAGER`).
- ✅ No se usa `@Autowired` en campos; solo inyección por constructor (vía Lombok `@RequiredArgsConstructor`).
- ✅ Las contraseñas en texto plano están **prohibidas** en cualquier log o respuesta JSON.
- ✅ `application.properties` puede contener valores por defecto para desarrollo; en producción usar placeholders `${...}`.
- ✅ Los DTOs de respuesta **nunca** incluyen el campo `password`.
- ✅ Toda respuesta de error usa el formato `ErrorResponse` unificado.
