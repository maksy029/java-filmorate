package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
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
        if (validator(user)) {
            if (user.getName() == null || user.getName().equals("")) {
                user.setName(user.getLogin());
            }
            user.setId(id++);
            log.debug("Добавлен новый пользователь: {}", user);
            users.put(user.getId(), user);
        } else {
            notValid(user);
        }
        return user;
    }

    @Override
    public User update(User user) {
        if (validator(user) && users.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().equals("")) {
                user.setName(user.getLogin());
            }
            log.debug("Обновлены данные пользователя: {}", user);
            users.put(user.getId(), user);
        } else {
            if (!users.containsKey(user.getId())) {
                log.debug("Ошибка при обновлении пользователя, некорректный id= {}", user.getId());
                throw new UserNotFoundException("Ошибка при обновлении пользователя, некорректный id= " + user.getId());
            }
            notValid(user);
        }
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

    private boolean validator(User user) {
        return !user.getEmail().isEmpty()
                && user.getEmail().contains("@")
                && !user.getLogin().isEmpty()
                && !user.getLogin().contains(" ")
                && !user.getBirthday().isAfter(LocalDate.now());
    }

    private void notValid(User user) {
        if (user.getEmail().isEmpty()) {
            log.debug("Ошибка валидации данных пользователя, пустой email= {}", user.getEmail());
            throw new ValidationException("Ошибка валидации данных пользователя, пустой email= " + user.getEmail());
        }
        if (!user.getEmail().contains("@")) {
            log.debug("Ошибка валидации данных пользователя,в поле email нет '@' = {}", user.getEmail());
            throw new ValidationException("Ошибка валидации данных пользователя,в поле email нет '@' = "
                    + user.getEmail());
        }
        if (user.getLogin().isEmpty()) {
            log.debug("Ошибка валидации данных пользователя, пустое поле login= {}", user.getLogin());
            throw new ValidationException("Ошибка валидации данных пользователя, пустое поле login= "
                    + user.getLogin());
        }
        if (user.getLogin().contains(" ")) {
            log.debug("Ошибка валидации данных пользователя, пробелы в поле login= {}", user.getLogin());
            throw new ValidationException("Ошибка валидации данных пользователя, пробелы в поле login= "
                    + user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Ошибка валидации данных пользователя, будущая дата рождения= {}", user.getBirthday());
            throw new ValidationException("Ошибка валидации данных пользователя, будущая дата рождения= "
                    + user.getBirthday());
        }
    }
}
