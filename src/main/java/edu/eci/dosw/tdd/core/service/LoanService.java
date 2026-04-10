package edu.eci.dosw.tdd.core.service;

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
import edu.eci.dosw.tdd.persistence.port.BookRepositoryPort;
import edu.eci.dosw.tdd.persistence.port.LoanRepositoryPort;
import edu.eci.dosw.tdd.persistence.port.UserRepositoryPort;

@Service
public class LoanService {

	private static final int MAX_ACTIVE_LOANS_PER_USER = 3;

	private final LoanRepositoryPort loanRepository;
	private final BookRepositoryPort bookRepository;
	private final UserRepositoryPort userRepository;

	public LoanService(LoanRepositoryPort loanRepository, BookRepositoryPort bookRepository, UserRepositoryPort userRepository) {
		this.loanRepository = loanRepository;
		this.bookRepository = bookRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public Loan createLoan(String bookId, String userId) {
		LoanValidator.validateLoanRequest(bookId, userId);
		Book book = bookRepository.findById(bookId)
			.orElseThrow(() -> new NoSuchElementException("Book not found: " + bookId));
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

		if (!book.isAvailable() || bookRepository.getCopies(bookId) <= 0) {
			throw new BookNotAvailableException("Book is not available: " + bookId);
		}

		long currentUserLoans = loanRepository.countActiveByUserId(userId);
		if (currentUserLoans >= MAX_ACTIVE_LOANS_PER_USER) {
			throw new LoanLimitExceededException("Loan limit exceeded for user: " + userId);
		}

		Loan loan = new Loan();
		loan.setBook(book);
		loan.setUser(user);
		loan.setLoanDate(DateUtil.today());
		loan.setStatus(Loan.Status.ACTIVE);
		loan.setReturnDate(null);
		Loan createdLoan = loanRepository.save(loan);

		bookRepository.decrementCopy(bookId);
		createdLoan.setBook(bookRepository.findById(bookId).orElse(book));
		return createdLoan;
	}

	@Transactional
	public Loan returnLoan(String bookId, String userId) {
		LoanValidator.validateLoanRequest(bookId, userId);
		Loan loan = loanRepository.findActiveByBookAndUser(bookId, userId)
			.orElseThrow(() -> new NoSuchElementException("Active loan not found for book " + bookId + " and user " + userId));

		loan.setStatus(Loan.Status.RETURNED);
		loan.setReturnDate(DateUtil.today());
		Loan returnedLoan = loanRepository.save(loan);
		bookRepository.incrementCopy(bookId);
		returnedLoan.setBook(bookRepository.findById(bookId).orElse(loan.getBook()));
		return returnedLoan;
	}

	@Transactional(readOnly = true)
	public List<Loan> getAllLoans() {
		return Collections.unmodifiableList(loanRepository.findAll());
	}

	@Transactional(readOnly = true)
	public List<Loan> getLoansByUserId(String userId) {
		return Collections.unmodifiableList(loanRepository.findAllByUserId(userId));
	}
}
