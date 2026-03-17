package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.model.User;

class LoanServiceTest {

    private BookService bookService;
    private UserService userService;
    private LoanService loanService;
    private String bookId;
    private String userId;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        userService = new UserService();
        loanService = new LoanService(bookService, userService);

        Book book = new Book();
        book.setTitle("Refactoring");
        book.setAuthor("Martin Fowler");
        bookId = bookService.addBook(book, 1).getId();

        User user = new User();
        user.setName("Ana");
        userId = userService.registerUser(user).getId();
    }

    @Test
    void shouldCreateAndReturnLoan() {
        Loan loan = loanService.createLoan(userId, bookId);

        assertEquals(Status.ACTIVE, loan.getStatus());
        assertEquals(0, bookService.getBookQuantity(bookId));

        Loan returned = loanService.returnLoan(loan.getId());

        assertEquals(Status.RETURNED, returned.getStatus());
        assertEquals(1, bookService.getBookQuantity(bookId));
    }

    @Test
    void shouldFailWhenBookIsNotAvailable() {
        loanService.createLoan(userId, bookId);

        assertThrows(BookNotAvailableException.class, () -> loanService.createLoan(userId, bookId));
    }

    @Test
    void shouldFailWhenUserExceedsLoanLimit() {
        Book second = new Book();
        second.setTitle("Patterns");
        second.setAuthor("GoF");
        String secondId = bookService.addBook(second, 1).getId();

        Book third = new Book();
        third.setTitle("Effective Java");
        third.setAuthor("Joshua Bloch");
        String thirdId = bookService.addBook(third, 1).getId();

        Book fourth = new Book();
        fourth.setTitle("TDD");
        fourth.setAuthor("Kent Beck");
        String fourthId = bookService.addBook(fourth, 1).getId();

        loanService.createLoan(userId, bookId);
        loanService.createLoan(userId, secondId);
        loanService.createLoan(userId, thirdId);

        assertThrows(LoanLimitExceededException.class, () -> loanService.createLoan(userId, fourthId));
    }
}
