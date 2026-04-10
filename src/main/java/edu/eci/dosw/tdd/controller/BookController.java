package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.model.Book;
import edu.eci.dosw.tdd.service.LibraryService;
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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private final LibraryService libraryService;

	public BookController(LibraryService libraryService) {
		this.libraryService = libraryService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Book addBook(@RequestBody Book book, @RequestParam(defaultValue = "1") int copies) {
		return libraryService.addBook(book, copies);
	}

	@GetMapping
	public List<Book> getAllBooks() {
		return libraryService.getAllBooks();
	}

	@GetMapping("/{bookId}")
	public Book getBookById(@PathVariable String bookId) {
		return libraryService.getBookById(bookId);
	}

	@GetMapping("/inventory")
	public Map<String, Integer> getInventory() {
		return libraryService.getBookInventory();
	}

	@PatchMapping("/{bookId}/availability")
	public Book updateAvailability(@PathVariable String bookId, @RequestParam boolean available) {
		return libraryService.updateBookAvailability(bookId, available);
	}
}