package edu.eci.dosw.tdd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.controller.mapper.UserMapper;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasRole('LIBRARIAN')")
	public UserDTO registerUser(@RequestBody UserDTO userDto) {
		User registeredUser = userService.registerUser(UserMapper.toModel(userDto));
		return UserMapper.toDto(registeredUser);
	}

	@GetMapping
	@PreAuthorize("hasRole('LIBRARIAN')")
	public List<UserDTO> getAllUsers() {
		return userService.getAllUsers().stream()
			.map(UserMapper::toDto)
			.toList();
	}

	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('LIBRARIAN') or #userId == authentication.principal.id")
	public UserDTO getUserById(@PathVariable String userId) {
		return UserMapper.toDto(userService.getUserById(userId));
	}
}