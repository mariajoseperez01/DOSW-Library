package edu.eci.dosw.tdd.persistence.mongo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;

public interface LoanMongoRepository extends MongoRepository<LoanDocument, String> {

	long countByUserIdAndStatus(String userId, Loan.Status status);

	List<LoanDocument> findAllByUserId(String userId);

	Optional<LoanDocument> findFirstByBookIdAndUserIdAndStatusOrderByLoanDateDesc(String bookId, String userId, Loan.Status status);
}