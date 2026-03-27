package edu.eci.dosw.tdd.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.controller.mapper.UserMapper;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Operaciones para gestionar usuarios")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	@Operation(summary = "Registrar usuario", description = "Registra un usuario nuevo")
	public UserDTO register(@RequestBody UserDTO userDTO) {
		User created = userService.registerUser(UserMapper.toModel(userDTO));
		return UserMapper.toDto(created);
	}

	@GetMapping
	@Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios registrados")
	public List<UserDTO> getAllUsers() {
		return userService.getAllUsers().stream().map(UserMapper::toDto).toList();
	}

	@GetMapping("/{userId}")
	@Operation(summary = "Obtener usuario por id", description = "Consulta un usuario por su identificacion")
	public UserDTO getUserById(@PathVariable String userId) {
		return UserMapper.toDto(userService.getUserById(userId));
	}
}
