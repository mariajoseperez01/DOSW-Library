package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterAndFindUser() {
        User user = new User();
        user.setName("Maria");
        user.setUsername("maria");
        user.setPassword("secret");

        User created = userService.registerUser(user);

        assertEquals("Maria", userService.getUserById(created.getId()).getName());
        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void shouldFailWhenUserDoesNotExist() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById("missing"));
    }
}
