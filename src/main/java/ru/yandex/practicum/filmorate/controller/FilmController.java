package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;


    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAll();
    }

    @PostMapping
    public Film createFilms(@Valid @RequestBody Film film) {
        log.info("Фильм успешно добавлен - {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film upadteFilms(@Valid @RequestBody Film film) {
        log.info("Фильм успешно обновлен {}", film);
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        log.info("Запрошен фильм с id - {}", id);
        return filmService.get(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info(("Фильму с id {}, поставил лайк пользователь с id {}"), id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info(("Фильму с id {}, удалил лайк пользователь с id {}"), id, userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("запрошены популярные фильмы в количестве - {}", count);
        return filmService.getPopular(count);
    }

}
