# DOSW Library

Proyecto Spring Boot con soporte para pruebas unitarias con JUnit 5, cobertura con JaCoCo y análisis de calidad con SonarQube.

## DIAGRAMA DE COMPONENTES GENERAL DE LA BIBLIOTECA

![alt text](src/main/resources/uml/ComponentesGeneral.png)

## DIAGRAMA DE COMPONENTES ESPECIFICO DE LA BIBLIOTECA
![alt text](src/main/resources/uml/ComponentesEspecifico.png)

## Requisitos

- Java 21
- Maven 3.9+ o el wrapper incluido en el repositorio

## Ejecución

Compilar y ejecutar las pruebas:

```bash
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

Ejecutar el reporte de cobertura de JaCoCo:

```bash
./mvnw verify
```

El reporte se genera en `target/site/jacoco/index.html` y el archivo XML para SonarQube en `target/site/jacoco/jacoco.xml`.

## SonarQube

Para analizar el código con SonarQube:

```bash
./mvnw sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.token=TU_TOKEN
```

## Estructura

- `src/main/java`: código fuente de la aplicación
- `src/test/java`: pruebas unitarias con JUnit 5
- `pom.xml`: dependencias y plugins de Maven

## Funcionalidades Implementadas

- `Book`: título, autor, id y disponibilidad.
- `User`: nombre e id.
- `Loan`: libro, usuario, fecha de préstamo, estado (`ACTIVE`, `RETURNED`) y fecha de devolución.
- `LibraryService` mantiene:
	- listado de usuarios.
	- listado de préstamos.
	- mapa de inventario de libros (`bookId -> cantidad de ejemplares`).
- Operaciones soportadas:
	- agregar libro con número de ejemplares.
	- consultar todos los libros y un libro por id.
	- actualizar disponibilidad de libro.
	- registrar usuario y consultar usuarios.
	- crear préstamo validando disponibilidad.
	- devolver préstamo y actualizar inventario.

## API REST - Prueba De Ejecución

Base URL: `http://localhost:8080`

Crear libro con stock:

```bash
curl -X POST "http://localhost:8080/api/books?copies=3" \
	-H "Content-Type: application/json" \
	-d '{"id":"B-1","title":"Clean Code","author":"Robert C. Martin"}'
```

Consultar inventario:

```bash
curl "http://localhost:8080/api/books/inventory"
```

Respuesta esperada:

```json
{"B-1":3}
```

Registrar usuario:

```bash
curl -X POST "http://localhost:8080/api/users" \
	-H "Content-Type: application/json" \
	-d '{"id":"U-1","name":"Maria Perez"}'
```

Crear préstamo:

```bash
curl -X POST "http://localhost:8080/api/loans" \
	-H "Content-Type: application/json" \
	-d '{"bookId":"B-1","userId":"U-1"}'
```

Devolver préstamo:

```bash
curl -X PATCH "http://localhost:8080/api/loans/return" \
	-H "Content-Type: application/json" \
	-d '{"bookId":"B-1","userId":"U-1"}'
```

Error esperado cuando no hay ejemplares:

```json
{"error":"Book is not available: B-1"}
```

## Pruebas De Servicios - Evidencia

Ejecución:

```bash
./mvnw test
```

Resumen esperado:

```text
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Cobertura Y Análisis Estático - Evidencia

Cobertura (JaCoCo):

```bash
./mvnw verify
```

Reporte HTML:

- `target/site/jacoco/index.html`

Análisis estático (SonarQube):

```bash
./mvnw sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.token=TU_TOKEN
```

Nota: para ejecutar el análisis estático es necesario tener un servidor SonarQube activo en `localhost:9000`.

## Bitácora

Link del repositorio (ESA):

- https://github.com/mariajoseperez01/DOSW-Library
