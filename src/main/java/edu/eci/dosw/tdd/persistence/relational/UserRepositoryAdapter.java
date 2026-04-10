package edu.eci.dosw.tdd.persistence.relational;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.mapper.UserPersistenceMapper;
import edu.eci.dosw.tdd.persistence.port.UserRepositoryPort;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;

@Repository
@Profile({"relational", "test"})
public class UserRepositoryAdapter implements UserRepositoryPort {

	private final UserRepository userRepository;

	public UserRepositoryAdapter(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User save(User entity) {
		return UserPersistenceMapper.toModel(userRepository.save(UserPersistenceMapper.toDao(entity)));
	}

	@Override
	public Optional<User> findById(String id) {
		return userRepository.findById(id).map(UserPersistenceMapper::toModel);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll().stream().map(UserPersistenceMapper::toModel).toList();
	}

	@Override
	public void deleteAll() {
		userRepository.deleteAll();
	}

	@Override
	public Optional<User> findByName(String name) {
		return userRepository.findByName(name).map(UserPersistenceMapper::toModel);
	}
}