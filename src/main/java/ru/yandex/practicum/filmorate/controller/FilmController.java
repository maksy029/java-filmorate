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
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
@Data
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {

        if (validator(film)) {
            film.setId(id++);
            log.info("Добавлен новый фильм: {}", film);
            films.put(film.getId(), film);
        } else {
            log.info("Ошибка валидации при добавление нового фильма: {}", film);
            throw new ValidationException("Ошибка валидации при добавление нового фильма");
        }

        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {

        if (validator(film) && films.containsKey(film.getId())) {
            log.info("Обновлен фильм: {}", film);
            films.put(film.getId(), film);
        } else {
            log.info("Ошибка валидации при обновлении фильма: {}", film);
            throw new ValidationException("Ошибка валидации при обновление нового фильма");
        }

        return film;
    }

    private boolean validator(Film film) {
        return !film.getName().isBlank()
                && film.getDescription().length() <= 200
                && film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))
                && film.getDuration() > 0;
    }
}
