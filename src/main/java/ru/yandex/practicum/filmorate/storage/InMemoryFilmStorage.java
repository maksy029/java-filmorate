package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
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
        if (validator(film)) {
            film.setId(id++);
            log.debug("Добавлен новый фильм: {}", film);
            films.put(film.getId(), film);
        } else {
            notValid(film);
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        if (validator(film) && films.containsKey(film.getId())) {
            log.debug("Обновлен фильм: {}", film);
            films.put(film.getId(), film);
        } else {
            if (!films.containsKey(film.getId())) {
                log.debug("Ошибка при обновлении фильма, некорректый id= {}", film.getId());
                throw new FilmNotFoundException("Ошибка при обновлении фильма, некорректый id= " + film.getId());
            }
            notValid(film);
        }
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

    private boolean validator(Film film) {
        return !film.getName().isBlank()
                && film.getDescription().length() <= 200
                && film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))
                && film.getDuration() > 0;
    }

    private void notValid(Film film) {
        if (film.getName().isBlank()) {
            log.debug("Ошибка при валидации фильма, не заполнено поле name= {}", film.getName());
            throw new ValidationException("Ошибка при валидации фильма, не заполнено поле name= " + film.getName());
        }
        if (film.getDescription().length() > 200) {
            log.debug("Ошибка при валидации фильма, длина поля description >200, а именно= {}",
                    film.getDescription().length());
            throw new ValidationException("Ошибка при валидации фильма, длина поля description >200, а именно= "
                    + film.getDescription().length());
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Ошибка при валидации фильма, дата релиза раньше 28.12.1895, а именно= {}",
                    film.getReleaseDate());
            throw new ValidationException("Ошибка при валидации фильма, дата релиза раньше 28.12.1895, а именно= "
                    + film.getReleaseDate());
        }
        if (film.getDuration() < 0) {
            log.debug("Ошибка при валидации фильма, отрицательная продолжительность= {}", film.getDuration());
            throw new ValidationException("Ошибка при валидации фильма, отрицательная продолжительность= "
                    + film.getDuration());
        }
    }
}
