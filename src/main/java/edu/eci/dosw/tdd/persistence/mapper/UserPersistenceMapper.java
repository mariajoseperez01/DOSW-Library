package edu.eci.dosw.tdd.persistence.mapper;

import org.springframework.stereotype.Component;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.entity.UserEntity;

@Component
public class UserPersistenceMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        User domain = new User();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setUsername(entity.getUsername());
        domain.setPassword(entity.getPassword());
        domain.setRole(entity.getRole());
        return domain;
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setUsername(domain.getUsername());
        entity.setPassword(domain.getPassword());
        entity.setRole(domain.getRole());
        return entity;
    }
}
