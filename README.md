# Дипломная работа «Облачное хранилище»

## Описание проекта

Сервис предоставляет REST-интерфейс для загрузки файлов и вывода списка уже загруженных файлов пользователя.

Сервис работает в связке с [frontend](https://github.com/netology-code/jd-homeworks/tree/master/diploma/netology-diplom-frontend)
## Описание приложения

- Сервис реализовывает все методы, описанные в [yaml-файле](https://github.com/netology-code/jd-homeworks/blob/master/diploma/CloudServiceSpecification.yaml):
    1. Вывод списка файлов.
    2. Добавление файла.
    3. Удаление файла.
    4. Авторизация.
- Все настройки считываются с файла [настроек](./src/main/resources/application.properties).
- Информация о пользователях сервиса хранится в базе данных PostgreSQL.

## Требования к реализации

- Приложение разработано с использованием Spring Boot.
- Использован сборщик пакетов gradle/maven.
- Для запуска используется docker, docker-compose.
- Код размещён на Github.
- Код покрыт unit-тестами с использованием mockito.
- Добавлены интеграционные тесты с использованием testcontainers.

## Описание и запуск FRONT

1. Установите nodejs (версия не ниже 19.7.0) на компьютер, следуя [инструкции](https://nodejs.org/ru/download/current/).
2. Скачайте [FRONT](./netology-diplom-frontend) (JavaScript).
3. Перейдите в папку FRONT приложения и все команды для запуска выполняйте из неё.
4. Следуя описанию README.md FRONT проекта, запустите nodejs-приложение (`npm install`, `npm run serve`).
5. По умолчанию FRONT запускается на порту 8080 и доступен по url в браузере `http://localhost:8080`.
    1. Если порт 8080 занят, FRONT займёт следующий доступный (`8081`). После выполнения `npm run serve` в терминале вы увидите, на каком порту он запустился.