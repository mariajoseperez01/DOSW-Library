# DOSW Library

Proyecto Spring Boot con soporte para pruebas unitarias con JUnit 5, cobertura con JaCoCo y análisis de calidad con SonarQube.

## DIAGRAMA DE COMPONENTES GENERAL DE LA BIBLIOTECA

![alt text](src/main/resources/uml/ComponentesGeneral.png)

## DIAGRAMA DE COMPONENTES ESPECIFICO DE LA BIBLIOTECA

![alt text](src/main/resources/uml/ComponentesEspecifico.png)

## DIAGRAMA clases


## Requisitos

- Java 21
- Maven 3.9+ o el wrapper incluido en el repositorio
- Docker Desktop (para PostgreSQL y SonarQube)

## Ejecución

Compilar y ejecutar las pruebas:

```bash
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

Levantar PostgreSQL y SonarQube:

```powershell
docker compose up -d
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

- `src/main/java/edu/eci/dosw/tdd/controller`: API REST, DTOs y mappers
- `src/main/java/edu/eci/dosw/tdd/core`: modelo de dominio, servicios, utilidades, validadores y excepciones
- `src/main/java/edu/eci/dosw/tdd/persistence`: capa de persistencia (DAO/Entity, mappers y repositories)
- `src/test/java/edu/eci/dosw/tdd/controller`: pruebas funcionales por operación de controlador
- `src/main/resources/application.yaml`: configuración PostgreSQL/JPA
- `pom.xml`: dependencias y plugins de Maven



Ejecución de funcionalidades de la API
![alt text](src/main/resources/imagenes/Funcionalidades%20API.png)




Video con pruebas de persistencia relacional

 https://drive.google.com/file/d/1tA0qauwH5J1jzvwv0SdI3OaPnU1Uy0s7/view?usp=sharing 


video de Seguridad (Autorización y Autenticación)

