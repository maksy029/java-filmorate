package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
@Data
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {

        if (validator(user)) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            user.setId(id++);
            log.info("Добавлен новый пользователь: {}", user);
            users.put(user.getId(), user);
        } else {
            log.info("Ошибка валидации при добавление нового пользователя: {}", user);
            throw new ValidationException("Ошибка валидации при добавление нового пользователя");
        }

        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {

        if (validator(user) && users.containsKey(user.getId())) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            log.info("Обновлены данные пользователя: {}", user);
            users.put(user.getId(), user);
        } else {
            log.info("Ошибка валидации при обновление пользователя: {}", user);
            throw new ValidationException("Ошибка валидации при добавление нового пользователя");
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
}
