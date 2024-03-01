package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmExtractor filmExtractor;

    @Override
    public Film create(Film data) {

        String sqlQuery = "insert into films (name, description, release_date, duration, rate, mpa_id) values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(sqlQuery, new String[]{"id"});
            statement.setString(1, data.getName());
            statement.setString(2, data.getDescription());
            statement.setDate(3, Date.valueOf(data.getReleaseDate()));
            statement.setInt(4, data.getDuration());
            statement.setInt(5, data.getRate());
            statement.setInt(6, data.getMpa().getId());
            return statement;
        }, keyHolder);
        data.setId(keyHolder.getKey().intValue());
        saveGenres(data);
        return data;
    }

    @Override
    public Film update(Film data) {
        get(data.getId());
        String sqlQuery = "update films set name = ?, description = ?, release_date = ?, duration = ?, rate = ?, mpa_id = ? where id = ?";
        jdbcTemplate.update(sqlQuery, data.getName(), data.getDescription(), data.getReleaseDate(), data.getDuration(), data.getRate(), data.getMpa().getId(), data.getId());
        saveGenres(data);
        return data;
    }


    private void saveGenres(Film data) {
        final int filmId = data.getId();
        jdbcTemplate.update("delete from film_genres where film_id = ?", filmId);


        final List<Genre> genreArrayList = new ArrayList<>();
        genreArrayList.addAll(data.getGenres());
        if (genreArrayList == null || genreArrayList.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                "insert into film_genres (film_id, genre_id) values (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {

                        ps.setInt(1, filmId);
                        ps.setInt(2, genreArrayList.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genreArrayList.size();
                    }
                }
        );


    }

    @Override
    public List<Film> getAll() {
        List<Film> allFilms = jdbcTemplate.query("select * from films join mpa on (films.mpa_id = mpa.id) left join film_genres on (films.id = film_id) left join genres on (genre_id = genres.id)", filmExtractor);
        List<Film> films = new ArrayList<>();
        if (allFilms.size() > 1) {
            for (int i = allFilms.size() - 1; i > -1; i--) {
                films.add(allFilms.get(i));
            }
            return films;
        }
        return allFilms;
    }


    @Override
    public Film delete(Film data) {
        String sqlQuery = "delete from films where id = ?";
        jdbcTemplate.update(sqlQuery, data.getId());
        return data;
    }

    @Override
    public Film get(int id) {
        String sqlQuery = "select * from films left join mpa on (films.mpa_id = mpa.id) left join film_genres on (films.id = film_genres.film_id) left join genres on (genres.id = film_genres.genre_id) where films.id = ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, filmExtractor, id);
        if (films.size() != 1) {
            throw new NotFoundException("Find any");
        }
        Film film = films.get(0);
        return film;
    }


}
