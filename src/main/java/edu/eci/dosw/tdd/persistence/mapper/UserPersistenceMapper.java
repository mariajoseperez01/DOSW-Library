package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.dao.UserDAO;

public final class UserPersistenceMapper {

	private UserPersistenceMapper() {
	}

	public static UserDAO toDao(User user) {
		UserDAO dao = new UserDAO();
		dao.setId(user.getId());
		dao.setName(user.getName());
		dao.setPassword(user.getPassword());
		dao.setRole(user.getRole() == null ? Role.USER : user.getRole());
		return dao;
	}

	public static User toModel(UserDAO dao) {
		User user = new User();
		user.setId(dao.getId());
		user.setName(dao.getName());
		user.setPassword(dao.getPassword());
		user.setRole(dao.getRole());
		return user;
	}
}
