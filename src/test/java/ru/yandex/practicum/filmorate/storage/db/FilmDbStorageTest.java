package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private FilmDbStorage filmDbStorage;
    private Film film;

    @BeforeEach
    void set() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        FilmGenresDbStorage filmGenresDbStorage = new FilmGenresDbStorage(jdbcTemplate, genreDbStorage);
        filmDbStorage = new FilmDbStorage(jdbcTemplate, filmGenresDbStorage);

        film = new Film(0, "Film1", "descFilm1"
                , LocalDate.of(2023, 1, 1), 100, new Mpa(1, "G"));
    }

    @Test
    @DirtiesContext
    void shouldReturnListSizeFilmWith1() {
        set();
        filmDbStorage.create(film);

        List<Film> savedFilms = new ArrayList<>(filmDbStorage.findAll());

        assertThat(savedFilms.size())
                .isEqualTo(1);
    }

    @Test
    @DirtiesContext
    void shouldReturnFilmWithId1() {
        set();
        filmDbStorage.create(film);

       Film savedFilm = filmDbStorage.filmById(1);

       assertThat(savedFilm)
               .isNotNull()
               .isEqualTo(film);
    }

    @Test
    @DirtiesContext
    void shouldCreateFilmWithId1() {
        set();
        Film savedFilm = filmDbStorage.create(film);

        assertThat(savedFilm.getId())
                .isEqualTo(1);
    }

    @Test
    @DirtiesContext
    void shouldReturnNameNoNameFilm() {
        set();
        filmDbStorage.create(film);
        film.setName("No name film");

        filmDbStorage.update(film);

        assertThat(filmDbStorage.filmById(1).getName())
                .isEqualTo("No name film");
    }
}