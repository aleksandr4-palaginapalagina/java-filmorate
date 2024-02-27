package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<User> getAllUsers() {
        return service.getAll();
    }

    @PostMapping
    public User createUsers(@Valid @RequestBody User user) {
        log.info("Пользователь создан {}", user);
        return service.create(user);
    }

    @PutMapping
    public User updateUsers(@Valid @RequestBody User user) {
        log.info("Пользователь обновлен {}", user);
        return service.update(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        log.info("Запрошен пользователь с id - {}", id);
        return service.get(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info(("Добавление в друзья у пользователей с id {}, {}"), id, friendId);
        service.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info(("Удаление друзей у пользователей с id {}, {}"), id, friendId);
        service.deleteFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> friendsUser(@PathVariable int id) {
        log.info("Запрошены друзья пользователя с id - {}", id);
        return service.getFriendsUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> mutualFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info(("Вывод общих друзей у пользователей с id {}, {}"), id, otherId);
        return service.mutualFriends(id, otherId);
    }

}
