package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.ValidationUtil;

public final class UserValidator {

	private UserValidator() {
	}

	public static void validate(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		ValidationUtil.requireNonBlank(user.getId(), "User id");
		ValidationUtil.requireNonBlank(user.getName(), "User name");
	}
}
