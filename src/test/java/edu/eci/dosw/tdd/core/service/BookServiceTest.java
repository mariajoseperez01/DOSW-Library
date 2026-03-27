package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;

@SpringBootTest
@ActiveProfiles("test")
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void shouldAddBookAndGetById() {
        Book book = new Book();
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setTotalUnits(2);
        book.setAvailableUnits(2);

        Book created = bookService.addBook(book);

        assertEquals("Clean Code", bookService.getBookById(created.getId()).getTitle());
        assertEquals(2, bookService.getBookQuantity(created.getId()));
        assertTrue(bookService.isBookAvailable(created.getId()));
    }

    @Test
    void shouldUpdateAvailabilityToFalse() {
        Book book = new Book();
        book.setTitle("DDD");
        book.setAuthor("Eric Evans");
        book.setTotalUnits(1);
        book.setAvailableUnits(1);

        Book created = bookService.addBook(book);
        bookService.updateAvailability(created.getId(), false);

        assertEquals(0, bookService.getBookQuantity(created.getId()));
        assertFalse(bookService.isBookAvailable(created.getId()));
    }

    @Test
    void shouldFailWhenBookDoesNotExist() {
        assertThrows(BookNotAvailableException.class, () -> bookService.getBookById("missing"));
    }
}
