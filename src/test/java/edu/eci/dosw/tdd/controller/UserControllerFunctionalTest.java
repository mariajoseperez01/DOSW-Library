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

import edu.eci.dosw.tdd.persistence.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerFunctionalTest {

	@LocalServerPort
	private int port;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LoanRepository loanRepository;

	@BeforeEach
	void cleanDatabase() {
		loanRepository.deleteAll();
		userRepository.deleteAll();
	}

	private HttpResponse<String> send(String method, String path, String body) throws IOException, InterruptedException {
		HttpRequest.Builder builder = HttpRequest.newBuilder()
			.uri(URI.create("http://localhost:" + port + path))
			.header("Content-Type", "application/json");
		if (body == null) {
			builder.method(method, HttpRequest.BodyPublishers.noBody());
		} else {
			builder.method(method, HttpRequest.BodyPublishers.ofString(body));
		}
		return HttpClient.newHttpClient().send(builder.build(), HttpResponse.BodyHandlers.ofString());
	}

	@Test
	void shouldCreateUserAndPersistInDatabase() throws Exception {
		String body = "{\"id\":\"U-1\",\"name\":\"Maria Perez\"}";
		HttpResponse<String> response = send("POST", "/api/users", body);
		assertThat(response.statusCode()).isEqualTo(201);
		assertThat(response.body()).contains("\"id\":\"U-1\"");

		assertThat(userRepository.findById("U-1")).isPresent();
	}

	@Test
	void shouldReturnRegisteredUsers() throws Exception {
		String body = "{\"id\":\"U-2\",\"name\":\"Juan Gomez\"}";
		send("POST", "/api/users", body);

		HttpResponse<String> usersResponse = send("GET", "/api/users", null);
		assertThat(usersResponse.statusCode()).isEqualTo(200);
		assertThat(usersResponse.body()).contains("\"id\":\"U-2\"");

		HttpResponse<String> userResponse = send("GET", "/api/users/U-2", null);
		assertThat(userResponse.statusCode()).isEqualTo(200);
		assertThat(userResponse.body()).contains("\"name\":\"Juan Gomez\"");
	}
}
