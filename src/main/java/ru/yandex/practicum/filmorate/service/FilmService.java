package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService extends AbstractService<Film> {

    private final LocalDate releaseDate = LocalDate.of(1895, 12, 28);
    AbstractStorage userStorage;

    @Autowired
    FilmService(AbstractStorage<Film> storage, AbstractStorage<User> userStorage) {

        this.storage = storage;
        this.userStorage = userStorage;
    }

    public void addLike(int id, int userId) {
        Film film = storage.get(id);
        userStorage.get(userId);
        film.addLike(userId);
        storage.update(film);
    }

    public void removeLike(int id, int userId) {
        Film film = storage.get(id);
        userStorage.get(userId);
        film.removeLike(userId);
        storage.update(film);
    }

    public List<Film> getPopular(int count) {
        return storage.getAll().stream().sorted(FILM_COMPARATOR).limit(count).collect(Collectors.toList());
    }


    public static final Comparator<Film> FILM_COMPARATOR = Comparator.comparingLong(Film::getRate);

    @Override
    void validate(Film data) {
        if (data.getReleaseDate().isBefore(releaseDate)) {
            throw new ValidationException("Дата релиза некорректна");
        }
    }

}
