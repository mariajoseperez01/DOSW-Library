package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.util.ValidationUtil;

public final class BookValidator {

	private BookValidator() {
	}

	public static void validate(Book book) {
		if (book == null) {
			throw new IllegalArgumentException("Book cannot be null");
		}
		ValidationUtil.requireNonBlank(book.getId(), "Book id");
		ValidationUtil.requireNonBlank(book.getTitle(), "Book title");
		ValidationUtil.requireNonBlank(book.getAuthor(), "Book author");
	}
}
