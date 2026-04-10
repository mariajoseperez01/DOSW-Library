package edu.eci.dosw.tdd.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.dao.LoanDAO;

public interface LoanRepository extends JpaRepository<LoanDAO, Long> {

	long countByUser_IdAndStatus(String userId, Loan.Status status);

	List<LoanDAO> findAllByUser_Id(String userId);

	Optional<LoanDAO> findFirstByBook_IdAndUser_IdAndStatusOrderByLoanDateDesc(String bookId, String userId, Loan.Status status);
}
