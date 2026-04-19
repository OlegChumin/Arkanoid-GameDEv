package org.example.arkanoid;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 * Генератор уровней Arkanoid.
 *
 * <p>Класс создает раскладку кирпичей для текущего номера уровня и отвечает
 * за переход к следующему уровню после уничтожения всех кирпичей.</p>
 */
public class Levels {
    /**
     * Номер текущего уровня.
     */
    public static int currentLevel = 1;

    /**
     * Последний индекс строки, используемый при создании уровня.
     *
     * <p>Значение {@code 4} означает пять строк: от {@code 0} до {@code 4}.</p>
     */
    public int rowNum = 4; // 3 или четыре ряда надо проверить

    /**
     * Вспомогательный счетчик для смещения кирпичей на некоторых уровнях.
     */
    public int levelCounter = 0;

    /**
     * Игровая панель, для которой создаются уровни.
     */
    private Arkanoid game;

    /**
     * Генератор случайных смещений для уровней с нерегулярной раскладкой.
     */
    private final Random random = new Random();

    /**
     * Создает контроллер уровней и сразу строит текущий уровень.
     *
     * @param game игровая панель, где размещаются кирпичи
     */
    public Levels(Arkanoid game) {
        this.game = game;
        createLevel();
    }

    /**
     * Переводит игру на следующий уровень.
     *
     * <p>Метод увеличивает номер уровня, останавливает бонусы, сбрасывает шар
     * и платформу в начальные позиции, создает новую раскладку кирпичей и
     * переводит игру в состояние ожидания клика для запуска.</p>
     *
     * @param game игра, которую нужно перевести на следующий уровень
     */
    public static void startNewLevel(Arkanoid game) {
        Levels.currentLevel++;
        game.waitingToStart = true;
        game.rewards.stopAllRewards();
        game.ball.xa = 0;
        game.ball.ya = 0;
        game.ball.x = Ball.DEFAULT_X;
        game.ball.y = Ball.DEFAULT_Y;
        game.bar.x = Bar.DEFAULT_X;
        game.levels.createLevel();
        game.text.startLabel.setText("Уровень " + currentLevel + ", Нажми для запуска");
        game.speed = Arkanoid.DEFAULT_SPEED;
    }

    /**
     * Пересоздает коллекции кирпичей для текущего уровня.
     *
     * <p>Перед построением метод очищает старые строки и плоский список
     * кирпичей. Это важно, чтобы при перезапуске или переходе уровни не
     * накапливали дубли.</p>
     */
    void createLevel() { // метод создает уровень
        game.brick.bricks.clear();
        game.brick.brickRows.clear();
        for (int i = 0; i <= rowNum; i++) {
            ArrayList<Bricks.Brick> row = drawLevel(i);
            game.brick.brickRows.add(row);
            game.brick.bricks.addAll(row);
        }
//        game.rewards.createAllRewards();
    }

