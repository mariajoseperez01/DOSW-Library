package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
class LoanServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    private String bookId;
    private String userId;

    @BeforeEach
    void setUp() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        Book book = new Book();
        book.setTitle("Refactoring");
        book.setAuthor("Martin Fowler");
        book.setTotalUnits(1);
        book.setAvailableUnits(1);
        bookId = bookService.addBook(book).getId();

        User user = new User();
        user.setName("Ana");
        user.setUsername("ana");
        user.setPassword("secret");
        userId = userService.registerUser(user).getId();
    }

    @AfterEach
    void tearDown() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
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
        second.setTotalUnits(1);
        second.setAvailableUnits(1);
        String secondId = bookService.addBook(second).getId();

        Book third = new Book();
        third.setTitle("Effective Java");
        third.setAuthor("Joshua Bloch");
        third.setTotalUnits(1);
        third.setAvailableUnits(1);
        String thirdId = bookService.addBook(third).getId();

        Book fourth = new Book();
        fourth.setTitle("TDD");
        fourth.setAuthor("Kent Beck");
        fourth.setTotalUnits(1);
        fourth.setAvailableUnits(1);
        String fourthId = bookService.addBook(fourth).getId();

        loanService.createLoan(userId, bookId);
        loanService.createLoan(userId, secondId);
        loanService.createLoan(userId, thirdId);

        assertThrows(LoanLimitExceededException.class, () -> loanService.createLoan(userId, fourthId));
    }
}
