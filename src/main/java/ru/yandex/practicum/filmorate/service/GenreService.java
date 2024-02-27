package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
public class GenreService extends AbstractService<Genre> {

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.storage = genreStorage;
    }

    @Override
    public List<Genre> getAll() {
        return storage.getAll();
    }

    @Override
    public Genre get(int id) {
        return storage.get(id);
    }


    @Override
    public Genre create(Genre data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Genre update(Genre data) {
        throw new UnsupportedOperationException();
    }


    @Override
    public Genre delete(Genre data) {
        throw new UnsupportedOperationException();
    }


    @Override
    void validate(Genre data) {
        throw new UnsupportedOperationException();
    }
}
