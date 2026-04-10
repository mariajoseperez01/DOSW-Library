package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.model.User;
import edu.eci.dosw.tdd.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final LibraryService libraryService;

	public UserController(LibraryService libraryService) {
		this.libraryService = libraryService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User registerUser(@RequestBody User user) {
		return libraryService.registerUser(user);
	}

	@GetMapping
	public List<User> getAllUsers() {
		return libraryService.getAllUsers();
	}

	@GetMapping("/{userId}")
	public User getUserById(@PathVariable String userId) {
		return libraryService.getUserById(userId);
	}
}