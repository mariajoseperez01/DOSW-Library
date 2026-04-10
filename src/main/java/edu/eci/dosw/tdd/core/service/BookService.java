package edu.eci.dosw.tdd.core.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import edu.eci.dosw.tdd.persistence.dao.BookDAO;
import edu.eci.dosw.tdd.persistence.dao.BookInventoryDAO;
import edu.eci.dosw.tdd.persistence.mapper.BookPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.BookInventoryRepository;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;

@Service
public class BookService {

	private final BookRepository bookRepository;
	private final BookInventoryRepository inventoryRepository;

	public BookService(BookRepository bookRepository, BookInventoryRepository inventoryRepository) {
		this.bookRepository = bookRepository;
		this.inventoryRepository = inventoryRepository;
	}

	@Transactional
	public Book addBook(Book book, int copies) {
		BookValidator.validate(book);
		ValidationUtil.requireNonNegative(copies, "Copies");

		BookDAO bookDAO = bookRepository.save(BookPersistenceMapper.toDao(book));
		BookInventoryDAO inventoryDAO = inventoryRepository.findById(book.getId()).orElseGet(BookInventoryDAO::new);
		inventoryDAO.setBook(bookDAO);
		inventoryDAO.setCopies(copies);
		inventoryRepository.save(inventoryDAO);
		return BookPersistenceMapper.toModel(bookDAO, copies);
	}

	public List<Book> getAllBooks() {
		return Collections.unmodifiableList(
			bookRepository.findAll().stream()
				.map(bookDAO -> BookPersistenceMapper.toModel(bookDAO, getAvailableCopies(bookDAO.getId())))
				.toList());
	}

	public Book getBookById(String bookId) {
		BookDAO bookDAO = bookRepository.findById(bookId)
			.orElseThrow(() -> new NoSuchElementException("Book not found: " + bookId));
		return BookPersistenceMapper.toModel(bookDAO, getAvailableCopies(bookId));
	}

	public Map<String, Integer> getBookInventory() {
		Map<String, Integer> inventory = new HashMap<>();
		for (BookInventoryDAO inventoryDAO : inventoryRepository.findAll()) {
			inventory.put(inventoryDAO.getBookId(), inventoryDAO.getCopies());
		}
		return Collections.unmodifiableMap(inventory);
	}

	public int getAvailableCopies(String bookId) {
		return inventoryRepository.findById(bookId)
			.map(BookInventoryDAO::getCopies)
			.orElse(0);
	}

	@Transactional
	public Book updateBookAvailability(String bookId, boolean available) {
		BookDAO bookDAO = bookRepository.findById(bookId)
			.orElseThrow(() -> new NoSuchElementException("Book not found: " + bookId));
		BookInventoryDAO inventoryDAO = inventoryRepository.findById(bookId)
			.orElseThrow(() -> new NoSuchElementException("Inventory not found for book: " + bookId));

		if (!available) {
			inventoryDAO.setCopies(0);
		} else if (inventoryDAO.getCopies() == 0) {
			inventoryDAO.setCopies(1);
		}

		inventoryRepository.save(inventoryDAO);
		return BookPersistenceMapper.toModel(bookDAO, inventoryDAO.getCopies());
	}

	@Transactional
	public void decrementCopy(String bookId) {
		BookInventoryDAO inventoryDAO = inventoryRepository.findById(bookId)
			.orElseThrow(() -> new NoSuchElementException("Inventory not found for book: " + bookId));
		inventoryDAO.setCopies(inventoryDAO.getCopies() - 1);
		inventoryRepository.save(inventoryDAO);
	}

	@Transactional
	public void incrementCopy(String bookId) {
		BookInventoryDAO inventoryDAO = inventoryRepository.findById(bookId)
			.orElseThrow(() -> new NoSuchElementException("Inventory not found for book: " + bookId));
		inventoryDAO.setCopies(inventoryDAO.getCopies() + 1);
		inventoryRepository.save(inventoryDAO);
	}
}
