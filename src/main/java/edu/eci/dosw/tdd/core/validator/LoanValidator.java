package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.util.ValidationUtil;

public final class LoanValidator {

	private LoanValidator() {
	}

	public static void validateLoanRequest(String bookId, String userId) {
		ValidationUtil.requireNonBlank(bookId, "Book id");
		ValidationUtil.requireNonBlank(userId, "User id");
	}
}
