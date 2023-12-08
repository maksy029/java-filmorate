# java-filmorate
## Sprint 11: add-database


### Структура базы данных:
![image](https://github.com/maksy029/java-filmorate/blob/add-database/ERD_FILMORATE.drawio.png)


### Примеры SQL-запросов к базе данных:
- Запрос на получение фильмов с genre='Комедия':
```
SELECT f.NAME film_name, g.GENRE_NAME genre, r.RATING_NAME rating, r.RATING_DESC
FROM FILMS f
JOIN FILMS_GENRES fg ON f.FILM_ID =fg.FILM_ID
JOIN GENRES g ON g.GENRE_ID = fg.GENRE_ID
JOIN RATINGS r ON r.RATING_ID =f.RATING_ID
WHERE g.GENRE_NAME = 'Комедия'; 
```
- Запрос на получение top-5 films by likes:
```
SELECT f.NAME film_name, g.GENRE_NAME genre, COUNT(l.FILM_ID) likes
FROM FILMS f
JOIN LIKES l ON f.FILM_ID = l.FILM_ID
JOIN FILMS_GENRES fg ON fg.FILM_ID =f.FILM_ID
JOIN GENRES g ON g.GENRE_ID = fg.GENRE_ID
GROUP BY f.NAME, g.GENRE_NAME
ORDER BY likes DESC
LIMIT 5;
```

- Запрос на получение top-5 users с наибольшим кол-вом friends:
```
SELECT u.NAME, COUNT(f.FRIEND_ID)
FROM USERS u
JOIN FRIENDS f ON f.USER_ID = u.USER_ID
GROUP BY u.NAME
ORDER BY COUNT(f.FRIEND_ID) DESC
LIMIT 5; 
```

- Запрос на получение фильмов c rating 18+:
```
SELECT f.NAME, g.GENRE_NAME, r.RATING_NAME, r.RATING_DESC
FROM FILMS f
JOIN RATINGS r ON r.RATING_ID =f.RATING_ID
JOIN FILMS_GENRES fg ON fg.FILM_ID =f.FILM_ID
JOIN GENRES g ON g.GENRE_ID =fg.GENRE_ID
WHERE r.RATING_NAME = 'NC-17';
```
