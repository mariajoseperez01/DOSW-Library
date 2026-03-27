package edu.eci.dosw.tdd.persistence.mapper;

import org.springframework.stereotype.Component;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.entity.LoanEntity;

@Component
public class LoanPersistenceMapper {

    private final BookPersistenceMapper bookPersistenceMapper;
    private final UserPersistenceMapper userPersistenceMapper;

    public LoanPersistenceMapper(BookPersistenceMapper bookPersistenceMapper, UserPersistenceMapper userPersistenceMapper) {
        this.bookPersistenceMapper = bookPersistenceMapper;
        this.userPersistenceMapper = userPersistenceMapper;
    }

    public Loan toDomain(LoanEntity entity) {
        if (entity == null) {
            return null;
        }
        Loan domain = new Loan();
        domain.setId(entity.getId());
        domain.setUser(userPersistenceMapper.toDomain(entity.getUser()));
        domain.setBook(bookPersistenceMapper.toDomain(entity.getBook()));
        domain.setLoanDate(entity.getLoanDate());
        domain.setReturnDate(entity.getReturnDate());
        domain.setStatus(entity.getStatus());
        return domain;
    }

    public LoanEntity toEntity(Loan domain) {
        if (domain == null) {
            return null;
        }
        LoanEntity entity = new LoanEntity();
        entity.setId(domain.getId());
        entity.setUser(userPersistenceMapper.toEntity(domain.getUser()));
        entity.setBook(bookPersistenceMapper.toEntity(domain.getBook()));
        entity.setLoanDate(domain.getLoanDate());
        entity.setReturnDate(domain.getReturnDate());
        entity.setStatus(domain.getStatus());
        return entity;
    }
}
