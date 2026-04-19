package org.example.arkanoid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Хранилище и отрисовщик кирпичей текущего уровня.
 *
 * <p>Класс хранит два представления кирпичей: плоский список для игровой
 * логики столкновений и список строк для сохранения структуры уровня.</p>
 */
public class Bricks {
    /**
     * Кирпичи, сгруппированные по строкам уровня.
     */
    public ArrayList<ArrayList<Brick>> brickRows = new ArrayList<>(); // создаем все ряды кирпичей

    /**
     * Плоский список всех активных кирпичей.
     *
     * <p>Именно этот список используется при столкновениях шара и при
     * отрисовке, потому что из него удаляются разрушенные кирпичи.</p>
     */
    public ArrayList<Brick> bricks = new ArrayList<>(); // создаем ряд кирпичей

    /**
     * Игровая панель, которой принадлежит коллекция кирпичей.
     */
    private Arkanoid game;

    /**
     * Создает коллекцию кирпичей для указанной игры.
     *
     * @param game игровая панель, где будут отрисованы кирпичи
     */
    public Bricks(Arkanoid game) {
        this.game = game;
    }

    /**
     * Один разрушаемый кирпич.
     *
     * <p>Кирпич знает свое положение, цвет, количество оставшихся ударов и
     * тип бонуса, который может выпасть после удара.</p>
     */
    public static class Brick {
        /**
         * Ширина кирпича в пикселях.
         */
        public static final int WIDTH = 15; // ширина блока brick

        /**
         * Высота кирпича в пикселях.
         */
        public static final int HEIGHT = 10; // высота блока brick

        /**
         * Координата X левого верхнего угла кирпича.
         */
        int x = 15;

        /**
         * Координата Y левого верхнего угла кирпича.
         */
        int y = 50;

        /**
         * Текущий цвет кирпича.
         */
        Color color = Color.BLACK;

        /**
         * Количество оставшихся ударов до разрушения кирпича.
         */
        int hits = 1;

        /**
         * Тип бонуса, спрятанного в кирпиче.
         *
         * <p>Пустая строка означает, что бонуса нет.</p>
         */
        String rewardType = "";

        /**
         * Номер удара, на котором бонус должен выпасть из кирпича.
         */
        int rewardNum = ThreadLocalRandom.current().nextInt(1, hits + 1); // случайный блок куда будет записан приз reward

        /**
         * Создает кирпич с начальными значениями координат, цвета и прочности.
         */
        public Brick() {
        }

        /**
         * Проверяет, должен ли кирпич выпустить бонус на текущем ударе.
         *
         * @return {@code true}, если у кирпича есть тип бонуса и текущий удар
         *         совпал с номером выпадения
         */
        public boolean hasRewards() {
            return !"".equals(rewardType) && rewardNum == hits;
        }

        /**
         * Возвращает прямоугольник столкновения кирпича.
         *
         * @return текущие границы кирпича
         */
        public Rectangle getBounds() {
            return new Rectangle(x, y, WIDTH, HEIGHT); // получение границ блока
        }
    }

    /**
     * Применяет один удар к кирпичу и обновляет его цвет.
     *
     * <p>Цвет показывает игроку оставшуюся прочность кирпича. После изменения
     * цвета счетчик ударов уменьшается на единицу.</p>
     *
     * @param brick индекс кирпича в списке {@link #bricks}
     */
    void updateHits(int brick) {
        int hits = bricks.get(brick).hits;
        switch (hits) {
            case 1:
                bricks.get(brick).color = Color.GREEN;
                break;
            case 2:
                bricks.get(brick).color = Color.YELLOW;
                break;
            case 3:
                bricks.get(brick).color = Color.LIGHT_GRAY;
                break;
            case 4:
                bricks.get(brick).color = Color.GRAY;
                break;
            case 5:
                bricks.get(brick).color = Color.DARK_GRAY;
                break;
        }
        bricks.get(brick).hits -= 1;
    }

    /**
     * Отрисовывает все активные кирпичи.
     *
     * @param graphics графический контекст, в который рисуются кирпичи
     */
    public void paintBrick(Graphics2D graphics) {
        for (Brick brick : game.brick.bricks) {
            graphics.setColor(brick.color);
            graphics.fillRect(brick.x, brick.y, Brick.WIDTH, Brick.HEIGHT);
        }
    }
}
