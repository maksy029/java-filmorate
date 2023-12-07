package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User userById(@PathVariable("id") Integer userId) {
        return userService.userById(userId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validator(user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        validator(user);
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> userFriends(@PathVariable("id") Integer userId) {
        return userService.userFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable("id") Integer userId, @PathVariable("otherId") Integer otherId) {
        return userService.commonFriends(userId, otherId);
    }

    private void validator(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        if (user.getEmail().isEmpty()) {
            log.warn("Ошибка валидации данных пользователя, пустой email= {}", user.getEmail());
            throw new ValidationException("Ошибка валидации данных пользователя, пустой email= " + user.getEmail());
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Ошибка валидации данных пользователя,в поле email нет '@' = {}", user.getEmail());
            throw new ValidationException("Ошибка валидации данных пользователя,в поле email нет '@' = "
                    + user.getEmail());
        }
        if (user.getLogin().isEmpty()) {
            log.warn("Ошибка валидации данных пользователя, пустое поле login= {}", user.getLogin());
            throw new ValidationException("Ошибка валидации данных пользователя, пустое поле login= "
                    + user.getLogin());
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Ошибка валидации данных пользователя, пробелы в поле login= {}", user.getLogin());
            throw new ValidationException("Ошибка валидации данных пользователя, пробелы в поле login= "
                    + user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка валидации данных пользователя, будущая дата рождения= {}", user.getBirthday());
            throw new ValidationException("Ошибка валидации данных пользователя, будущая дата рождения= "
                    + user.getBirthday());
        }
    }
}
