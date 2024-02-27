package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService extends AbstractService<User> {

    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       FriendStorage friendStorage) {
        this.storage = userStorage;
        this.friendStorage = friendStorage;
    }

    public void addFriends(int id, int friendId) {
        storage.get(id);
        storage.get(friendId);
        friendStorage.addFriend(id, friendId);
    }

    public void deleteFriends(int id, int friendId) {
        friendStorage.removeFriend(id, friendId);
    }

    public List<User> mutualFriends(int id, int friendId) {

        return friendStorage.getMutualFriends(id, friendId);
    }

    public List<User> getFriendsUser(int id) {
        return friendStorage.getFriends(id);
    }

    @Override
    void validate(User data) {
        if (data.getName() == null || data.getName().isBlank()) {
            data.setName(data.getLogin());
        }
    }

}
