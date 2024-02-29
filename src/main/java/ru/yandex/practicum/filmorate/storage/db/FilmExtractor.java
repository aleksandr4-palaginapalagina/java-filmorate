package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmExtractor implements ResultSetExtractor<List<Film>> {
    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Film> filmList = new ArrayList<>();
        Map<Film, LinkedHashSet<Genre>> filmListMap = new HashMap<>();
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
            filmListMap.putIfAbsent(film, new LinkedHashSet<>());
            Genre genre = Genre.builder()
                    .id(rs.getInt("genre_id"))
                    .name(rs.getString("genre_name"))
                    .build();
            if (genre.getId() != 0) {
                filmListMap.get(film).add(genre);
            }

        }
        for (Film film : filmListMap.keySet()) {
            film.setGenres(filmListMap.get(film));
        }
        filmList.addAll(filmListMap.keySet());
        return filmList;
    }

}
