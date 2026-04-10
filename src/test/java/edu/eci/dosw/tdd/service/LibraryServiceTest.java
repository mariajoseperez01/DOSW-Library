package edu.eci.dosw.tdd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import edu.eci.dosw.tdd.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.model.Book;
import edu.eci.dosw.tdd.model.Loan;
import edu.eci.dosw.tdd.model.User;

class LibraryServiceTest {

	private final LibraryService libraryService = new LibraryService();

	@Test
	void shouldAddAndFindBooks() {
		Book book = libraryService.addBook(new Book("Clean Code", "Robert C. Martin", "B-1"), 3);

		assertEquals(1, libraryService.getAllBooks().size());
		assertEquals(book, libraryService.getBookById("B-1"));
		assertTrue(book.isAvailable());
		assertEquals(3, libraryService.getBookInventory().get("B-1"));
	}

	@Test
	void shouldRegisterAndFindUsers() {
		User user = libraryService.registerUser(new User("Maria Perez", "U-1"));

		assertEquals(1, libraryService.getAllUsers().size());
		assertEquals(user, libraryService.getUserById("U-1"));
	}

	@Test
	void shouldCreateAndReturnLoan() {
		libraryService.addBook(new Book("Clean Architecture", "Robert C. Martin", "B-2"), 2);
		libraryService.registerUser(new User("Juan Gomez", "U-2"));

		Loan loan = libraryService.createLoan("B-2", "U-2");

		assertNotNull(loan.getLoanDate());
		assertEquals(Loan.Status.ACTIVE, loan.getStatus());
		assertTrue(libraryService.getBookById("B-2").isAvailable());
		assertEquals(1, libraryService.getBookInventory().get("B-2"));
		assertEquals(1, libraryService.getAllLoans().size());

		Loan returnedLoan = libraryService.returnLoan("B-2", "U-2");

		assertEquals(Loan.Status.RETURNED, returnedLoan.getStatus());
		assertNotNull(returnedLoan.getReturnDate());
		assertTrue(libraryService.getBookById("B-2").isAvailable());
		assertEquals(2, libraryService.getBookInventory().get("B-2"));
	}

	@Test
	void shouldRejectUnavailableBookLoans() {
		libraryService.addBook(new Book("Effective Java", "Joshua Bloch", "B-3"), 1);
		libraryService.registerUser(new User("Ana Ruiz", "U-3"));
		libraryService.createLoan("B-3", "U-3");

		assertThrows(BookNotAvailableException.class, () -> libraryService.createLoan("B-3", "U-3"));
		assertFalse(libraryService.getBookById("B-3").isAvailable());
		assertEquals(0, libraryService.getBookInventory().get("B-3"));
	}

	@Test
	void shouldThrowWhenBookDoesNotExist() {
		assertThrows(NoSuchElementException.class, () -> libraryService.getBookById("B-404"));
	}

	@Test
	void shouldThrowWhenUserDoesNotExist() {
		assertThrows(NoSuchElementException.class, () -> libraryService.getUserById("U-404"));
	}

	@Test
	void shouldThrowWhenReturningNonExistingLoan() {
		libraryService.addBook(new Book("Refactoring", "Martin Fowler", "B-4"), 1);
		libraryService.registerUser(new User("Luis Diaz", "U-4"));

		assertThrows(NoSuchElementException.class, () -> libraryService.returnLoan("B-4", "U-4"));
	}

	@Test
	void shouldUpdateBookAvailabilityAndInventory() {
		libraryService.addBook(new Book("Domain-Driven Design", "Eric Evans", "B-5"), 5);

		libraryService.updateBookAvailability("B-5", false);
		assertFalse(libraryService.getBookById("B-5").isAvailable());
		assertEquals(0, libraryService.getBookInventory().get("B-5"));

		libraryService.updateBookAvailability("B-5", true);
		assertTrue(libraryService.getBookById("B-5").isAvailable());
		assertEquals(1, libraryService.getBookInventory().get("B-5"));
	}
}