package edu.eci.dosw.tdd.security;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;
import edu.eci.dosw.tdd.persistence.port.UserRepositoryPort;

@Component
public class SecurityDataInitializer implements ApplicationRunner {

	private final UserRepositoryPort userRepository;
	private final UserService userService;

	public SecurityDataInitializer(UserRepositoryPort userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (userRepository.findByName("librarian").isEmpty()) {
			User librarian = new User("librarian", "librarian123", Role.LIBRARIAN, "LIB-1");
			userService.registerUser(librarian);
		}
	}
}
