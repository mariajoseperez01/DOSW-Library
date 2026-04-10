package edu.eci.dosw.tdd.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.eci.dosw.tdd.persistence.dao.BookDAO;

public interface BookRepository extends JpaRepository<BookDAO, String> {
}
