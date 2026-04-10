package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.core.model.Book;

public final class BookMapper {

	private BookMapper() {
	}

	public static Book toModel(BookDTO dto) {
		if (dto == null) {
			return null;
		}
		return new Book(dto.getTitle(), dto.getAuthor(), dto.getId(), dto.isAvailable());
	}

	public static BookDTO toDto(Book model, Integer copies) {
		if (model == null) {
			return null;
		}
		BookDTO dto = new BookDTO();
		dto.setId(model.getId());
		dto.setTitle(model.getTitle());
		dto.setAuthor(model.getAuthor());
		dto.setAvailable(model.isAvailable());
		dto.setCopies(copies);
		return dto;
	}
}
