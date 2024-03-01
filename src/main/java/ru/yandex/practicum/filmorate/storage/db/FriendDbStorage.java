package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(int idUser, int idFriend) {

        String sqlQuery = "merge into FRIENDS (user_id, friend_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, idUser, idFriend);

    }

    @Override
    public void removeFriend(int idUser, int idFriend) {
        String sqlQuery = "delete from FRIENDS where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, idUser, idFriend);
    }

    @Override
    public List<User> getFriends(int idUser) {
        String sqlQuery = "select * from USERS where id IN (select friend_id from FRIENDS where user_id = ?)";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::createUser, idUser);
    }

    @Override
    public List<User> getMutualFriends(int idUser, int idFriend) {
        List<User> mutualfriends = new ArrayList<>();
        String sqlQuery = "select * from USERS where id IN (select friend_id from FRIENDS where user_id = ?)";
        List<User> friendsUsers = jdbcTemplate.query(sqlQuery, UserDbStorage::createUser, idUser);
        List<User> friendsFriend = jdbcTemplate.query(sqlQuery, UserDbStorage::createUser, idFriend);
        for (User users : friendsFriend) {
            if (friendsUsers.contains(users)) {
                mutualfriends.add(users);
            }
        }
        return mutualfriends;
    }

}
