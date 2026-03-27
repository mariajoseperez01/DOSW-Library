package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.ValidationUtil;

public final class UserValidator {

	private UserValidator() {
	}

	public static void validate(User user) {
		ValidationUtil.requireNotNull(user, "User is required");
		ValidationUtil.requireNonBlank(user.getName(), "User name is required");
		ValidationUtil.requireNonBlank(user.getUsername(), "Username is required");
		ValidationUtil.requireNonBlank(user.getPassword(), "Password is required");
	}
}
