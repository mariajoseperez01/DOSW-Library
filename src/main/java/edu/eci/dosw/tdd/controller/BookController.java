package edu.eci.dosw.tdd.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@PostMapping
	public BookDTO addBook(@RequestBody BookDTO bookDTO) {
		int quantity = bookDTO.getQuantity() == null ? 1 : bookDTO.getQuantity();
		Book created = bookService.addBook(BookMapper.toModel(bookDTO), quantity);
		return BookMapper.toDto(created, bookService.getBookQuantity(created.getId()));
	}

	@GetMapping
	public List<BookDTO> getAllBooks() {
		return bookService.getAllBooks().stream()
			.map(book -> BookMapper.toDto(book, bookService.getBookQuantity(book.getId())))
			.toList();
	}

	@GetMapping("/{bookId}")
	public BookDTO getBookById(@PathVariable String bookId) {
		Book book = bookService.getBookById(bookId);
		return BookMapper.toDto(book, bookService.getBookQuantity(bookId));
	}

	@PatchMapping("/{bookId}/availability")
	public BookDTO updateAvailability(@PathVariable String bookId, @RequestParam boolean available) {
		bookService.updateAvailability(bookId, available);
		Book book = bookService.getBookById(bookId);
		return BookMapper.toDto(book, bookService.getBookQuantity(bookId));
	}
}
