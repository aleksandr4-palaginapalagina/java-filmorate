package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "select * from mpa";
        return jdbcTemplate.query(sqlQuery, MpaDbStorage::createMpa);
    }

    @Override
    public Mpa get(int id) {
        String sqlQuery = "select * from mpa where mpa_id = ?";
        List<Mpa> mpas = jdbcTemplate.query(sqlQuery, MpaDbStorage::createMpa, id);
        if (mpas.size() != 1) {
            throw new NotFoundException("Find any");
        }
        return mpas.get(0);
    }

    @Override
    public Mpa create(Mpa data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Mpa update(Mpa data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Mpa delete(Mpa data) {
        throw new UnsupportedOperationException();
    }

    static Mpa createMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }

}
