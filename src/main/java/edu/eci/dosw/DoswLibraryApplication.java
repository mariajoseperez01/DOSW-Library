package edu.eci.dosw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "DOSW Library API",
		version = "v1",
		description = "API para gestionar libros, usuarios y prestamos"
	)
)
public class DoswLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoswLibraryApplication.class, args);
	}

}
