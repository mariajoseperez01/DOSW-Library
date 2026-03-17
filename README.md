# DOSW-Library

Este proyecto implementa una API de biblioteca con Spring Boot para gestionar:

- Libros
- Usuarios
- Préstamos
- Disponibilidad de ejemplares
- Manejo centralizado de errores
- Pruebas unitarias con JUnit
- Reporte de cobertura con JaCoCo

La intención de este README es explicarte TODO el proceso paso a paso, desde la idea hasta la ejecución, para que entiendas el tema a profundidad.

## 1) ¿Qué problema resuelve el proyecto?

Una biblioteca necesita responder estas preguntas de negocio:

- ¿Qué libros existen?
- ¿Cuántos ejemplares hay de cada libro?
- ¿Un libro está disponible o no?
- ¿Qué usuarios están registrados?
- ¿Quién tiene un libro prestado?
- ¿Cuándo se prestó y cuándo se devolvió?
- ¿Qué errores deben devolverse si algo sale mal?

Para resolver eso, el sistema implementa reglas claras:

- No se puede prestar un libro sin stock.
- No se puede prestar si el usuario no existe.
- Un usuario tiene límite de préstamos activos.
- Al devolver un libro, el stock aumenta.

## 2) Estructura del proyecto (arquitectura por capas)

Se organizó el código en paquetes dentro de `edu.eci.dosw.tdd`:

- `controller`: endpoints HTTP (lo que expone la API)
- `controller/dto`: objetos de entrada/salida de la API
- `controller/mapper`: conversión entre DTO y modelo de negocio
- `core/model`: entidades del dominio (Book, User, Loan, Status)
- `core/service`: lógica de negocio
- `core/validator`: validaciones
- `core/util`: utilidades compartidas
- `core/exception`: excepciones de negocio

Esto permite separar responsabilidades y mantener un código más limpio:

- Controller: recibe requests y devuelve responses.
- Service: toma decisiones de negocio.
- Model: representa datos del dominio.
- Validator/Exception: asegura reglas y errores claros.

## 3) Modelos del dominio

Se definieron 4 piezas principales:

### Book

Representa un libro con:

- `id`
- `title`
- `author`

### User

Representa un usuario con:

- `id`
- `name`

### Loan

Representa un préstamo con:

- `id`
- `book`
- `user`
- `loanDate`
- `returnDate`
- `status`

### Status

Enum de estado del préstamo:

- `ACTIVE`
- `RETURNED`

## 4) Cómo se guardan los datos en memoria

Este proyecto no usa base de datos (todavía). Todo está en memoria dentro de servicios.

### En `BookService`

- `Map<String, Book> books`: catálogo de libros por id.
- `Map<String, Integer> stockByBookId`: cantidad de ejemplares por libro.

Esto cumple exactamente con el requisito de “mapa de libros (libro + cantidad de ejemplares)”.

### En `UserService`

- `List<User> users`: listado de usuarios registrados.

### En `LoanService`

- `List<Loan> loans`: listado de préstamos.

## 5) Reglas de negocio implementadas

### Libros

- Agregar libro con cantidad.
- Listar todos los libros.
- Consultar libro por id.
- Consultar disponibilidad por stock (`quantity > 0`).
- Forzar disponibilidad (`PATCH /availability`):
- Si se pone `false`, stock queda en 0.
- Si se pone `true` y estaba en 0, se deja en 1.

### Usuarios

- Registrar usuario.
- Listar usuarios.
- Consultar usuario por id.

### Préstamos

- Crear préstamo validando:
- Usuario existente.
- Libro existente.
- Libro con stock.
- Límite de préstamos activos por usuario (3).
- Devolver préstamo:
- Cambia estado a `RETURNED`.
- Coloca `returnDate`.
- Sube el stock del libro.

## 6) Manejo global de errores (Error Handler)

Se implementó `GlobalExceptionHandler` con `@RestControllerAdvice`.

¿Qué logra esto?

- Evita errores desordenados en texto plano.
- Devuelve respuestas consistentes para el cliente.
- Centraliza el manejo de excepciones de negocio.

Formato de error con `ApiError`:

- `timestamp`
- `status`
- `error`
- `message`

Errores manejados:

- `BookNotAvailableException` → `409 CONFLICT`
- `UserNotFoundException` → `404 NOT_FOUND`
- `LoanLimitExceededException` → `400 BAD_REQUEST`
- `IllegalArgumentException` → `400 BAD_REQUEST`

