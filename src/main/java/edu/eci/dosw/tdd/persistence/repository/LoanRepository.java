package edu.eci.dosw.tdd.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.persistence.entity.LoanEntity;

public interface LoanRepository extends JpaRepository<LoanEntity, String> {

    long countByUserIdAndStatus(String userId, Status status);

    List<LoanEntity> findByUserId(String userId);
}
