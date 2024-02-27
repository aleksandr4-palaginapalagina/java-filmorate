package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService extends AbstractService<Film> {

    private final LocalDate releaseDate = LocalDate.of(1895, 12, 28);

    private final LikeStorage likeStorage;

    private final UserStorage userStorage;


    @Autowired
    FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, LikeStorage likeStorage, @Qualifier("userDbStorage") UserStorage userStorage) {

        this.storage = filmStorage;
        this.likeStorage = likeStorage;
        this.userStorage = userStorage;

    }

    public void addLike(int id, int userId) {
        storage.get(id);
        userStorage.get(userId);
        likeStorage.addLike(id, userId);
    }

    public void removeLike(int id, int userId) {

        storage.get(id);
        userStorage.get(userId);
        likeStorage.removeLike(id, userId);
    }

    public List<Film> getPopular(int count) {
        return likeStorage.getPopular(count);
    }


    @Override
    void validate(Film data) {
        if (data.getReleaseDate().isBefore(releaseDate)) {
            throw new ValidationException("Дата релиза некорректна");
        }
    }

}
