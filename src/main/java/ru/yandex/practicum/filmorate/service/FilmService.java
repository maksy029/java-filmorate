package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = filmById(filmId);
        Collection<User> users = userService.findAll();
        if (!users.contains(userService.userById(userId))) {
            log.warn("Не найден пользователь с ID=" + userId);
            throw new UserNotFoundException("Не найден пользователь с ID=" + userId);
        }
        film.getLikes().add(userId);
        update(film);
        log.debug("Добавлен лайк к фильму с name=" + film.getName());
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = filmById(filmId);
        Collection<User> users = userService.findAll();
        if (!users.contains(userService.userById(userId))) {
            log.warn("Не найден пользователь с ID=" + userId);
            throw new UserNotFoundException("Не найден пользователь с ID=" + userId);
        }
        film.getLikes().remove(userId);
        update(film);
        log.debug("Удален лайк у фильма с  name=" + film.getName());
        return film;
    }

    public List<Film> topFilms(Integer count) {
        log.debug("Запрос на получение списка из топ фильмов в количестве count=" + count);
        return findAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film filmById(Integer filmId) {
        log.debug("Запрос на получение фильма с id=" + filmId);
        return filmStorage.filmById(filmId);
    }
}
