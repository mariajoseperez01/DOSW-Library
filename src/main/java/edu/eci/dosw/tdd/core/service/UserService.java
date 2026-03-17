package edu.eci.dosw.tdd.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import edu.eci.dosw.tdd.core.validator.UserValidator;

@Service
public class UserService {

	private final List<User> users = new ArrayList<>();

	public User registerUser(User user) {
		UserValidator.validate(user);

		if (user.getId() == null || user.getId().isBlank()) {
			user.setId(IdGeneratorUtil.generateId());
		}

		users.add(user);
		return user;
	}

	public List<User> getAllUsers() {
		return new ArrayList<>(users);
	}

	public User getUserById(String userId) {
		ValidationUtil.requireNonBlank(userId, "User id is required");
		return users.stream()
			.filter(user -> userId.equals(user.getId()))
			.findFirst()
			.orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
	}
}
