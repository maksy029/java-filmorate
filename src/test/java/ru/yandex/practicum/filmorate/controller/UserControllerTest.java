package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    UserController userController;

    @BeforeEach
    public void init() {
        userController = new UserController();
    }

    User user = User.builder()
            .email("m@ya.ru")
            .login("login")
            .name("m")
            .birthday(LocalDate.of(2023, 1, 1))
            .build();

    @DisplayName("Тест на добавление нового пользователя с верными параметрами")
    @Test
    void shouldCreatedNewUserWhenTrueParam() {
        userController.create(user);

        assertEquals(1, userController.getUsers().size());
    }

    @DisplayName("Тест на добавление нового пользователя с неверным параметром - пустая почта")
    @Test
    void shouldThrowExceptionWhenEmailEmpty() {
        user.setEmail("");

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Ошибка валидации данных пользователя, пустой email= " + user.getEmail(),
                exception.getMessage());
    }

    @DisplayName("Тест на добавление нового пользователя с неверным параметром - почта без @")
    @Test
    void shouldThrowExceptionWhenEmailWithOutA() {
        user.setEmail("mya.ru");

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Ошибка валидации данных пользователя,в поле email нет '@' = "
                + user.getEmail(), exception.getMessage());
    }

    @DisplayName("Тест на добавление нового пользователя с неверным параметром - пустой логин")
    @Test
    void shouldThrowExceptionWhenLoginEmpty() {
        user.setLogin("");

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Ошибка валидации данных пользователя, пустое поле login= "
                + user.getLogin(), exception.getMessage());
    }

    @DisplayName("Тест на добавление нового пользователя с неверным параметром - логин с пробелом")
    @Test
    void shouldThrowExceptionWhenLoginWithSpace() {
        user.setLogin("lo gin");

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Ошибка валидации данных пользователя, пробелы в поле login= "
                + user.getLogin(), exception.getMessage());
    }

    @DisplayName("Тест на добавление нового пользователя с пустым именем")
    @Test
    void shouldCreatedNewUserWhenNameNull() {
        user.setName(null);
        userController.create(user);
        User newUser = userController.getUsers().get(1);

        assertEquals("login", newUser.getName());
    }

    @DisplayName("Тест на добавление нового пользователя с неверным параметром - дата рождения  в будущем")
    @Test
    void shouldThrowExceptionWhenFutureBirthday() {
        user.setBirthday(LocalDate.of(2024, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Ошибка валидации данных пользователя, будущая дата рождения= "
                + user.getBirthday(), exception.getMessage());
    }
}