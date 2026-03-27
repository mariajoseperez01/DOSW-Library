package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.model.UserRole;

public final class UserMapper {

	private UserMapper() {
	}

	public static User toModel(UserDTO dto) {
		User user = new User();
		user.setId(dto.getId());
		user.setName(dto.getName());
		user.setUsername(dto.getUsername());
		user.setPassword(dto.getPassword());
		if (dto.getRole() != null && !dto.getRole().isBlank()) {
			user.setRole(UserRole.valueOf(dto.getRole().toUpperCase()));
		}
		return user;
	}

	public static UserDTO toDto(User user) {
		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setUsername(user.getUsername());
		dto.setRole(user.getRole() == null ? null : user.getRole().name());
		return dto;
	}
}
