package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {

    /*
     ТЗ10.1
     Что сделали?:
     - добавили пропущенные log;
     - попраили модификаторы доступа.
     */

    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }

}
