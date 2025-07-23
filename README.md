# Online-store App

Веб-приложение банка, разработанное в рамках проектной работы [Яндекс Практикум](https://practicum.yandex.ru/).

## 🌟 Основные возможности

- Регистрация пользователя
- Перевод денег с аккаунта на аккаунт
- Ввод/вывод денег

## 🛠 Технологический стек

**Config & Service Discovery**
- Consul

**Backend:**
- Spring WebFlux
- Spring Web
- Spring Cloud (gateway, loadbalancer, etc..)
- Mapstruct
- Lombok

**Frontend:**
- Thymeleaf
- JavaScript

**Database**
- PostgreSQL

**Тесты**
- Junit5

**Инструменты:**
- Gradle
- Git

## Запуск проекта

### Требования
- Java 21
- Gradle 8.13+
- Docker (docker compose tool)

### Установка
1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/yungdanie/bankapp
   ```
2. Выполните в каталоге с проектом
   ```
   docker compose up -d --build
   ```
3. Готово, UI запущен на порту 8088, сторонние сервисы заняли другие порты описанные в docker-compose.yml.