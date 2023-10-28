package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {

    /**
     * ТЗ9, первая интерация;
     * Создаем проект (используя сборщик Maven) для сервиса, который будет работать с фильмами и оценками пользователей.
     * В текущей итерации реализовали:
     * - restAPI: FilmController и UserController используя возможности Spring Boot;
     * - классы сущностей: User, Film используя возможности Lombok;
     * - добавили logs используя аннотацию @SLF4J от Lombok
     */

    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }

}
