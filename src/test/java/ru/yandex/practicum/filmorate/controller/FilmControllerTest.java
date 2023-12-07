package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmLikesStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class FilmControllerTest {
    FilmController filmController;
    FilmService filmService;
    FilmStorage filmStorage;
    UserService userService;
    UserStorage userStorage;
    FriendsStorage friendsStorage;
    FilmLikesStorage filmLikesStorage;


    @BeforeEach
    public void init() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage, friendsStorage);
        filmService = new FilmService(filmStorage, userService, filmLikesStorage);
        filmController = new FilmController(filmService);
    }

    Film film = Film.builder()
            .name("фильм")
            .description("описание фильма")
            .releaseDate(LocalDate.of(2020, 12, 31))
            .duration(100)
            .build();


    @DisplayName("Тест на добавление нового фильма с верными параметрами")
    @Test
    void shouldCreatedNewFilmWhenTrueParam() {
        filmController.create(film);
        assertEquals(1, filmController.findAll().size());
    }

    @DisplayName("Тест на добавление нового фильма с неверным параметром - пустое название")
    @Test
    void shouldThrowExceptionWhenFilmNameEmpty() {
        film.setName("");

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Ошибка при валидации фильма, не заполнено поле name= " + film.getName(),
                exception.getMessage());
    }

    @DisplayName("Тест на добавление нового фильма с неверным параметром - длина описания >200")
    @Test
    void shouldThrowExceptionWhenFilmDesc201() {
        StringBuilder desc = new StringBuilder();
        desc.setLength(201);
        film.setDescription(desc.toString());

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Ошибка при валидации фильма, длина поля description >200, а именно= "
                + film.getDescription().length(), exception.getMessage());
    }

    @DisplayName("Тест на добавление нового фильма с неверным параметром - дата релиза < 1895.12.28")
    @Test
    void shouldThrowsExceptionWhenRelease18951227() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Ошибка при валидации фильма, дата релиза раньше 28.12.1895, а именно= "
                + film.getReleaseDate(), exception.getMessage());
    }

    @DisplayName("Тест на добавление нового фильма с неверным параметром - продолжительность <0")
    @Test
    void shouldThrowsExceptionWhenDurationNegative() {
        film.setDuration(-5);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Ошибка при валидации фильма, отрицательная продолжительность= "
                + film.getDuration(), exception.getMessage());
    }
}