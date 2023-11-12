package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class UserService {
    public UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User addFriend(Integer userId, Integer friendId) {
        User user = userById(userId);
        User friend = userById(friendId);
        user.getFriends().add(friendId);
        update(user);
        friend.getFriends().add(userId);
        update(friend);
        log.debug("Пользователю с id=" + userId + " добавлен друг с id=" + friendId);
        return user;
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        User user = userById(userId);
        User friend = userById(friendId);
        user.getFriends().remove(friendId);
        update(user);
        friend.getFriends().remove(userId);
        update(friend);
        log.debug("У пользователя с id=" + userId + " удален друг с id=" + friendId);
        return user;
    }

    public List<User> userFriends(Integer userId) {
        Set<Integer> friendsIds = userById(userId).getFriends();
        Collection<User> users = findAll();
        List<User> userFriends = new ArrayList<>();
        for (Integer friendsId : friendsIds) {
            if (users.contains(userById(friendsId))) {
                userFriends.add(userById(friendsId));
            }
        }
        log.debug("Список друзей пользовтеля с id=" + userId + ": " + userFriends);
        return userFriends;
    }

    public List<User> commonFriends(Integer userId, Integer otherId) {
        Set<Integer> userIds = userById(userId).getFriends();
        Set<Integer> otherIds = userById(otherId).getFriends();
        List<User> commonFriends = new ArrayList<>();
        for (Integer id : otherIds) {
            if (userIds.contains(id)) {
                commonFriends.add(userById(id));
            }
        }
        log.debug("Список общих друзей пользователя с id=" + userId + " и пользователя с id=" + otherId);
        return commonFriends;
    }

    public User userById(Integer userId) {
        log.debug("Запрос на получение пользователя с id=" + userId);
        return userStorage.userById(userId);
    }
}
