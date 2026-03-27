package edu.eci.dosw.tdd.persistence.mapper;

import org.springframework.stereotype.Component;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;

@Component
public class BookPersistenceMapper {

    public Book toDomain(BookEntity entity) {
        if (entity == null) {
            return null;
        }
        Book domain = new Book();
        domain.setId(entity.getId());
        domain.setTitle(entity.getTitle());
        domain.setAuthor(entity.getAuthor());
        domain.setTotalUnits(entity.getTotalUnits());
        domain.setAvailableUnits(entity.getAvailableUnits());
        return domain;
    }

    public BookEntity toEntity(Book domain) {
        if (domain == null) {
            return null;
        }
        BookEntity entity = new BookEntity();
        entity.setId(domain.getId());
        entity.setTitle(domain.getTitle());
        entity.setAuthor(domain.getAuthor());
        entity.setTotalUnits(domain.getTotalUnits());
        entity.setAvailableUnits(domain.getAvailableUnits());
        return entity;
    }
}
