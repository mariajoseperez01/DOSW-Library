package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.dao.BookDAO;

public final class BookPersistenceMapper {

	private BookPersistenceMapper() {
	}

	public static BookDAO toDao(Book book) {
		BookDAO dao = new BookDAO();
		dao.setId(book.getId());
		dao.setTitle(book.getTitle());
		dao.setAuthor(book.getAuthor());
		return dao;
	}

	public static Book toModel(BookDAO dao, int copies) {
		Book book = new Book();
		book.setId(dao.getId());
		book.setTitle(dao.getTitle());
		book.setAuthor(dao.getAuthor());
		book.setAvailable(copies > 0);
		return book;
	}
}
