package edu.eci.dosw.tdd.persistence.port;

import java.util.Optional;

import edu.eci.dosw.tdd.core.model.User;

public interface UserRepositoryPort extends RepositoryPort<User, String> {

	Optional<User> findByName(String name);
}