package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
@Deprecated
public class InMemoryFilmStorage extends InMemoryBaseStorage<Film> implements FilmStorage {

}
