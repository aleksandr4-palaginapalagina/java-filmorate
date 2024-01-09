package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void negativeValidate() {
        Film film = Film.builder()
                .name("Terminator")
                .description("Description")
                .releaseDate(LocalDate.of(1800, 9, 11))
                .duration(10)
                .build();
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    void validate() {
        Film film = Film.builder()
                .name("Terminator")
                .description("Description")
                .releaseDate(LocalDate.of(1999, 9, 11))
                .duration(10)
                .build();
        filmController.validate(film);
    }
}