package edu.eci.dosw.tdd.persistence.port;

import java.util.Map;

import edu.eci.dosw.tdd.core.model.Book;

public interface BookRepositoryPort extends RepositoryPort<Book, String> {

	Book save(Book book, int copies);

	int getCopies(String bookId);

	Map<String, Integer> findInventory();

	Book updateAvailability(String bookId, boolean available);

	void decrementCopy(String bookId);

	void incrementCopy(String bookId);
}