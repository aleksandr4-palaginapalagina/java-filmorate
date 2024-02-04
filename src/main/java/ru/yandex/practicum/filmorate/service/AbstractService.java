package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;
import java.util.List;

public abstract class AbstractService<T extends BaseUnit> {

    AbstractStorage<T> storage;

    public T create(T data) {
        validate(data);
        storage.create(data);
        return data;
    }

    public T update(T data) {
        validate(data);
        storage.update(data);
        return data;
    }

    public List<T> getAll() {
        return storage.getAll();
    }

    public T delete(T data) {
        storage.delete(data);
        return data;
    }

    public T get(int id) {
        return (T) storage.get(id);
    }

    abstract void validate(T data);


}
