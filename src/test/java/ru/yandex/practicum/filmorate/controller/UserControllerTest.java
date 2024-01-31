package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(UserService userservice);
    }

    @Test
    void validateNegative() {
        User user = User.builder()
                .email("evstigneev@yandex.ru")
                .birthday(LocalDate.of(1992, 11, 1))
                .login("ьщдщвщш")
                .build();
        userController.validate(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void validate() {
        User user = User.builder()
                .name("Alex")
                .email("evstigneev@yandex.ru")
                .birthday(LocalDate.of(1992, 11, 1))
                .login("ьщдщвщш")
                .build();
        userController.validate(user);
        assertEquals("Alex", user.getName());
    }
}