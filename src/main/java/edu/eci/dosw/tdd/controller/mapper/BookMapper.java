package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.core.model.Book;

public final class BookMapper {

	private BookMapper() {
	}

	public static Book toModel(BookDTO dto) {
		Book book = new Book();
		book.setId(dto.getId());
		book.setTitle(dto.getTitle());
		book.setAuthor(dto.getAuthor());
		return book;
	}

	public static BookDTO toDto(Book book, int quantity) {
		BookDTO dto = new BookDTO();
		dto.setId(book.getId());
		dto.setTitle(book.getTitle());
		dto.setAuthor(book.getAuthor());
		dto.setQuantity(quantity);
		dto.setAvailable(quantity > 0);
		return dto;
	}
}
