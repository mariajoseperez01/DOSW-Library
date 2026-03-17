package edu.eci.dosw.tdd.core.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import edu.eci.dosw.tdd.core.validator.BookValidator;

@Service
public class BookService {

	private final Map<String, Book> books = new LinkedHashMap<>();
	private final Map<String, Integer> stockByBookId = new LinkedHashMap<>();

	public Book addBook(Book book, int quantity) {
		BookValidator.validate(book);
		ValidationUtil.requireTrue(quantity >= 0, "Book quantity must be >= 0");

		if (book.getId() == null || book.getId().isBlank()) {
			book.setId(IdGeneratorUtil.generateId());
		}

		books.put(book.getId(), book);
		stockByBookId.put(book.getId(), quantity);
		return book;
	}

	public List<Book> getAllBooks() {
		return new ArrayList<>(books.values());
	}

	public Book getBookById(String bookId) {
		ValidationUtil.requireNonBlank(bookId, "Book id is required");
		Book book = books.get(bookId);
		if (book == null) {
			throw new BookNotAvailableException("Book not found: " + bookId);
		}
		return book;
	}

	public int getBookQuantity(String bookId) {
		getBookById(bookId);
		return stockByBookId.getOrDefault(bookId, 0);
	}

	public boolean isBookAvailable(String bookId) {
		return getBookQuantity(bookId) > 0;
	}

	public void updateAvailability(String bookId, boolean available) {
		getBookById(bookId);
		if (available) {
			if (stockByBookId.getOrDefault(bookId, 0) == 0) {
				stockByBookId.put(bookId, 1);
			}
			return;
		}
		stockByBookId.put(bookId, 0);
	}

	public void decreaseStock(String bookId) {
		int current = getBookQuantity(bookId);
		if (current <= 0) {
			throw new BookNotAvailableException("Book is not available: " + bookId);
		}
		stockByBookId.put(bookId, current - 1);
	}

	public void increaseStock(String bookId) {
		int current = getBookQuantity(bookId);
		stockByBookId.put(bookId, current + 1);
	}
}