## 7) Endpoints expuestos

### Books

- `POST /books` crea libro.
- `GET /books` lista libros.
- `GET /books/{bookId}` obtiene libro por id.
- `PATCH /books/{bookId}/availability?available=true|false` actualiza disponibilidad.

Ejemplo `POST /books`:

```json
{
	"title": "Clean Code",
	"author": "Robert C. Martin",
	"quantity": 3
}
```

### Users

- `POST /users` registra usuario.
- `GET /users` lista usuarios.
- `GET /users/{userId}` obtiene usuario por id.

Ejemplo `POST /users`:

```json
{
	"name": "Maria"
}
```

### Loans

- `POST /loans` crea préstamo.
- `POST /loans/{loanId}/return` devuelve préstamo.
- `GET /loans` lista préstamos.

Ejemplo `POST /loans`:

```json
{
	"userId": "USER_ID",
	"bookId": "BOOK_ID"
}
```

## 8) Flujo completo explicado paso a paso

### Escenario feliz (préstamo correcto)

1. Registras usuario con `POST /users`.
2. Creas libro con stock con `POST /books`.
3. Llamas `POST /loans` con `userId` + `bookId`.
4. El sistema valida existencia de usuario/libro.
5. El sistema valida límite de préstamos.
6. El sistema baja stock del libro.
7. Crea un `Loan` con estado `ACTIVE` y fecha actual.
8. Devuelve el préstamo creado.

### Escenario de devolución

1. Llamas `POST /loans/{loanId}/return`.
2. El sistema busca ese préstamo.
3. Si ya estaba devuelto, responde error.
4. Si estaba activo, cambia a `RETURNED`.
5. Coloca fecha de devolución.
6. Incrementa stock del libro.

### Escenario de error por stock

1. Un libro ya no tiene ejemplares (`stock = 0`).
2. Se intenta crear otro préstamo de ese libro.
3. Se lanza `BookNotAvailableException`.
4. `GlobalExceptionHandler` devuelve `409` con mensaje claro.

## 9) Pruebas implementadas

Se crearon pruebas unitarias de servicios para cubrir éxitos y errores:

- `BookServiceTest`
- `UserServiceTest`
- `LoanServiceTest`

Escenarios cubiertos:

- Crear y consultar libro.
- Cambiar disponibilidad.
- Error al buscar libro inexistente.
- Registrar y consultar usuario.
- Error por usuario inexistente.
- Crear y devolver préstamo.
- Error por libro sin stock.
- Error por límite de préstamos activos.

## 10) Cobertura y análisis

### Cobertura (JaCoCo)

Se genera en:

- `target/site/jacoco/index.html`

Comando:

```bash
mvn verify
```

### Análisis estático (Sonar)

Comando:

```bash
mvn sonar:sonar
```

Nota importante: para que funcione, debes tener configurado en SonarCloud:

- organización existente
- project key correcto
- token válido en `SONAR_TOKEN`

## 11) Cómo ejecutar localmente

### 1. Compilar

```bash
mvn -DskipTests compile
```

### 2. Ejecutar pruebas

```bash
mvn test
```

### 3. Levantar API

```bash
mvn spring-boot:run
```

## 12) Decisiones técnicas importantes (para entender el “por qué”)

- Se usó almacenamiento en memoria para enfocarse en lógica de negocio.
- Se separó DTO del modelo para no acoplar contrato HTTP al dominio.
- Se centralizó manejo de errores para respuestas consistentes.
- Se añadió validación explícita para evitar datos inválidos.
- Se implementaron pruebas primero sobre servicios porque ahí vive la lógica crítica.

## 13) Qué sigue para evolucionar el proyecto

Cuando este nivel esté dominado, el siguiente paso natural es:

- Persistencia real con JPA + PostgreSQL
- Capa repository
- Más pruebas de integración con `@SpringBootTest` y `MockMvc`
- Versionado de API y documentación con OpenAPI/Swagger
- Endurecer reglas de negocio (fechas límite, multas, etc.)

---

Si estás estudiando este proyecto para clase, te recomiendo practicar en este orden:

1. Crear usuario.
2. Crear libro con distintas cantidades.
3. Prestar hasta agotar stock.
4. Forzar errores (usuario inexistente, límite de préstamos).
5. Devolver préstamo y comprobar que el stock sube.
6. Revisar los tests y luego escribir uno nuevo por tu cuenta.

Ese ejercicio te deja dominando el flujo completo de una API de negocio real.