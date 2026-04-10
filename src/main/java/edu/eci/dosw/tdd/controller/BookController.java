package edu.eci.dosw.tdd.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO addBook(@RequestBody BookDTO bookDto, @RequestParam(defaultValue = "1") int copies) {
		Book createdBook = bookService.addBook(BookMapper.toModel(bookDto), copies);
		return BookMapper.toDto(createdBook, bookService.getAvailableCopies(createdBook.getId()));
	}

	@GetMapping
	public List<BookDTO> getAllBooks() {
		return bookService.getAllBooks().stream()
			.map(book -> BookMapper.toDto(book, bookService.getAvailableCopies(book.getId())))
			.toList();
	}

	@GetMapping("/{bookId}")
	public BookDTO getBookById(@PathVariable String bookId) {
		Book book = bookService.getBookById(bookId);
		return BookMapper.toDto(book, bookService.getAvailableCopies(bookId));
	}

	@GetMapping("/inventory")
	public Map<String, Integer> getInventory() {
		return bookService.getBookInventory();
	}

	@PatchMapping("/{bookId}/availability")
	public BookDTO updateAvailability(@PathVariable String bookId, @RequestParam boolean available) {
		Book book = bookService.updateBookAvailability(bookId, available);
		return BookMapper.toDto(book, bookService.getAvailableCopies(bookId));
	}
}