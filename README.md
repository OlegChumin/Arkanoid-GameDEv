# Arkanoid Game Dev

Учебный проект с простой реализацией игры Arkanoid на Java Swing.

Автор: Олег Чумин.

В репозитории оставлена одна актуальная версия игры: пакет `org.example.arkanoid`.
Старые экспериментальные реализации и черновики удалены, чтобы проект собирался и поддерживался как одно приложение.

## Что внутри

- `src/main/java/org/example/ArkanoidGameStart.java` - точка входа в приложение.
- `src/main/java/org/example/arkanoid/Arkanoid.java` - основная игровая панель и игровой цикл.
- `src/main/java/org/example/arkanoid/Ball.java` - шар и обработка столкновений.
- `src/main/java/org/example/arkanoid/Bar.java` - платформа игрока.
- `src/main/java/org/example/arkanoid/Bricks.java` - кирпичи уровня.
- `src/main/java/org/example/arkanoid/Levels.java` - создание и переключение уровней.
- `src/main/java/org/example/arkanoid/RewardsNew.java` - бонусы и временные эффекты.
- `src/main/java/org/example/arkanoid/ListenersHandler.java` - обработка клавиатуры и мыши.
- `src/main/java/org/example/arkanoid/Text.java` - текстовые элементы интерфейса.
- `src/test/java/org/example/arkanoid/ArkanoidGameTest.java` - тесты игровой логики.

## Требования

- JDK 17 или новее.
- Gradle Wrapper уже лежит в проекте, отдельно Gradle устанавливать не нужно.

## Запуск

На Windows:

```powershell
.\gradlew.bat run
```

Если задача `run` не настроена в IDE, можно запустить класс:

```text
org.example.ArkanoidGameStart
```

В IntelliJ IDEA достаточно открыть проект как Gradle-проект и запустить `ArkanoidGameStart`.

## Управление

- Клик мышью по игровому полю - старт игры.
- Повторный клик - пауза или продолжение.
- Стрелки влево/вправо - движение платформы.
- Движение мышью - платформа следует за курсором.

## Проверка проекта

Запустить тесты:

```powershell
.\gradlew.bat test
```

Собрать проект, прогнать тесты и проверить JavaDoc:

```powershell
.\gradlew.bat clean test javadoc
```

## Текущее состояние

Проект собирается как единая Swing-игра. В код добавлены JavaDoc-комментарии для основных классов и методов, а также тесты на ключевую игровую логику:

- создание уровня без дублей кирпичей;
- переход на следующий уровень;
- потеря жизни и сброс состояния раунда;
- запуск и остановка бонуса `UltraBall`.

## Известные ограничения

- Игра использует простой бесконечный цикл в Swing-приложении.
- Часть состояния пока хранится в статических полях, поэтому тесты перед запуском сбрасывают это состояние.
- Графика и звук минимальные: проект сфокусирован на учебной игровой логике, а не на готовом релизном интерфейсе.
