package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.util.ValidationUtil;

public final class LoanValidator {

	private LoanValidator() {
	}

	public static void validate(Loan loan) {
		ValidationUtil.requireNotNull(loan, "Loan is required");
		ValidationUtil.requireNotNull(loan.getBook(), "Loan book is required");
		ValidationUtil.requireNotNull(loan.getUser(), "Loan user is required");
		ValidationUtil.requireNotNull(loan.getLoanDate(), "Loan date is required");
		ValidationUtil.requireNotNull(loan.getStatus(), "Loan status is required");
	}
}
