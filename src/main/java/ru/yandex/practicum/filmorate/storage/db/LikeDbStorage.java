package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.storage.LikeStorage;


import java.util.*;

@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmExtractor filmExtractor;

    @Override
    public void addLike(int filmId, int userId) {
        String sqlQuery = "merge into likes (film_id, user_id) values (?,?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        plusRate(filmId);

    }

    @Override
    public void removeLike(int filmId, int userId) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        minusRate(filmId);

    }

    public void minusRate(int filmId) {
        String sqlQuery = "update films set rate = rate - 1 where id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    public void plusRate(int filmId) {
        String sqlQuery = "update films set rate = rate + 1 where id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public List<Film> getPopular(int count) {
        String sqlQuery = "select f.id, f.name, f.description, f.release_date, f.duration, f.rate, m.id as mpa_id, m.name as mpa_name, g.id as genre_id, g.name as genre_name from films as f left join mpa as m on (f.mpa_id = m.id) left join film_genres as fg on (f.id = fg.film_id) left join genres as g on (fg.genre_id = g.id) order by f.rate desc limit ?";
        return jdbcTemplate.query(sqlQuery, filmExtractor, count);


    }


}
