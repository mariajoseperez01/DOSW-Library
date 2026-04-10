package edu.eci.dosw.tdd.core.service;

import java.util.Collections;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import edu.eci.dosw.tdd.persistence.port.UserRepositoryPort;

@Service
public class UserService {

	private final UserRepositoryPort userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public User registerUser(User user) {
		UserValidator.validate(user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public List<User> getAllUsers() {
		return Collections.unmodifiableList(userRepository.findAll());
	}

	public User getUserById(String userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
	}
}
