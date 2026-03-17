package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void shouldRegisterAndFindUser() {
        User user = new User();
        user.setName("Maria");

        User created = userService.registerUser(user);

        assertEquals("Maria", userService.getUserById(created.getId()).getName());
        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void shouldFailWhenUserDoesNotExist() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById("missing"));
    }
}
