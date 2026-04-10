package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.dao.UserDAO;

public final class UserPersistenceMapper {

	private UserPersistenceMapper() {
	}

	public static UserDAO toDao(User user) {
		UserDAO dao = new UserDAO();
		dao.setId(user.getId());
		dao.setName(user.getName());
		return dao;
	}

	public static User toModel(UserDAO dao) {
		User user = new User();
		user.setId(dao.getId());
		user.setName(dao.getName());
		return user;
	}
}
