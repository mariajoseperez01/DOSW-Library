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
		book.setTotalUnits(dto.getTotalUnits() == null ? 0 : dto.getTotalUnits());
		book.setAvailableUnits(dto.getAvailableUnits() == null
			? (dto.getTotalUnits() == null ? 0 : dto.getTotalUnits())
			: dto.getAvailableUnits());
		return book;
	}

	public static BookDTO toDto(Book book) {
		BookDTO dto = new BookDTO();
		dto.setId(book.getId());
		dto.setTitle(book.getTitle());
		dto.setAuthor(book.getAuthor());
		dto.setTotalUnits(book.getTotalUnits());
		dto.setAvailableUnits(book.getAvailableUnits());
		dto.setAvailable(book.getAvailableUnits() > 0);
		return dto;
	}
}
