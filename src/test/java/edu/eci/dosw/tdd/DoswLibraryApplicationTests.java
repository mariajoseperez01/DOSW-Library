package edu.eci.dosw.tdd;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DoswLibraryApplicationTests {

	@Test
	void contextLoads() {
		// Context load test validates Spring wiring after package refactor.
	}

}