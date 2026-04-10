package edu.eci.dosw.tdd.core.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import edu.eci.dosw.tdd.persistence.port.BookRepositoryPort;

@Service
public class BookService {

	private final BookRepositoryPort bookRepository;

	public BookService(BookRepositoryPort bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Transactional
	public Book addBook(Book book, int copies) {
		BookValidator.validate(book);
		ValidationUtil.requireNonNegative(copies, "Copies");
		return bookRepository.save(book, copies);
	}

	public List<Book> getAllBooks() {
		return Collections.unmodifiableList(bookRepository.findAll());
	}

	public Book getBookById(String bookId) {
		return bookRepository.findById(bookId)
			.orElseThrow(() -> new NoSuchElementException("Book not found: " + bookId));
	}

	public Map<String, Integer> getBookInventory() {
		return Collections.unmodifiableMap(bookRepository.findInventory());
	}

	public int getAvailableCopies(String bookId) {
		return bookRepository.getCopies(bookId);
	}

	@Transactional
	public Book updateBookAvailability(String bookId, boolean available) {
		return bookRepository.updateAvailability(bookId, available);
	}

	@Transactional
	public void decrementCopy(String bookId) {
		bookRepository.decrementCopy(bookId);
	}

	@Transactional
	public void incrementCopy(String bookId) {
		bookRepository.incrementCopy(bookId);
	}
}
