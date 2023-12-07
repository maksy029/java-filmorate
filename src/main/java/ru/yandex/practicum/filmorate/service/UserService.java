package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User userById(Integer userId) {
        return userStorage.userById(userId);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (userById(userId) == null || userById(friendId) == null) {
            throw new UserNotFoundException("Не найдены пользователи с userId="+ userId+" или friendId="+friendId);
        }
            friendsStorage.addFriend(userId, friendId);
        log.debug("Пользователю с id=" + userId + " добавлен друг с id=" + friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        friendsStorage.deleteFriend(userId, friendId);
        log.debug("У пользователя с id=" + userId + " удален друг с id=" + friendId);
    }

    public Collection<User> userFriends(Integer userId) {
        log.debug("Запрошен список друзей пользовтеля с id=" + userId);
        return friendsStorage.getFriendsByUserId(userId);
    }

    public List<User> commonFriends(Integer userId, Integer otherId) {
        return friendsStorage.commonFriends(userId,otherId);
    }
}
