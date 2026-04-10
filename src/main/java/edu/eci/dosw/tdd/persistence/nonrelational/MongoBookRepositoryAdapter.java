package edu.eci.dosw.tdd.persistence.nonrelational;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.mongo.repository.BookMongoRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;
import edu.eci.dosw.tdd.persistence.port.BookRepositoryPort;

@Repository
@Profile("mongo")
public class MongoBookRepositoryAdapter implements BookRepositoryPort {

	private final BookMongoRepository bookMongoRepository;

	public MongoBookRepositoryAdapter(BookMongoRepository bookMongoRepository) {
		this.bookMongoRepository = bookMongoRepository;
	}

	@Override
	public Book save(Book entity) {
		throw new UnsupportedOperationException("Use save(Book, int copies) instead");
	}

	@Override
	public Book save(Book book, int copies) {
		BookDocument document = toDocument(book);
		document.setCopies(copies);
		return toModel(bookMongoRepository.save(document));
	}

	@Override
	public Optional<Book> findById(String id) {
		return bookMongoRepository.findById(id).map(this::toModel);
	}

	@Override
	public List<Book> findAll() {
		return bookMongoRepository.findAll().stream().map(this::toModel).toList();
	}

	@Override
	public void deleteAll() {
		bookMongoRepository.deleteAll();
	}

	@Override
	public int getCopies(String bookId) {
		return bookMongoRepository.findById(bookId).map(book -> book.getCopies() == null ? 0 : book.getCopies()).orElse(0);
	}

	@Override
	public Map<String, Integer> findInventory() {
		Map<String, Integer> inventory = new HashMap<>();
		bookMongoRepository.findAll().forEach(book -> inventory.put(book.getId(), book.getCopies() == null ? 0 : book.getCopies()));
		return inventory;
	}

	@Override
	public Book updateAvailability(String bookId, boolean available) {
		BookDocument document = bookMongoRepository.findById(bookId).orElseThrow(() -> new IllegalStateException("Book not found: " + bookId));
		if (!available) {
			document.setCopies(0);
		} else if (document.getCopies() == null || document.getCopies() == 0) {
			document.setCopies(1);
		}
		return toModel(bookMongoRepository.save(document));
	}

	@Override
	public void decrementCopy(String bookId) {
		BookDocument document = bookMongoRepository.findById(bookId).orElseThrow(() -> new IllegalStateException("Book not found: " + bookId));
		document.setCopies(getCopies(bookId) - 1);
		bookMongoRepository.save(document);
	}

	@Override
	public void incrementCopy(String bookId) {
		BookDocument document = bookMongoRepository.findById(bookId).orElseThrow(() -> new IllegalStateException("Book not found: " + bookId));
		document.setCopies(getCopies(bookId) + 1);
		bookMongoRepository.save(document);
	}

	private BookDocument toDocument(Book book) {
		BookDocument document = new BookDocument();
		document.setId(book.getId());
		document.setTitle(book.getTitle());
		document.setAuthor(book.getAuthor());
		document.setCopies(book.isAvailable() ? 1 : 0);
		return document;
	}

	private Book toModel(BookDocument document) {
		Book book = new Book();
		book.setId(document.getId());
		book.setTitle(document.getTitle());
		book.setAuthor(document.getAuthor());
		book.setAvailable(document.getCopies() != null && document.getCopies() > 0);
		return book;
	}
}