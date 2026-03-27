package edu.eci.dosw.tdd.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import edu.eci.dosw.tdd.persistence.mapper.BookPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;

@Service
@Transactional
public class BookService {

	private final BookRepository bookRepository;
	private final BookPersistenceMapper bookPersistenceMapper;

	public BookService(BookRepository bookRepository, BookPersistenceMapper bookPersistenceMapper) {
		this.bookRepository = bookRepository;
		this.bookPersistenceMapper = bookPersistenceMapper;
	}

	public Book addBook(Book book) {
		BookValidator.validate(book);

		if (book.getId() == null || book.getId().isBlank()) {
			book.setId(IdGeneratorUtil.generateId());
		}

		return bookPersistenceMapper.toDomain(bookRepository.save(bookPersistenceMapper.toEntity(book)));
	}

	@Transactional(readOnly = true)
	public List<Book> getAllBooks() {
		return bookRepository.findAll().stream().map(bookPersistenceMapper::toDomain).toList();
	}

	@Transactional(readOnly = true)
	public Book getBookById(String bookId) {
		ValidationUtil.requireNonBlank(bookId, "Book id is required");
		return bookRepository.findById(bookId)
			.map(bookPersistenceMapper::toDomain)
			.orElseThrow(() -> new BookNotAvailableException("Book not found: " + bookId));
	}

	public int getBookQuantity(String bookId) {
		return getBookById(bookId).getAvailableUnits();
	}

	public boolean isBookAvailable(String bookId) {
		return getBookQuantity(bookId) > 0;
	}

	public void updateAvailability(String bookId, boolean available) {
		Book book = getBookById(bookId);
		if (available) {
			if (book.getAvailableUnits() == 0) {
				book.setAvailableUnits(1);
				if (book.getTotalUnits() == 0) {
					book.setTotalUnits(1);
				}
				bookRepository.save(bookPersistenceMapper.toEntity(book));
			}
			return;
		}
		book.setAvailableUnits(0);
		bookRepository.save(bookPersistenceMapper.toEntity(book));
	}

	public void decreaseStock(String bookId) {
		Book book = getBookById(bookId);
		if (book.getAvailableUnits() <= 0) {
			throw new BookNotAvailableException("Book is not available: " + bookId);
		}
		book.setAvailableUnits(book.getAvailableUnits() - 1);
		bookRepository.save(bookPersistenceMapper.toEntity(book));
	}

	public void increaseStock(String bookId) {
		Book book = getBookById(bookId);
		int next = Math.min(book.getAvailableUnits() + 1, book.getTotalUnits());
		book.setAvailableUnits(next);
		bookRepository.save(bookPersistenceMapper.toEntity(book));
	}
}
