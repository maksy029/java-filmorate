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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film filmById(@PathVariable("id") Integer filmId) {
        return filmService.filmById(filmId);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validator(film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        validator(film);
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> topFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.topFilms(count);
    }

    private void validator(Film film) {
        if (film.getName().isBlank()) {
            log.warn("Ошибка при валидации фильма, не заполнено поле name= {}", film.getName());
            throw new ValidationException("Ошибка при валидации фильма, не заполнено поле name= " + film.getName());
        }
        if (film.getDescription().length() > 200) {
            log.warn("Ошибка при валидации фильма, длина поля description >200, а именно= {}",
                    film.getDescription().length());
            throw new ValidationException("Ошибка при валидации фильма, длина поля description >200, а именно= "
                    + film.getDescription().length());
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Ошибка при валидации фильма, дата релиза раньше 28.12.1895, а именно= {}",
                    film.getReleaseDate());
            throw new ValidationException("Ошибка при валидации фильма, дата релиза раньше 28.12.1895, а именно= "
                    + film.getReleaseDate());
        }
        if (film.getDuration() < 0) {
            log.warn("Ошибка при валидации фильма, отрицательная продолжительность= {}", film.getDuration());
            throw new ValidationException("Ошибка при валидации фильма, отрицательная продолжительность= "
                    + film.getDuration());
        }
    }
}