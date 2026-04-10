package edu.eci.dosw.tdd.persistence.port;

import java.util.List;
import java.util.Optional;

import edu.eci.dosw.tdd.core.model.Loan;

public interface LoanRepositoryPort {

	Loan save(Loan loan);

	long countActiveByUserId(String userId);

	List<Loan> findAllByUserId(String userId);

	Optional<Loan> findActiveByBookAndUser(String bookId, String userId);

	List<Loan> findAll();

	void deleteAll();
}