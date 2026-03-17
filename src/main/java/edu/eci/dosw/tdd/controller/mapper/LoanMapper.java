package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.core.model.Loan;

public final class LoanMapper {

	private LoanMapper() {
	}

	public static LoanDTO toDto(Loan loan) {
		LoanDTO dto = new LoanDTO();
		dto.setId(loan.getId());
		dto.setBookId(loan.getBook() == null ? null : loan.getBook().getId());
		dto.setUserId(loan.getUser() == null ? null : loan.getUser().getId());
		dto.setLoanDate(loan.getLoanDate());
		dto.setReturnDate(loan.getReturnDate());
		dto.setStatus(loan.getStatus() == null ? null : loan.getStatus().name());
		return dto;
	}
}
