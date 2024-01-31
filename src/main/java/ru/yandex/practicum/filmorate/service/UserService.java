package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService<User> {


    @Autowired
    public UserService(AbstractStorage<User> userStorage) {
        this.storage = userStorage;
    }

    public void addFriends(int id, int friendId) {
        User user1 = storage.get(id);
        User user2 = storage.get(friendId);
        user1.addFriend(friendId);
        user2.addFriend(id);
        storage.update(user1);
        storage.update(user2);

    }

    public void deleteFriends(int id, int friendId) {
        User user1 = storage.get(id);
        User user2 = storage.get(friendId);
        user1.deleteFriend(friendId);
        user2.deleteFriend(id);
        storage.update(user1);
        storage.update(user2);
    }

    public List<User> mutualFriends(int id, int friendId) {

        Set<Integer> allFriends = new HashSet<>();
        Set<Integer> friends = storage.get(id).getFriends();
        Set<Integer> friendsUser2 = storage.get(friendId).getFriends();
        for (Integer integer : friends) {
            if (friendsUser2.contains(integer)) {
                allFriends.add(integer);
            }
        }
        List<User> mutualFriends = allFriends.stream().map(x -> storage.get(x)).collect(Collectors.toList());
        return mutualFriends;
    }

    public List<User> getFriendsUser(int id) {
        Set<Integer> userFriend = storage.get(id).getFriends();
        List<User> friends = userFriend.stream().map(x -> storage.get(x)).collect(Collectors.toList());
        return friends;
    }

    @Override
    void validate(User data) {
        if (data.getName() == null || data.getName().isBlank()) {
            data.setName(data.getLogin());
        }
    }

}
