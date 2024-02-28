package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class FilmExtractor implements ResultSetExtractor<Map<Film, List<Genre>>> {
    @Override
    public Map<Film, List<Genre>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Film, List<Genre>> films = new LinkedHashMap<>();
        System.out.println("получение популярных фильмов в экстракте");
        while (rs.next()) {

            Film film = Film.builder()
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
            System.out.println(film);
            films.putIfAbsent(film, new LinkedList<>());
            Genre genre = Genre.builder()
                    .id(rs.getInt("genres_id"))
                    .name(rs.getString("genres_name"))
                    .build();
            films.get(film).add(genre);

        }
        return films;
    }
}

