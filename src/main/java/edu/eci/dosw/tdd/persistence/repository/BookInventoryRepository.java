package edu.eci.dosw.tdd.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.eci.dosw.tdd.persistence.dao.BookInventoryDAO;

public interface BookInventoryRepository extends JpaRepository<BookInventoryDAO, String> {
}
