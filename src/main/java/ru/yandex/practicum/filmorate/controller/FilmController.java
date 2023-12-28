package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController extends BaseController<Film>{

    private final static LocalDate START_RELEASE_DATE = LocalDate.of(1895,12,28);

    @GetMapping
    public List<Film> getAllFilms(){
        return super.getAll();
    }

    @PostMapping
    public Film createFilms(@Valid @RequestBody Film film){
        log.info("Фильм успешно добавлен - {}", film);
         return super.create(film);
    }

    @PutMapping
    public Film upadteFilms(@Valid @RequestBody Film film){
        log.info("Фильм успешно обновлен {}", film);
        return  super.update(film);
    }

    @Override
    void validate(Film data) {
        if(data.getReleaseDate().isBefore(START_RELEASE_DATE)){
            throw new ValidationException("Дата релиза некорректна");
        }
    }
}
