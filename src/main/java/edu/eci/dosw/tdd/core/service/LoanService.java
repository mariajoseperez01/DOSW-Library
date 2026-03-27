package edu.eci.dosw.tdd.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.DateUtil;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import edu.eci.dosw.tdd.persistence.mapper.LoanPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;

@Service
@Transactional
public class LoanService {

	private static final int MAX_ACTIVE_LOANS_PER_USER = 3;

	private final LoanRepository loanRepository;
	private final LoanPersistenceMapper loanPersistenceMapper;
	private final BookService bookService;
	private final UserService userService;

	public LoanService(LoanRepository loanRepository, LoanPersistenceMapper loanPersistenceMapper,
			BookService bookService, UserService userService) {
		this.loanRepository = loanRepository;
		this.loanPersistenceMapper = loanPersistenceMapper;
		this.bookService = bookService;
		this.userService = userService;
	}

	public Loan createLoan(String userId, String bookId) {
		User user = userService.getUserById(userId);
		Book book = bookService.getBookById(bookId);

		long activeByUser = loanRepository.countByUserIdAndStatus(userId, Status.ACTIVE);

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
		return loanPersistenceMapper.toDomain(loanRepository.save(loanPersistenceMapper.toEntity(loan)));
	}

	public Loan returnLoan(String loanId) {
		Loan loan = loanRepository.findById(loanId)
			.map(loanPersistenceMapper::toDomain)
			.orElseThrow(() -> new IllegalArgumentException("Loan not found: " + loanId));

		if (loan.getStatus() == Status.RETURNED) {
			throw new IllegalArgumentException("Loan is already returned");
		}

		loan.setStatus(Status.RETURNED);
		loan.setReturnDate(DateUtil.today());
		bookService.increaseStock(loan.getBook().getId());
		return loanPersistenceMapper.toDomain(loanRepository.save(loanPersistenceMapper.toEntity(loan)));
	}

	@Transactional(readOnly = true)
	public List<Loan> getAllLoans() {
		return loanRepository.findAll().stream().map(loanPersistenceMapper::toDomain).toList();
	}

	@Transactional(readOnly = true)
	public List<Loan> getLoansByUserId(String userId) {
		return loanRepository.findByUserId(userId).stream().map(loanPersistenceMapper::toDomain).toList();
	}
}
