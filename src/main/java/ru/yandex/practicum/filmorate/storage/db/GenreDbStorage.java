package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre get(int id) {
        String sqlQuery = "select * from genres where genres_id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreDbStorage::createGenre, id);
        if (genres.size() != 1) {
            throw new NotFoundException("Find any");
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "select * from genres";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::createGenre);
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

    static Genre createGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genres_id"))
                .name(rs.getString("genres_name"))
                .build();
    }


}
