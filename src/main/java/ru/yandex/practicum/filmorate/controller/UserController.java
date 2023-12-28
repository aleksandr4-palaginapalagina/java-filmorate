package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController extends BaseController<User> {

    @GetMapping
    public List<User> getAllUsers() {
        return super.getAll();
    }

    @PostMapping
    public User createUsers(@Valid @RequestBody User user) {
        log.info("Пользователь создан {}", user);
        return super.create(user);
    }

    @PutMapping
    public User updateUsers(@Valid @RequestBody User user) {
        log.info("Пользователь обновлен {}", user);
        return super.update(user);
    }

    @Override
    void validate(User data) {
        if (data.getName() == null) {
            data.setName(data.getLogin());
        }

    }
}
