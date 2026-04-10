package edu.eci.dosw.tdd.persistence.port;

import java.util.List;
import java.util.Optional;

public interface RepositoryPort<T, ID> {

	T save(T entity);

	Optional<T> findById(ID id);

	List<T> findAll();

	void deleteAll();
}