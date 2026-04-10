package edu.eci.dosw.tdd.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import edu.eci.dosw.tdd.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.model.Book;
import edu.eci.dosw.tdd.model.Loan;
import edu.eci.dosw.tdd.model.User;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {

	private final Map<String, Book> books = new HashMap<>();
	private final Map<String, Integer> inventory = new HashMap<>();
	private final Map<String, User> usersById = new HashMap<>();
	private final List<User> users = new ArrayList<>();
	private final List<Loan> loans = new ArrayList<>();

	public Book addBook(Book book) {
		return addBook(book, 1);
	}

	public Book addBook(Book book, int copies) {
		if (copies < 0) {
			throw new IllegalArgumentException("Copies cannot be negative");
		}

		books.put(book.getId(), book);
		inventory.put(book.getId(), copies);
		book.setAvailable(copies > 0);
		return book;
	}

	public List<Book> getAllBooks() {
		return Collections.unmodifiableList(new ArrayList<>(books.values()));
	}

	public Book getBookById(String bookId) {
		Book book = books.get(bookId);
		if (book == null) {
			throw new NoSuchElementException("Book not found: " + bookId);
		}
		return book;
	}

	public Map<String, Integer> getBookInventory() {
		return Collections.unmodifiableMap(inventory);
	}

	public Book updateBookAvailability(String bookId, boolean available) {
		Book book = getBookById(bookId);
		book.setAvailable(available);
		if (!available) {
			inventory.put(bookId, 0);
		} else if (inventory.getOrDefault(bookId, 0) == 0) {
			inventory.put(bookId, 1);
		}
		return book;
	}

	public User registerUser(User user) {
		usersById.put(user.getId(), user);
		users.add(user);
		return user;
	}

	public List<User> getAllUsers() {
		return Collections.unmodifiableList(users);
	}

	public User getUserById(String userId) {
		User user = usersById.get(userId);
		if (user == null) {
			throw new NoSuchElementException("User not found: " + userId);
		}
		return user;
	}

	public Loan createLoan(String bookId, String userId) {
		Book book = getBookById(bookId);
		int copies = inventory.getOrDefault(bookId, 0);
		if (!book.isAvailable() || copies <= 0) {
			throw new BookNotAvailableException("Book is not available: " + bookId);
		}

		User user = getUserById(userId);
		Loan loan = new Loan(book, user, LocalDate.now(), Loan.Status.ACTIVE, null);
		loans.add(loan);
		inventory.put(bookId, copies - 1);
		book.setAvailable(inventory.get(bookId) > 0);
		return loan;
	}

	public Loan returnLoan(String bookId, String userId) {
		Loan loan = loans.stream()
			.filter(currentLoan -> currentLoan.getBook().getId().equals(bookId)
				&& currentLoan.getUser().getId().equals(userId)
				&& currentLoan.getStatus() == Loan.Status.ACTIVE)
			.findFirst()
			.orElseThrow(() -> new NoSuchElementException("Active loan not found for book " + bookId + " and user " + userId));

		loan.setStatus(Loan.Status.RETURNED);
		loan.setReturnDate(LocalDate.now());
		String returnedBookId = loan.getBook().getId();
		inventory.put(returnedBookId, inventory.getOrDefault(returnedBookId, 0) + 1);
		loan.getBook().setAvailable(true);
		return loan;
	}

	public List<Loan> getAllLoans() {
		return Collections.unmodifiableList(loans);
	}
}