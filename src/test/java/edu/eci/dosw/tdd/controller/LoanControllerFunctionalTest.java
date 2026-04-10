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

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;
import edu.eci.dosw.tdd.persistence.repository.BookInventoryRepository;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "relational"})
class LoanControllerFunctionalTest {

	@LocalServerPort
	private int port;

	@Autowired
	private LoanRepository loanRepository;

	@Autowired
	private BookInventoryRepository inventoryRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@BeforeEach
	void cleanDatabase() {
		loanRepository.deleteAll();
		inventoryRepository.deleteAll();
		bookRepository.deleteAll();
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

	private void seedBookAndUser() throws Exception {
		String token = loginToken();
		send("POST", "/api/books?copies=2", "{\"id\":\"B-1\",\"title\":\"Clean Architecture\",\"author\":\"Robert C. Martin\",\"available\":true}", token);
		send("POST", "/api/users", "{\"id\":\"U-1\",\"name\":\"Maria Perez\",\"password\":\"user123\",\"role\":\"USER\"}", token);
	}

	@Test
	void shouldCreateLoanAndPersistDatabaseChanges() throws Exception {
		seedBookAndUser();
		String token = loginToken();
		HttpResponse<String> response = send("POST", "/api/loans", "{\"bookId\":\"B-1\",\"userId\":\"U-1\"}", token);
		assertThat(response.statusCode()).isEqualTo(201);
		assertThat(response.body()).contains("\"status\":\"ACTIVE\"");

		assertThat(loanRepository.count()).isEqualTo(1);
		assertThat(inventoryRepository.findById("B-1").orElseThrow().getCopies()).isEqualTo(1);
	}

	@Test
	void shouldReturnLoanAndPersistDatabaseChanges() throws Exception {
		seedBookAndUser();
		String token = loginToken();
		send("POST", "/api/loans", "{\"bookId\":\"B-1\",\"userId\":\"U-1\"}", token);
		HttpResponse<String> response = send("PATCH", "/api/loans/return", "{\"bookId\":\"B-1\",\"userId\":\"U-1\"}", token);

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body()).contains("\"status\":\"RETURNED\"");
		assertThat(loanRepository.findAll()).hasSize(2);
		assertThat(loanRepository.findAll()).anySatisfy(loan -> assertThat(loan.getStatus()).isEqualTo(Loan.Status.RETURNED));
		assertThat(inventoryRepository.findById("B-1").orElseThrow().getCopies()).isEqualTo(2);
	}

	@Test
	void shouldListLoansFromDatabase() throws Exception {
		seedBookAndUser();
		String token = loginToken();
		send("POST", "/api/loans", "{\"bookId\":\"B-1\",\"userId\":\"U-1\"}", token);
		HttpResponse<String> response = send("GET", "/api/loans", null, token);

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body()).contains("\"bookId\":\"B-1\"");
	}
}
