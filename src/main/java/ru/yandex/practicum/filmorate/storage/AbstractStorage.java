package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.BaseUnit;
import java.util.List;

public interface AbstractStorage<T extends BaseUnit> {

    T create(T data);

    T update(T data);

    List<T> getAll();

    T delete(T data);

    T get(int id);
}
