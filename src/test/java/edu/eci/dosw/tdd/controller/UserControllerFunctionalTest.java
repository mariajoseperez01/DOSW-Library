package edu.eci.dosw.tdd.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "relational"})
class UserControllerFunctionalTest {

	@LocalServerPort
	private int port;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LoanRepository loanRepository;

	@Autowired
	private UserService userService;

	@BeforeEach
	void cleanDatabase() {
		loanRepository.deleteAll();
		userRepository.deleteAll();
		userService.registerUser(new User("librarian", "librarian123", Role.LIBRARIAN, "LIB-1"));
	}

	private String loginToken() throws IOException, InterruptedException {
		HttpResponse<String> response = send("POST", "/auth/login", "{\"username\":\"librarian\",\"password\":\"librarian123\"}", null);
		return response.body().replaceAll(".*\\\"token\\\":\\\"([^\\\"]+)\\\".*", "$1");
	}

	private HttpResponse<String> send(String method, String path, String body, String token) throws IOException, InterruptedException {
		HttpRequest.Builder builder = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:" + port + path))
			.header("Content-Type", "application/json");
		if (token != null) {
			builder.header("Authorization", "Bearer " + token);
		}
		if (body == null) {
			builder.method(method, HttpRequest.BodyPublishers.noBody());
		} else {
			builder.method(method, HttpRequest.BodyPublishers.ofString(body));
		}
		return HttpClient.newHttpClient().send(builder.build(), HttpResponse.BodyHandlers.ofString());
	}

	@Test
	void shouldCreateUserAndPersistInDatabase() throws Exception {
		String token = loginToken();
		HttpResponse<String> response = send("POST", "/api/users", "{\"id\":\"U-1\",\"name\":\"Maria Perez\",\"password\":\"user123\",\"role\":\"USER\"}", token);
		assertThat(response.statusCode()).isEqualTo(201);
		assertThat(response.body()).contains("\"id\":\"U-1\"");

		assertThat(userRepository.findById("U-1")).isPresent();
	}

	@Test
	void shouldReturnRegisteredUsers() throws Exception {
		String token = loginToken();
		send("POST", "/api/users", "{\"id\":\"U-2\",\"name\":\"Juan Gomez\",\"password\":\"user123\",\"role\":\"USER\"}", token);

		HttpResponse<String> usersResponse = send("GET", "/api/users", null, token);
		assertThat(usersResponse.statusCode()).isEqualTo(200);
		assertThat(usersResponse.body()).contains("\"id\":\"U-2\"");

		HttpResponse<String> userResponse = send("GET", "/api/users/U-2", null, token);
		assertThat(userResponse.statusCode()).isEqualTo(200);
		assertThat(userResponse.body()).contains("\"name\":\"Juan Gomez\"");
	}
}
