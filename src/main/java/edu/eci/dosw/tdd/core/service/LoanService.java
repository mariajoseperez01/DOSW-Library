package edu.eci.dosw.tdd.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.DateUtil;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.validator.LoanValidator;

@Service
public class LoanService {

	private static final int MAX_ACTIVE_LOANS_PER_USER = 3;

	private final List<Loan> loans = new ArrayList<>();
	private final BookService bookService;
	private final UserService userService;

	public LoanService(BookService bookService, UserService userService) {
		this.bookService = bookService;
		this.userService = userService;
	}

	public Loan createLoan(String userId, String bookId) {
		User user = userService.getUserById(userId);
		Book book = bookService.getBookById(bookId);

		long activeByUser = loans.stream()
			.filter(loan -> loan.getUser() != null
				&& userId.equals(loan.getUser().getId())
				&& loan.getStatus() == Status.ACTIVE)
			.count();

		if (activeByUser >= MAX_ACTIVE_LOANS_PER_USER) {
			throw new LoanLimitExceededException("User reached max active loans");
		}

		bookService.decreaseStock(bookId);

		Loan loan = new Loan();
		loan.setId(IdGeneratorUtil.generateId());
		loan.setUser(user);
		loan.setBook(book);
		loan.setLoanDate(DateUtil.today());
		loan.setStatus(Status.ACTIVE);

		LoanValidator.validate(loan);
		loans.add(loan);
		return loan;
	}

	public Loan returnLoan(String loanId) {
		Loan loan = loans.stream()
			.filter(item -> loanId.equals(item.getId()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Loan not found: " + loanId));

		if (loan.getStatus() == Status.RETURNED) {
			throw new IllegalArgumentException("Loan is already returned");
		}

		loan.setStatus(Status.RETURNED);
		loan.setReturnDate(DateUtil.today());
		bookService.increaseStock(loan.getBook().getId());
		return loan;
	}

	public List<Loan> getAllLoans() {
		return new ArrayList<>(loans);
	}
}
