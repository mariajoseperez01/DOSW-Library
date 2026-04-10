package edu.eci.dosw.tdd.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.DateUtil;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import edu.eci.dosw.tdd.persistence.dao.BookDAO;
import edu.eci.dosw.tdd.persistence.dao.LoanDAO;
import edu.eci.dosw.tdd.persistence.dao.UserDAO;
import edu.eci.dosw.tdd.persistence.mapper.LoanPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;

@Service
public class LoanService {

	private static final int MAX_ACTIVE_LOANS_PER_USER = 3;

	private final BookService bookService;
	private final UserService userService;
	private final LoanRepository loanRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;

	public LoanService(BookService bookService, UserService userService, LoanRepository loanRepository,
			BookRepository bookRepository, UserRepository userRepository) {
		this.bookService = bookService;
		this.userService = userService;
		this.loanRepository = loanRepository;
		this.bookRepository = bookRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public Loan createLoan(String bookId, String userId) {
		LoanValidator.validateLoanRequest(bookId, userId);
		Book book = bookService.getBookById(bookId);
		User user = userService.getUserById(userId);

		if (!book.isAvailable() || bookService.getAvailableCopies(bookId) <= 0) {
			throw new BookNotAvailableException("Book is not available: " + bookId);
		}

		long currentUserLoans = loanRepository.countByUser_IdAndStatus(userId, Loan.Status.ACTIVE);
		if (currentUserLoans >= MAX_ACTIVE_LOANS_PER_USER) {
			throw new LoanLimitExceededException("Loan limit exceeded for user: " + userId);
		}

		LoanDAO loanDAO = new LoanDAO();
		BookDAO bookDAO = bookRepository.findById(book.getId())
			.orElseThrow(() -> new NoSuchElementException("Book not found: " + book.getId()));
		UserDAO userDAO = userRepository.findById(user.getId())
			.orElseThrow(() -> new NoSuchElementException("User not found: " + user.getId()));
		loanDAO.setBook(bookDAO);
		loanDAO.setUser(userDAO);
		loanDAO.setLoanDate(DateUtil.today());
		loanDAO.setStatus(Loan.Status.ACTIVE);
		loanDAO.setReturnDate(null);
		LoanDAO createdLoanDAO = loanRepository.save(loanDAO);

		bookService.decrementCopy(bookId);
		return LoanPersistenceMapper.toModel(createdLoanDAO, bookService.getAvailableCopies(bookId));
	}

	@Transactional
	public Loan returnLoan(String bookId, String userId) {
		LoanValidator.validateLoanRequest(bookId, userId);
		LoanDAO loanDAO = loanRepository.findFirstByBook_IdAndUser_IdAndStatusOrderByLoanDateDesc(bookId, userId, Loan.Status.ACTIVE)
			.orElseThrow(() -> new NoSuchElementException(
				"Active loan not found for book " + bookId + " and user " + userId));

		loanDAO.setStatus(Loan.Status.RETURNED);
		loanDAO.setReturnDate(DateUtil.today());
		LoanDAO returnedLoanDAO = loanRepository.save(loanDAO);
		bookService.incrementCopy(bookId);
		return LoanPersistenceMapper.toModel(returnedLoanDAO, bookService.getAvailableCopies(bookId));
	}

	@Transactional(readOnly = true)
	public List<Loan> getAllLoans() {
		List<Loan> loans = new ArrayList<>();
		for (LoanDAO loanDAO : loanRepository.findAll()) {
			loans.add(LoanPersistenceMapper.toModel(loanDAO, bookService.getAvailableCopies(loanDAO.getBook().getId())));
		}
		return Collections.unmodifiableList(loans);
	}
}
