package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.core.model.Loan;

public final class LoanMapper {

	private LoanMapper() {
	}

	public static LoanDTO toDto(Loan model) {
		if (model == null) {
			return null;
		}
		LoanDTO dto = new LoanDTO();
		dto.setBookId(model.getBook().getId());
		dto.setUserId(model.getUser().getId());
		dto.setLoanDate(model.getLoanDate());
		dto.setStatus(model.getStatus().name());
		dto.setReturnDate(model.getReturnDate());
		return dto;
	}
}
