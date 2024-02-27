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
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

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

    private List<Genre> genresFilms(Film data) {
        final int filmId = data.getId();
        String sqlQuery = "select * from genres where genres_id in (select genre_id from film_genres where film_id = ?)";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::createGenre, filmId);
    }

    private void saveGenres(Film data) {
        final int filmId = data.getId();
        jdbcTemplate.update("delete from film_genres where film_id = ?", filmId);

        final Set<Genre> genreSet = new HashSet<>(data.getGenres());
        final List<Genre> genreArrayList = new ArrayList<>(genreSet);
        if (genreArrayList == null || genreArrayList.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                "MERGE into film_genres (film_id, genre_id) values (?, ?)",
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
        System.out.println(genreArrayList);

    }

    @Override
    public List<Film> getAll() {
        List<Film> films = jdbcTemplate.query("select * from films f, mpa m where f.mpa_id = m.mpa_id", FilmDbStorage::createFilm);
        if (films == null) {
            throw new RuntimeException();
        }
        for (Film film : films) {
            film.setGenres(genresFilms(film));
            System.out.println(film.getMpa().getId());
        }

        return films;
    }


    @Override
    public Film delete(Film data) {
        String sqlQuery = "delete from films where id = ?";
        jdbcTemplate.update(sqlQuery, data.getId());
        return data;
    }

    @Override
    public Film get(int id) {
        String sqlQuery = "select * from films as f join mpa as m on f.mpa_id = m.mpa_id where f.id = ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::createFilm, id);
        if (films.size() != 1) {
            throw new NotFoundException("Find any");
        }
        Film film = films.get(0);
        film.setGenres(genresFilms(film));
        return film;
    }

    static Film createFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate"))
                .mpa(Mpa.builder()
                        .id(rs.getInt("mpa_id"))
                        .name(rs.getString("mpa_name"))
                        .build())
                .build();
    }
}
