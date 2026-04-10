package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.User;

public final class UserMapper {

	private UserMapper() {
	}

	public static User toModel(UserDTO dto) {
		if (dto == null) {
			return null;
		}
		User user = new User();
		user.setId(dto.getId());
		user.setName(dto.getName());
		user.setPassword(dto.getPassword());
		user.setRole(dto.getRole() == null ? Role.USER : Role.valueOf(dto.getRole()));
		return user;
	}

	public static UserDTO toDto(User model) {
		if (model == null) {
			return null;
		}
		UserDTO dto = new UserDTO();
		dto.setId(model.getId());
		dto.setName(model.getName());
		dto.setRole(model.getRole() == null ? Role.USER.name() : model.getRole().name());
		return dto;
	}
}
