package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        film.setId(id++);
        log.debug("Добавлен новый фильм: {}", film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.debug("Ошибка при обновлении фильма, некорректый id= {}", film.getId());
            throw new FilmNotFoundException("Ошибка при обновлении фильма, некорректый id= " + film.getId());
        }
        log.debug("Обновлен фильм: {}", film);
        films.put(film.getId(), film);
        return film;
    }


    @Override
    public Film filmById(Integer filmId) {
        Film film = films.get(filmId);
        if (film == null) {
            log.warn("Не найден фильм с ID=" + filmId);
            throw new FilmNotFoundException("Не найден фильм с ID=" + filmId);
        }
        return film;
    }
}
