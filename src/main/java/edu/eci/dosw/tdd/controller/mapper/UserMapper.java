package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.core.model.User;

public final class UserMapper {

	private UserMapper() {
	}

	public static User toModel(UserDTO dto) {
		if (dto == null) {
			return null;
		}
		return new User(dto.getName(), dto.getId());
	}

	public static UserDTO toDto(User model) {
		if (model == null) {
			return null;
		}
		UserDTO dto = new UserDTO();
		dto.setId(model.getId());
		dto.setName(model.getName());
		return dto;
	}
}
