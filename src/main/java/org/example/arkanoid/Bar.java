package org.example.arkanoid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Платформа игрока.
 *
 * <p>Платформа управляется клавиатурой и мышью, отражает шар и хранит счетчик
 * жизней игрока. Для более гибких отскоков платформа разделена на центральную,
 * левую и правую зоны столкновения.</p>
 */
public class Bar {
    /**
     * Отступ платформы от нижней границы игрового окна.
     */
    public static final int Y = 40;

    /**
     * Ширина центральной части платформы.
     */
    public int barWidth = 30;

    /**
     * Высота платформы.
     */
    public int barHeight = 10;

    /**
     * Ширина боковой части платформы.
     */
    public int barSideWidth = 20;

    /**
     * Цвет центральной части платформы.
     */
    public Color barMainColor = Color.WHITE;

    /**
     * Цвет боковых частей платформы.
     */
    public Color barSideColor = Color.GRAY;

    /**
     * Количество оставшихся жизней игрока.
     */
    public int lives = 3; // количество "жизней"

    /**
     * Начальная координата X платформы.
     */
    public static final int DEFAULT_X = 197;

    /**
     * Текущая координата X центральной части платформы.
     */
    public int x = DEFAULT_X; // координата платформы

    /**
     * Скорость смещения платформы при нажатии стрелок.
     */
    public int moveSpeed = 10;

    /**
     * Игровая панель, в которой находится платформа.
     */
    private Arkanoid game;

    /**
     * Создает платформу для указанной игровой панели.
     *
     * @param game игровая панель, которой принадлежит платформа
     */
    public Bar(Arkanoid game) {
        this.game = game;
    }

    /**
     * Перемещает платформу влево или вправо внутри игрового поля.
     *
     * @param direction направление движения: {@link ListenersHandler#LEFT}
     *                  или {@link ListenersHandler#RIGHT}
     */
    void move(int direction) { // движение платформы
        // кнопки влево и вправо
        int widthMargin = 5;
        if (direction == ListenersHandler.LEFT) {
            if (x > widthMargin) {
                x -= moveSpeed;
            }
        } else if (direction == ListenersHandler.RIGHT) {
            if (x < game.getWidth() - (barWidth + widthMargin)) {
                x += moveSpeed;
            }
        }
    }

    /**
     * Обрабатывает потерю одной жизни.
     *
     * <p>Метод уменьшает счетчик жизней, останавливает шар, возвращает шар и
     * платформу в начальные позиции, сбрасывает скорость игры и выключает
     * активные бонусы.</p>
     *
     * @param game игра, в которой игрок потерял жизнь
     */
    public static void looseLive(Arkanoid game) { // потеря жизней
        game.bar.lives--;
        game.waitingToStart = true;
        game.ball.xa = 0;
        game.ball.ya = 0;
        game.ball.x = Ball.DEFAULT_X;
        game.ball.y = Ball.DEFAULT_Y;
        game.bar.x = Bar.DEFAULT_X;
        game.text.livesLabel.setText("" + game.bar.lives);
        game.text.startLabel.setText("Lives: " + game.bar.lives);
        game.speed = Arkanoid.DEFAULT_SPEED;
        game.rewards.stopAllRewards();
    }

    /**
     * Отрисовывает платформу.
     *
     * @param g2 графический контекст, в который рисуется платформа
     */
    public void paintBar(Graphics2D g2) { // отрисовываем платформу (биту)
        g2.setColor(barSideColor); // рисуем левую часть платформы, цвет серый
        g2.fillRoundRect(x - (barSideWidth - 3), game.getHeight() - Y, barSideWidth, barHeight, 10, 10);

        g2.setColor(barSideColor); //рисуем правую часть платформы, цвет серый
        g2.fillRoundRect(x + (barWidth - 3), game.getHeight() - Y, barSideWidth, barHeight, 10, 10); //рисуем правую часть платформы, цвет серый

        g2.setColor(barMainColor); // рисуем центральную часть платформы, цвет белый
        g2.fillRect(x, game.getHeight() - Y, barWidth, barHeight);
    }

    /**
     * Возвращает верхнюю координату платформы.
     *
     * <p>Значение используется шаром, чтобы после столкновения поставить себя
     * над платформой и не застревать внутри нее.</p>
     *
     * @return координата верхней границы платформы
     */
    public int getTopY() {
        return game.getHeight() - Y - barHeight;
    }

    /**
     * Возвращает границы центральной зоны столкновения платформы.
     *
     * @return прямоугольник центральной части платформы
     */
    public Rectangle getBounds() {
        return new Rectangle(x, game.getHeight() - Y, barWidth, barHeight);
    }

    /**
     * Возвращает границы левой зоны столкновения платформы.
     *
     * @return прямоугольник левой части платформы
     */
    public Rectangle getBoundsLeft() {
        return new Rectangle(x - 20, game.getHeight() - Y, barWidth - 3, barHeight);
    }

    /**
     * Возвращает границы правой зоны столкновения платформы.
     *
     * @return прямоугольник правой части платформы
     */
    public Rectangle getBoundsRight() {
        return new Rectangle(x + 20, game.getHeight() - Y, barWidth - 3, barHeight);
    }
}
