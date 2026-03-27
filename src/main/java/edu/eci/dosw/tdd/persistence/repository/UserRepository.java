package edu.eci.dosw.tdd.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.eci.dosw.tdd.persistence.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    boolean existsByUsername(String username);

    Optional<UserEntity> findByUsername(String username);
}
