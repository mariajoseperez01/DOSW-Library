package edu.eci.dosw.tdd.persistence.relational;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.dao.BookInventoryDAO;
import edu.eci.dosw.tdd.persistence.dao.BookDAO;
import edu.eci.dosw.tdd.persistence.mapper.BookPersistenceMapper;
import edu.eci.dosw.tdd.persistence.port.BookRepositoryPort;
import edu.eci.dosw.tdd.persistence.repository.BookInventoryRepository;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;

@Repository
@Profile({"relational", "test"})
public class BookRepositoryAdapter implements BookRepositoryPort {

	private final BookRepository bookRepository;
	private final BookInventoryRepository inventoryRepository;

	public BookRepositoryAdapter(BookRepository bookRepository, BookInventoryRepository inventoryRepository) {
		this.bookRepository = bookRepository;
		this.inventoryRepository = inventoryRepository;
	}

	@Override
	public Book save(Book entity) {
		throw new UnsupportedOperationException("Use save(Book, int copies) instead");
	}

	@Override
	public Book save(Book book, int copies) {
		BookDAO bookDAO = bookRepository.save(BookPersistenceMapper.toDao(book));
		BookInventoryDAO inventoryDAO = inventoryRepository.findById(book.getId()).orElseGet(BookInventoryDAO::new);
		inventoryDAO.setBook(bookDAO);
		inventoryDAO.setCopies(copies);
		inventoryRepository.save(inventoryDAO);
		return BookPersistenceMapper.toModel(bookDAO, copies);
	}

	@Override
	public Optional<Book> findById(String id) {
		return bookRepository.findById(id).map(bookDAO -> BookPersistenceMapper.toModel(bookDAO, getCopies(id)));
	}

	@Override
	public List<Book> findAll() {
		return bookRepository.findAll().stream().map(bookDAO -> BookPersistenceMapper.toModel(bookDAO, getCopies(bookDAO.getId()))).toList();
	}

	@Override
	public void deleteAll() {
		inventoryRepository.deleteAll();
		bookRepository.deleteAll();
	}

	@Override
	public int getCopies(String bookId) {
		return inventoryRepository.findById(bookId).map(BookInventoryDAO::getCopies).orElse(0);
	}

	@Override
	public Map<String, Integer> findInventory() {
		Map<String, Integer> inventory = new HashMap<>();
		for (BookInventoryDAO inventoryDAO : inventoryRepository.findAll()) {
			inventory.put(inventoryDAO.getBookId(), inventoryDAO.getCopies());
		}
		return inventory;
	}

	@Override
	public Book updateAvailability(String bookId, boolean available) {
		BookDAO bookDAO = bookRepository.findById(bookId).orElseThrow(() -> new IllegalStateException("Book not found: " + bookId));
		BookInventoryDAO inventoryDAO = inventoryRepository.findById(bookId).orElseThrow(() -> new IllegalStateException("Inventory not found: " + bookId));
		if (!available) {
			inventoryDAO.setCopies(0);
		} else if (inventoryDAO.getCopies() == 0) {
			inventoryDAO.setCopies(1);
		}
		inventoryRepository.save(inventoryDAO);
		return BookPersistenceMapper.toModel(bookDAO, inventoryDAO.getCopies());
	}

	@Override
	public void decrementCopy(String bookId) {
		BookInventoryDAO inventoryDAO = inventoryRepository.findById(bookId).orElseThrow(() -> new IllegalStateException("Inventory not found: " + bookId));
		inventoryDAO.setCopies(inventoryDAO.getCopies() - 1);
		inventoryRepository.save(inventoryDAO);
	}

	@Override
	public void incrementCopy(String bookId) {
		BookInventoryDAO inventoryDAO = inventoryRepository.findById(bookId).orElseThrow(() -> new IllegalStateException("Inventory not found: " + bookId));
		inventoryDAO.setCopies(inventoryDAO.getCopies() + 1);
		inventoryRepository.save(inventoryDAO);
	}
}