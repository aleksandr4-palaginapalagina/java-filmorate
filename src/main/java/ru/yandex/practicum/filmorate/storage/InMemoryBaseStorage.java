package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
abstract class InMemoryBaseStorage<T extends BaseUnit> implements AbstractStorage<T> {

    private Map<Integer, T> storage = new HashMap<>();
    private int generateId = 0;

    @Override
    public T create(T data) {
        data.setId(++generateId);
        storage.put(data.getId(), data);
        return data;
    }

    @Override
    public T update(T data) {
        if (!storage.containsKey(data.getId())) {
            throw new NotFoundException("Не найден айди " + data.getId());
        }
        storage.put(data.getId(), data);
        return data;
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public T delete(T data) {
        if (!storage.containsKey(data.getId())) {
            throw new NotFoundException("Не найден айди " + data.getId());
        }
        storage.remove(data.getId());
        return data;
    }

    @Override
    public T get(int id) {
        if (!storage.containsKey(id)) {
            throw new NotFoundException("Не найден айди " + id);
        }
        return storage.get(id);
    }

}
