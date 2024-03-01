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
        String sqlQuery = "select * from films join mpa on (mpa_id = mpa.id) left join film_genres on (film_id = films.id) left join genres on (genre_id = genres.id) order by films.rate desc limit ?";
        return jdbcTemplate.query(sqlQuery, filmExtractor, count);


    }


}
