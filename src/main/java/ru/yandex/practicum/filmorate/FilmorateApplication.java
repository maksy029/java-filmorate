package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {

    /*
     ТЗ10
     Что сделали?:
     - изменили архитектуру:
     *Хранение данных о фильмах и пользователях вынесли в отдельные классы пакета storage;
     *Логику приложения вынесли в классы пакета service.
     - добавили новую функциональность:
     * Обеспечили возможность пользователям добавлять друг друга в друзья и ставить фильмам лайки.
     - довели API до соответствия REST.
     */

    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }

}
