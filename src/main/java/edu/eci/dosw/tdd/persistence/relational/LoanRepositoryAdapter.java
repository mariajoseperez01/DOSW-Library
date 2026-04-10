package edu.eci.dosw.tdd.persistence.relational;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.dao.BookDAO;
import edu.eci.dosw.tdd.persistence.dao.LoanDAO;
import edu.eci.dosw.tdd.persistence.dao.UserDAO;
import edu.eci.dosw.tdd.persistence.mapper.LoanPersistenceMapper;
import edu.eci.dosw.tdd.persistence.port.LoanRepositoryPort;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;
import edu.eci.dosw.tdd.core.util.DateUtil;

@Repository
@Profile({"relational", "test"})
public class LoanRepositoryAdapter implements LoanRepositoryPort {

	private final LoanRepository loanRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;

	public LoanRepositoryAdapter(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
		this.loanRepository = loanRepository;
		this.bookRepository = bookRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Loan save(Loan loan) {
		LoanDAO loanDAO = new LoanDAO();
		BookDAO bookDAO = bookRepository.findById(loan.getBook().getId()).orElseThrow(() -> new NoSuchElementException("Book not found: " + loan.getBook().getId()));
		UserDAO userDAO = userRepository.findById(loan.getUser().getId()).orElseThrow(() -> new NoSuchElementException("User not found: " + loan.getUser().getId()));
		loanDAO.setBook(bookDAO);
		loanDAO.setUser(userDAO);
		loanDAO.setLoanDate(loan.getLoanDate() == null ? DateUtil.today() : loan.getLoanDate());
		loanDAO.setStatus(loan.getStatus());
		loanDAO.setReturnDate(loan.getReturnDate());
		LoanDAO saved = loanRepository.save(loanDAO);
		return LoanPersistenceMapper.toModel(saved, 0);
	}

	@Override
	public List<Loan> findAll() {
		return loanRepository.findAll().stream().map(loanDAO -> LoanPersistenceMapper.toModel(loanDAO, 0)).toList();
	}

	@Override
	public void deleteAll() {
		loanRepository.deleteAll();
	}

	@Override
	public long countActiveByUserId(String userId) {
		return loanRepository.countByUser_IdAndStatus(userId, Loan.Status.ACTIVE);
	}

	@Override
	public List<Loan> findAllByUserId(String userId) {
		return loanRepository.findAllByUser_Id(userId).stream().map(loanDAO -> LoanPersistenceMapper.toModel(loanDAO, 0)).toList();
	}

	@Override
	public Optional<Loan> findActiveByBookAndUser(String bookId, String userId) {
		return loanRepository.findFirstByBook_IdAndUser_IdAndStatusOrderByLoanDateDesc(bookId, userId, Loan.Status.ACTIVE)
			.map(loanDAO -> LoanPersistenceMapper.toModel(loanDAO, 0));
	}
}