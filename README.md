# DOSW Library

Proyecto Spring Boot con soporte para pruebas unitarias con JUnit 5, cobertura con JaCoCo y análisis de calidad con SonarQube.

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