    /**
     * Создает одну строку кирпичей для текущего уровня.
     *
     * <p>Форма строки зависит от номера уровня. На первых уровнях раскладки
     * более регулярные, на поздних появляются случайные смещения координат.</p>
     *
     * @param i номер создаваемой строки
     * @return список кирпичей этой строки
     */
    ArrayList<Bricks.Brick> drawLevel(int i) {
        ArrayList<Bricks.Brick> row = new ArrayList<>();
        int level = Levels.currentLevel;
        if (level == 1) {
            for (int j = 0; j <= 17; j++) {
                Bricks.Brick brick = new Bricks.Brick();
                brick.x += j * 20;
                brick.y = i * 20 + 80;
                switch (i) {
                    case 0:
                        brick.color = Color.LIGHT_GRAY;
                        brick.hits = 2;
                        break;
                    case 2:
                        brick.color = Color.YELLOW;
                        brick.hits = 1;
                        break;
                    case 4:
                        brick.color = Color.GREEN;
                        brick.hits = 0;
                        break;
                }
                if (j % 2 != 0 && i % 2 == 0) {
                    row.add(brick);
                }
            }
        } else if (level == 2) {
            for (int j = 0; j <= 17; j++) {
                Bricks.Brick brick = new Bricks.Brick();
                if (levelCounter == 0) {
                    brick.x += 2 * 21;
                    levelCounter = 1;
                } else {
                    brick.x += j * 20;
                }
                brick.y = i * 25 + 50;
                switch (i) {
                    case 0:
                        brick.color = Color.GRAY;
                        brick.hits = 3;
                        break;
                    case 1:
                        brick.color = Color.LIGHT_GRAY;
                        brick.hits = 2;
                        break;
                    case 2:
                        brick.color = Color.YELLOW;
                        brick.hits = 1;
                        break;
                    case 3:
                        brick.color = Color.GREEN;
                        brick.hits = 0;
                        break;
                    case 4:
                        brick.color = Color.GREEN;
                        brick.hits = 0;
                        break;
                }
                if ((j / 1) % 2 != i % 2) {
                    row.add(brick);
                }
            }
            levelCounter = 0;
        } else if (level == 3) {
            for (int j = 0; j <= 14; j++) {
                Bricks.Brick brick = new Bricks.Brick();
                brick.x += (j * 25) + 10;
                brick.y = i * 30 + 50;
                switch (i) {
                    case 0:
                        brick.color = Color.DARK_GRAY;
                        brick.hits = 4;
                        break;
                    case 1:
                        brick.color = Color.GRAY;
                        brick.hits = 3;
                        break;
                    case 2:
                        brick.color = Color.LIGHT_GRAY;
                        brick.hits = 2;
                        break;
                    case 3:
                        brick.color = Color.YELLOW;
                        brick.hits = 1;
                        break;
                    case 4:
                        brick.color = Color.GREEN;
                        brick.hits = 0;
                        break;
                }
                if ((j) % 3 != i % 3) {
                    row.add(brick);
                }
            }
        } else if (level == 4) {
            for (int j = 0; j <= 17; j++) {
                Bricks.Brick brick = new Bricks.Brick();
                brick.x += (j * 20) + 10;
                brick.y = i * 30 + 50;
                switch (i) {
                    case 0:
                        brick.color = Color.DARK_GRAY;
                        brick.hits = 4;
                        break;
                    case 1:
                        brick.color = Color.GRAY;
                        brick.hits = 3;
                        break;
                    case 2:
                        brick.color = Color.LIGHT_GRAY;
                        brick.hits = 2;
                        break;
                    case 3:
                        brick.color = Color.YELLOW;
                        brick.hits = 1;
                        break;
                    case 4:
                        brick.color = Color.GREEN;
                        brick.hits = 0;
                        break;
                }
                row.add(brick);
            }
        } else if (level == 5) {
            for (int j = 0; j <= 10; j++) {
                Bricks.Brick brick = new Bricks.Brick();
                brick.x += (j * 28) + random.nextInt(1, 41) + 10;
                brick.y = (i * 15 + 50) + random.nextInt(1, 11);
                switch (i) {
                    case 0:
                        brick.color = Color.DARK_GRAY;
                        brick.hits = 4;
                        break;
                    case 1:
                        brick.color = Color.GRAY;
                        brick.hits = 3;
                        break;
                    case 2:
                        brick.color = Color.LIGHT_GRAY;
                        brick.hits = 2;
                        break;
                    case 3:
                        brick.color = Color.YELLOW;
                        brick.hits = 1;
                        break;
                    case 4:
                        brick.color = Color.GREEN;
                        brick.hits = 0;
                        break;
                }
                row.add(brick);
            }
        } else {
            for (int j = 0; j <= 7; j++) {
                Bricks.Brick brick = new Bricks.Brick();
                brick.x += (j * 40) + random.nextInt(1, 41) + 10;
                brick.y = (i * 40 + 50) + random.nextInt(1, 71);
                switch (i) {
                    case 0:
                        brick.color = Color.DARK_GRAY;
                        brick.hits = 4;
                        break;
                    case 1:
                        brick.color = Color.GRAY;
                        brick.hits = 3;
                        break;
                    case 2:
                        brick.color = Color.LIGHT_GRAY;
                        brick.hits = 2;
                        break;
                    case 3:
                        brick.color = Color.YELLOW;
                        brick.hits = 1;
                        break;
                    case 4:
                        brick.color = Color.GREEN;
                        brick.hits = 0;
                        break;
                }
                row.add(brick);
            }
        }
        return row;
    }
}
