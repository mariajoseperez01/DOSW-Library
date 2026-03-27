package edu.eci.dosw.tdd.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.model.UserRole;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import edu.eci.dosw.tdd.persistence.mapper.UserPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;

@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final UserPersistenceMapper userPersistenceMapper;

	public UserService(UserRepository userRepository, UserPersistenceMapper userPersistenceMapper) {
		this.userRepository = userRepository;
		this.userPersistenceMapper = userPersistenceMapper;
	}

	public User registerUser(User user) {
		UserValidator.validate(user);
		ValidationUtil.requireTrue(!userRepository.existsByUsername(user.getUsername()),
			"Username already exists: " + user.getUsername());

		if (user.getId() == null || user.getId().isBlank()) {
			user.setId(IdGeneratorUtil.generateId());
		}
		if (user.getRole() == null) {
			user.setRole(UserRole.USER);
		}

		return userPersistenceMapper.toDomain(userRepository.save(userPersistenceMapper.toEntity(user)));
	}

	@Transactional(readOnly = true)
	public List<User> getAllUsers() {
		return userRepository.findAll().stream().map(userPersistenceMapper::toDomain).toList();
	}

	@Transactional(readOnly = true)
	public User getUserById(String userId) {
		ValidationUtil.requireNonBlank(userId, "User id is required");
		return userRepository.findById(userId)
			.map(userPersistenceMapper::toDomain)
			.orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
	}
}
