package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmLikesStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final FilmLikesStorage filmLikesStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage
            , UserService userService, FilmLikesStorage filmLikesStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.filmLikesStorage = filmLikesStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film filmById(Integer filmId) {
        log.debug("Запрос на получение фильма с id=" + filmId);
        return filmStorage.filmById(filmId);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public void addLike(Integer filmId, Integer userId) {
        filmLikesStorage.addLikeByFilmId(filmId, userId);
        log.debug("Добавлен лайк к фильму с ID=" + filmId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        filmById(filmId);
        userService.userById(userId);
        filmLikesStorage.deleteLikeByFilmId(filmId, userId);
        log.debug("Удален лайк у фильма с  ID=" + filmId);
    }

    public List<Film> topFilms(Integer count) {
        log.debug("Запрос на получение списка из топ фильмов в количестве count=" + count);
        return filmLikesStorage.topFilms(count);
    }
}
