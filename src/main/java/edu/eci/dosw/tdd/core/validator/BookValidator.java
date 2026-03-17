package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.util.ValidationUtil;

public final class BookValidator {

	private BookValidator() {
	}

	public static void validate(Book book) {
		ValidationUtil.requireNotNull(book, "Book is required");
		ValidationUtil.requireNonBlank(book.getTitle(), "Book title is required");
		ValidationUtil.requireNonBlank(book.getAuthor(), "Book author is required");
	}
}
