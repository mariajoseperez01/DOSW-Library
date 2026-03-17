package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;

class BookServiceTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
    }

    @Test
    void shouldAddBookAndGetById() {
        Book book = new Book();
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");

        Book created = bookService.addBook(book, 2);

        assertEquals("Clean Code", bookService.getBookById(created.getId()).getTitle());
        assertEquals(2, bookService.getBookQuantity(created.getId()));
        assertTrue(bookService.isBookAvailable(created.getId()));
    }

    @Test
    void shouldUpdateAvailabilityToFalse() {
        Book book = new Book();
        book.setTitle("DDD");
        book.setAuthor("Eric Evans");

        Book created = bookService.addBook(book, 1);
        bookService.updateAvailability(created.getId(), false);

        assertEquals(0, bookService.getBookQuantity(created.getId()));
        assertFalse(bookService.isBookAvailable(created.getId()));
    }

    @Test
    void shouldFailWhenBookDoesNotExist() {
        assertThrows(BookNotAvailableException.class, () -> bookService.getBookById("missing"));
    }
}
