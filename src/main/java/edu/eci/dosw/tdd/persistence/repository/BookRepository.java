package edu.eci.dosw.tdd.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.eci.dosw.tdd.persistence.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, String> {
}
