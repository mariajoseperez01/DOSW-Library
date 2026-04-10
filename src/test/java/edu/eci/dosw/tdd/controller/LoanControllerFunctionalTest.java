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
import edu.eci.dosw.tdd.persistence.repository.BookInventoryRepository;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
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

	@BeforeEach
	void cleanDatabase() {
		loanRepository.deleteAll();
		inventoryRepository.deleteAll();
		bookRepository.deleteAll();
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

	private void seedBookAndUser() throws Exception {
		send("POST", "/api/books?copies=2", "{\"id\":\"B-1\",\"title\":\"Clean Architecture\",\"author\":\"Robert C. Martin\",\"available\":true}");
		send("POST", "/api/users", "{\"id\":\"U-1\",\"name\":\"Maria Perez\"}");
	}

	@Test
	void shouldCreateLoanAndPersistDatabaseChanges() throws Exception {
		seedBookAndUser();
		HttpResponse<String> response = send("POST", "/api/loans", "{\"bookId\":\"B-1\",\"userId\":\"U-1\"}");
		assertThat(response.statusCode()).isEqualTo(201);
		assertThat(response.body()).contains("\"status\":\"ACTIVE\"");

		assertThat(loanRepository.count()).isEqualTo(1);
		assertThat(inventoryRepository.findById("B-1").orElseThrow().getCopies()).isEqualTo(1);
	}

	@Test
	void shouldReturnLoanAndPersistDatabaseChanges() throws Exception {
		seedBookAndUser();
		send("POST", "/api/loans", "{\"bookId\":\"B-1\",\"userId\":\"U-1\"}");
		HttpResponse<String> response = send("PATCH", "/api/loans/return", "{\"bookId\":\"B-1\",\"userId\":\"U-1\"}");

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body()).contains("\"status\":\"RETURNED\"");
		assertThat(loanRepository.findAll()).hasSize(1);
		assertThat(loanRepository.findAll().get(0).getStatus()).isEqualTo(Loan.Status.RETURNED);
		assertThat(inventoryRepository.findById("B-1").orElseThrow().getCopies()).isEqualTo(2);
	}

	@Test
	void shouldListLoansFromDatabase() throws Exception {
		seedBookAndUser();
		send("POST", "/api/loans", "{\"bookId\":\"B-1\",\"userId\":\"U-1\"}");
		HttpResponse<String> response = send("GET", "/api/loans", null);

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body()).contains("\"bookId\":\"B-1\"");
	}
}
