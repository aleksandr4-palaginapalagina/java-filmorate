package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.*;

@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;
//    private final FilmExtractor filmExtractor;

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
        String sqlQuery = "select * from films f, mpa m where f.mpa_id = m.mpa_id order by rate desc limit ?";
        List<Film> filmspopulars = jdbcTemplate.query(sqlQuery, FilmDbStorage::createFilm, count);
        if (filmspopulars == null) {
            throw new RuntimeException();
        }
        for (Film film : filmspopulars) {
            film.setGenres(genresFilms(film));
            System.out.println(film.getMpa().getId());
        }
//        String sqlQuery = "select f.id, f.name, f.description, f.release_date, f.rate, m.mpa_id, m.mpa_name, g.genres_id, g.genres_name from films as f join mpa as m on f.mpa_id = m.mpa_id join film_genres as fg on fg.film_id = f.id left join genres as g on g.genres_id = fg.genre_id";
//        Map<Film,List<Genre>> popular = jdbcTemplate.query(sqlQuery, filmExtractor);
//        System.out.println(popular); // Почему не работает extractor? Ошибка в запросе или реализации экстрактора?
//        return new ArrayList<>();
        return filmspopulars;
    }

    private List<Genre> genresFilms(Film data) {
        final int filmId = data.getId();
        String sqlQuery = "select * from genres where genres_id in (select genre_id from film_genres where film_id = ?)";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::createGenre, filmId);
    }
}
