package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.BaseUnit;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseController<T extends BaseUnit> {

    private Map<Long, T> storage = new HashMap<>();
    private long generateId = 0L;

    public T create(T data) {
        validate(data);
        data.setId(++generateId);
        storage.put(data.getId(), data);
        return data;
    }

    public T update(T data) {
        if (!storage.containsKey(data.getId())) {
            throw new ValidationException("Не найден айди " + data.getId());
        }
        validate(data);
        storage.put(data.getId(), data);
        return data;
    }

    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    abstract void validate(T data);
}