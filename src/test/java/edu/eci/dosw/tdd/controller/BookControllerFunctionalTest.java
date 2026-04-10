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

import edu.eci.dosw.tdd.persistence.repository.BookInventoryRepository;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BookControllerFunctionalTest {

	@LocalServerPort
	private int port;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookInventoryRepository inventoryRepository;

	@Autowired
	private LoanRepository loanRepository;

	@BeforeEach
	void cleanDatabase() {
		loanRepository.deleteAll();
		inventoryRepository.deleteAll();
		bookRepository.deleteAll();
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
	void shouldCreateBookAndPersistInventory() throws Exception {
		String body = "{\"id\":\"B-1\",\"title\":\"Clean Code\",\"author\":\"Robert C. Martin\",\"available\":true}";
		HttpResponse<String> response = send("POST", "/api/books?copies=3", body);

		assertThat(response.statusCode()).isEqualTo(201);
		assertThat(response.body()).contains("\"id\":\"B-1\"");
		assertThat(response.body()).contains("\"copies\":3");

		assertThat(bookRepository.findById("B-1")).isPresent();
		assertThat(inventoryRepository.findById("B-1")).isPresent();
		assertThat(inventoryRepository.findById("B-1").orElseThrow().getCopies()).isEqualTo(3);
	}

	@Test
	void shouldUpdateAvailabilityAndPersistChange() throws Exception {
		String body = "{\"id\":\"B-5\",\"title\":\"DDD\",\"author\":\"Eric Evans\",\"available\":true}";
		send("POST", "/api/books?copies=2", body);

		HttpResponse<String> patchResponse = send("PATCH", "/api/books/B-5/availability?available=false", null);
		assertThat(patchResponse.statusCode()).isEqualTo(200);
		assertThat(patchResponse.body()).contains("\"available\":false");
		assertThat(inventoryRepository.findById("B-5").orElseThrow().getCopies()).isZero();
	}

	@Test
	void shouldReturnBooksAndInventory() throws Exception {
		String body = "{\"id\":\"B-9\",\"title\":\"Refactoring\",\"author\":\"Martin Fowler\",\"available\":true}";
		send("POST", "/api/books?copies=1", body);

		HttpResponse<String> booksResponse = send("GET", "/api/books", null);
		assertThat(booksResponse.statusCode()).isEqualTo(200);
		assertThat(booksResponse.body()).contains("\"id\":\"B-9\"");

		HttpResponse<String> inventoryResponse = send("GET", "/api/books/inventory", null);
		assertThat(inventoryResponse.statusCode()).isEqualTo(200);
		assertThat(inventoryResponse.body()).contains("\"B-9\":1");
	}
}
