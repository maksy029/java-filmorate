package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        user.setId(id++);
        log.debug("Добавлен новый пользователь: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.debug("Ошибка при обновлении пользователя, некорректный id= {}", user.getId());
            throw new UserNotFoundException("Ошибка при обновлении пользователя, некорректный id= " + user.getId());
        }
        log.debug("Обновлены данные пользователя: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User userById(Integer userId) {
        User user = users.get(userId);
        if (user == null) {
            log.warn("Не найден пользователь с ID=" + userId);
            throw new UserNotFoundException("Не найден пользователь с ID=" + userId);
        }
        return user;
    }
}
