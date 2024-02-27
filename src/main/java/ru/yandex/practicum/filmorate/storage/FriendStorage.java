package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    void addFriend(int idUser, int idFriend);

    void removeFriend(int idUser, int idFriend);

    List<User> getFriends(int idUser);

    List<User> getMutualFriends(int idUser, int idFriend);
}
