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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;

@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "Operaciones para gestionar libros")
public class BookController {

	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@PostMapping
	@Operation(summary = "Agregar libro", description = "Registra un libro nuevo y su cantidad inicial")
	public BookDTO addBook(@RequestBody BookDTO bookDTO) {
		Book created = bookService.addBook(BookMapper.toModel(bookDTO));
		return BookMapper.toDto(created);
	}

	@GetMapping
	@Operation(summary = "Listar libros", description = "Obtiene todos los libros registrados")
	public List<BookDTO> getAllBooks() {
		return bookService.getAllBooks().stream()
			.map(BookMapper::toDto)
			.toList();
	}

	@GetMapping("/{bookId}")
	@Operation(summary = "Obtener libro por id", description = "Consulta un libro por su identificacion")
	public BookDTO getBookById(@PathVariable String bookId) {
		Book book = bookService.getBookById(bookId);
		return BookMapper.toDto(book);
	}

	@PatchMapping("/{bookId}/availability")
	@Operation(summary = "Actualizar disponibilidad", description = "Actualiza la disponibilidad de un libro")
	public BookDTO updateAvailability(@PathVariable String bookId, @RequestParam boolean available) {
		bookService.updateAvailability(bookId, available);
		Book book = bookService.getBookById(bookId);
		return BookMapper.toDto(book);
	}
}
