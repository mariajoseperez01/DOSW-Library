package edu.eci.dosw.tdd.core.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import edu.eci.dosw.tdd.persistence.mapper.UserPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public User registerUser(User user) {
		UserValidator.validate(user);
		return UserPersistenceMapper.toModel(userRepository.save(UserPersistenceMapper.toDao(user)));
	}

	public List<User> getAllUsers() {
		return Collections.unmodifiableList(userRepository.findAll().stream().map(UserPersistenceMapper::toModel).toList());
	}

	public User getUserById(String userId) {
		return userRepository.findById(userId)
			.map(UserPersistenceMapper::toModel)
			.orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
	}
}
