package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User data) {
        String sqlQuery = "insert into users(email, login, name, birthday) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();


        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(sqlQuery, new String[]{"id"});
            statement.setString(1, data.getEmail());
            statement.setString(2, data.getLogin());
            statement.setString(3, data.getName());
            final LocalDate birthday = data.getBirthday();
            if (birthday == null) {
                statement.setNull(4, Types.DATE);
            } else {
                statement.setDate(4, Date.valueOf(birthday));
            }
            return statement;
        }, keyHolder);
        data.setId(keyHolder.getKey().intValue());
        return data;
    }

    @Override
    public User update(User data) {
        get(data.getId());
        String sqlQuery = "update users set email = ?, login =?, name = ?, birthday = ? where id = ?";
        jdbcTemplate.update(sqlQuery, data.getEmail(), data.getLogin(), data.getName(), data.getBirthday(), data.getId());
        return data;
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::createUser);
    }

    @Override
    public User delete(User data) {
        String sqlQuery = "delete from users where id = ?";
        jdbcTemplate.update(sqlQuery, data.getId());
        return data;
    }

    @Override
    public User get(int id) {
        String sqlQuery = "select * from users where id = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::createUser, id);
        if (users.size() != 1) {
            throw new NotFoundException("Find any");
        }
        return users.get(0);
    }

    static User createUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
