package edu.eci.dosw.tdd.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.eci.dosw.tdd.persistence.dao.UserDAO;

public interface UserRepository extends JpaRepository<UserDAO, String> {
}
