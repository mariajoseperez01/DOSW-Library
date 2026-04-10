package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.dao.LoanDAO;

public final class LoanPersistenceMapper {

	private LoanPersistenceMapper() {
	}

	public static Loan toModel(LoanDAO dao, int copies) {
		Loan loan = new Loan();
		loan.setBook(BookPersistenceMapper.toModel(dao.getBook(), copies));
		loan.setUser(UserPersistenceMapper.toModel(dao.getUser()));
		loan.setLoanDate(dao.getLoanDate());
		loan.setStatus(dao.getStatus());
		loan.setReturnDate(dao.getReturnDate());
		return loan;
	}
}
