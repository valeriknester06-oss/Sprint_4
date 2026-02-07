# Sprint 4 — UI autotests (Яндекс Самокат)

## Стек
- Java 11
- Maven
- Selenium WebDriver
- JUnit 4

## Запуск тестов

Firefox:
mvn clean test -Dbrowser=firefox

Chrome:
mvn clean test -Dbrowser=chrome

## Особенность
В Chrome тест оформления заказа падает из-за известного бага приложения.
В Firefox тест проходит успешно.
