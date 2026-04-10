package edu.eci.dosw.tdd.persistence.nonrelational;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.DateUtil;
import edu.eci.dosw.tdd.persistence.mongo.repository.LoanMongoRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import edu.eci.dosw.tdd.persistence.port.BookRepositoryPort;
import edu.eci.dosw.tdd.persistence.port.LoanRepositoryPort;
import edu.eci.dosw.tdd.persistence.port.UserRepositoryPort;

@Repository
@Profile("mongo")
public class MongoLoanRepositoryAdapter implements LoanRepositoryPort {

	private final LoanMongoRepository loanMongoRepository;
	private final BookRepositoryPort bookRepositoryPort;
	private final UserRepositoryPort userRepositoryPort;

	public MongoLoanRepositoryAdapter(LoanMongoRepository loanMongoRepository, BookRepositoryPort bookRepositoryPort, UserRepositoryPort userRepositoryPort) {
		this.loanMongoRepository = loanMongoRepository;
		this.bookRepositoryPort = bookRepositoryPort;
		this.userRepositoryPort = userRepositoryPort;
	}

	@Override
	public Loan save(Loan loan) {
		LoanDocument document = new LoanDocument();
		document.setBookId(loan.getBook().getId());
		document.setUserId(loan.getUser().getId());
		document.setLoanDate(loan.getLoanDate() == null ? DateUtil.today() : loan.getLoanDate());
		document.setStatus(loan.getStatus());
		document.setReturnDate(loan.getReturnDate());
		return toModel(loanMongoRepository.save(document));
	}

	@Override
	public List<Loan> findAll() {
		return loanMongoRepository.findAll().stream().map(this::toModel).toList();
	}

	@Override
	public void deleteAll() {
		loanMongoRepository.deleteAll();
	}

	@Override
	public long countActiveByUserId(String userId) {
		return loanMongoRepository.countByUserIdAndStatus(userId, Loan.Status.ACTIVE);
	}

	@Override
	public List<Loan> findAllByUserId(String userId) {
		return loanMongoRepository.findAllByUserId(userId).stream().map(this::toModel).toList();
	}

	@Override
	public Optional<Loan> findActiveByBookAndUser(String bookId, String userId) {
		return loanMongoRepository.findFirstByBookIdAndUserIdAndStatusOrderByLoanDateDesc(bookId, userId, Loan.Status.ACTIVE).map(this::toModel);
	}

	private Loan toModel(LoanDocument document) {
		Loan loan = new Loan();
		Book book = bookRepositoryPort.findById(document.getBookId()).orElseThrow(() -> new NoSuchElementException("Book not found: " + document.getBookId()));
		User user = userRepositoryPort.findById(document.getUserId()).orElseThrow(() -> new NoSuchElementException("User not found: " + document.getUserId()));
		loan.setBook(book);
		loan.setUser(user);
		loan.setLoanDate(document.getLoanDate());
		loan.setStatus(document.getStatus());
		loan.setReturnDate(document.getReturnDate());
		return loan;
	}
}