# Online-store App

Веб-приложение банка, разработанное в рамках проектной работы [Яндекс Практикум](https://practicum.yandex.ru/).

## 🌟 Основные возможности

- Регистрация пользователя
- Перевод денег с аккаунта на аккаунт
- Ввод/вывод денег

## 🛠 Технологический стек

**CI/CD:**
- Jenkins

**Containerization:**
- k8s
- helm

**Backend:**
- Spring WebFlux
- Spring Web
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
#### Для запуска понадобятся: docker, docker-compose, git

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/yungdanie/bankapp
   ```
2. Выполните в каталоге с проектом
   ```
   docker compose up -d --build
   ```
3. Замените файл `/jenkins/jenkins_kubeconfig.yaml`
   Jenkins будет использовать этот файл для доступа к Kubernetes. 
   Выполните в терминале:

   ```bash
   cp ~/.kube/config ./jenkins/jenkins_kubeconfig.yaml
   ```
   Затем отредактируйте файл:

   **Замените `server` на:**
   
   ```yaml
   server: https://host.docker.internal:6443
   ```
   
   **Добавьте:**
   
   ```yaml
   insecure-skip-tls-verify: true
   ```
4. Создайте\замените `./jenkins/.env` файл

Создайте файл `.env` :

```env
# Путь до локального kubeconfig-файла
KUBECONFIG_PATH=/Users/username/.kube/jenkins_kubeconfig.yaml

# Параметры для GHCR
GITHUB_USERNAME=your-username
GITHUB_TOKEN=ghp_...
GHCR_TOKEN=ghp_...

# Docker registry (в данном случае GHCR)
DOCKER_REGISTRY=ghcr.io/your-username
GITHUB_REPOSITORY=your-username/YandexHelmApp

```

> Убедитесь, что ваш GitHub Token имеет права `write:packages`, `read:packages` и `repo`.


### 6. Запустите Jenkins

   ```bash
   cd jenkins
   docker compose up -d --build
   ```
   Jenkins будет доступен по адресу: [http://localhost:8080](http://localhost:8080)

---


## Как пользоваться Jenkins

1. Откройте Jenkins: [http://localhost:8080](http://localhost:8080)
2. Перейдите в задачу `YandexHelmApp` → `Build Now`
3. Jenkins выполнит:
   - сборку и тесты
   - сборку Docker-образов
   - деплой в Kubernetes в namespace

---

## Проверка успешного деплоя

### 1. Добавьте записи в `/etc/hosts`

```bash
sudo nano /etc/hosts
```

Добавьте:

```text
127.0.0.1 customer.test.local
127.0.0.1 order.test.local
127.0.0.1 customer.prod.local
127.0.0.1 order.prod.local
```

### 2. Отправьте запросы на `/actuator/health` на host сервиса (см. общий values.yaml)