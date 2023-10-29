package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {

    /*
     ТЗ9, вторая итерация
     Что сделали?:
     - раскрыли wild import;
     - изменили уровень log с имеющегося info на debug;
     - добавили дополнительную информацию в тексте exception при некорректной валидации данных;
     - удалили версию lombok в pom файле, тк версия имеется в parent pom;
     */

    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }

}
