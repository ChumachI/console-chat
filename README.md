# console-chat
Проект представляет собой клиент-серверное приложение на основе Java—Sockets API с использованием Spring Framework.
Проект состоит из двух частей - серверная и клиентская и поддерживает следующий функционал
- Регистрация
- Логин
- Логаут
- Возможность создавать комнаты
- Возможность выбирать комнаты
- Мгновенный обмен сообщениями между участниками одной комнаты
- При повторном входе пользователя он увидит последние 30 сообщений из последней посещенной комнаты

## Язык проекта - Java
## Использованные технологии:
- Maven
- Spring Framework
- JDBC
- SQL
- Multithreading
### Требуемые доработки
Мне не нравится как выполнен класс ClientHandler, но мне пока не хватает опыта чтобы его исправить.
Так же я довольно кустарно осуществил обмен JSON. Но мне никто не говорил что так нельзя.