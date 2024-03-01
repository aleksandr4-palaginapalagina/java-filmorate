package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
public class MpaService extends AbstractService<Mpa> {

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.storage = mpaStorage;
    }

    @Override
    public List<Mpa> getAll() {
        return storage.getAll();
    }

    @Override
    public Mpa get(int id) {
        return storage.get(id);
    }

    @Override
    public Mpa create(Mpa data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Mpa update(Mpa data) {
        throw new UnsupportedOperationException();
    }


    @Override
    public Mpa delete(Mpa data) {
        throw new UnsupportedOperationException();
    }


    @Override
    void validate(Mpa data) {
        throw new UnsupportedOperationException();
    }
}
