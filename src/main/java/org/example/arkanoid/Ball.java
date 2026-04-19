package org.example.arkanoid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Игровой шар.
 *
 * <p>Класс отвечает за движение шара, столкновения со стенами, платформой и
 * кирпичами, а также за визуальный режим {@code UltraBall}. Шар связан с
 * {@link Arkanoid}, потому что текущая модель хранит платформу, кирпичи и
 * бонусы прямо на игровой панели.</p>
 */
public class Ball {
    /**
     * Начальная координата X шара при старте или потере жизни.
     */
    public static final int DEFAULT_X = 205;

    /**
     * Начальная координата Y шара при старте или потере жизни.
     */
    public static final int DEFAULT_Y = 350;

    /**
     * Начальный диаметр шара в пикселях.
     */
    public static final int DEFAULT_DIAMETER = 10;

    /**
     * Текущая координата X левого верхнего угла шара.
     */
    int x = DEFAULT_X;

    /**
     * Текущая координата Y левого верхнего угла шара.
     */
    int y = DEFAULT_Y;

    /**
     * Текущий диаметр шара в пикселях.
     */
    int diameter = DEFAULT_DIAMETER;

    /**
     * Горизонтальная скорость шара за один кадр.
     */
    int xa = 0;

    /**
     * Вертикальная скорость шара за один кадр.
     */
    int ya = 0;

    /**
     * Индекс кирпича, с которым шар столкнулся на текущем шаге.
     */
    int brick;

    /**
     * Верхняя граница шара, сохраненная для расчета стороны столкновения.
     */
    int ballTopPosition = 0;

    /**
     * Нижняя граница шара, сохраненная для расчета стороны столкновения.
     */
    int ballBottomPosition = 0;

    /**
     * Правая граница шара, сохраненная для расчета стороны столкновения.
     */
    int ballRightPosition = 0;

    /**
     * Левая граница шара, сохраненная для расчета стороны столкновения.
     */
    int ballLeftPosition = 0;

    /**
     * Верхняя граница кирпича, с которым произошло столкновение.
     */
    int brickTopPosition = 0;

    /**
     * Нижняя граница кирпича, с которым произошло столкновение.
     */
    int brickBottomPosition = 0;

    /**
     * Левая граница кирпича, с которым произошло столкновение.
     */
    int brickLeftPosition = 0;

    /**
     * Правая граница кирпича, с которым произошло столкновение.
     */
    int brickRightPosition = 0;

    /**
     * Признак активного режима UltraBall.
     *
     * <p>В этом режиме шар уничтожает кирпичи без стандартного отскока.</p>
     */
    boolean ultraBallMode = false;

    /**
     * Индекс текущего цвета анимации UltraBall.
     */
    int ultraBallColor = 0;
    //    public static boolean bigBallMode = false;
    //    public static int bigBallModeColor = 0;
//    public static boolean fireBallMode = false;
//    public static int fireBallModeColor = 5; //дописать в код

    /**
     * Игровая панель, к которой принадлежит шар.
     */
    private Arkanoid game;

    /**
     * Создает шар для указанной игровой панели.
     *
     * @param game игровая панель, содержащая остальные объекты уровня
     */
    public Ball(Arkanoid game) {
        this.game = game;
    }

    /**
     * Двигает шар на один игровой шаг и обрабатывает столкновения.
     *
     * <p>Метод проверяет границы окна, верхнюю информационную область,
     * платформу игрока и все активные кирпичи. После обработки столкновения
     * координаты шара изменяются на значения {@link #xa} и {@link #ya}.</p>
     */
    void move() {
        // смена направления полета меча при столкновениях с границами игрового поля
        if (x + xa <= 0) {
            xa *= -1;
        } else if(x + xa >= game.getWidth() - diameter) {
            xa = -xa;
        } else if(y + ya <= Text.MENU_BAR_HEIGHT) {
            ya = 1;
        } else if(y + ya >= game.getHeight() - diameter) {
            if (game.bar.lives == 0) {
                game.gameOver();
            } else if (game.bar.lives > 0) {
                Bar.looseLive(game);
            }
        } else if (collision()) {//проверка столкновения с платформой Bar
            ya = -1;
            y = game.bar.getTopY() - diameter + 10;
        } else if (collisionWithBricks()) {  // проверка столкновения с блоками
            if (ultraBallMode) {
                game.brick.bricks.remove(brick);
            } else {
                ballTopPosition = y;
                ballBottomPosition = y + diameter;
                ballRightPosition = x + diameter + 14;
                ballLeftPosition = x;
                brickTopPosition = game.brick.bricks.get(brick).y + 1;
                brickBottomPosition = game.brick.bricks.get(brick).y + Bricks.Brick.HEIGHT - 1;
                brickRightPosition = game.brick.bricks.get(brick).x + 14;
                brickLeftPosition = game.brick.bricks.get(brick).x + Bricks.Brick.WIDTH;
                // изменения направления полета меча
                if ((ballBottomPosition == brickTopPosition || ballTopPosition == brickBottomPosition)
                        && (ballRightPosition != brickLeftPosition && ballLeftPosition != brickRightPosition)) {
                    if (ya == 1) {
                        ya = -1;
                    } else if (ya == -1) {
                        ya = 1;
                    }
                } else {
                    if (xa > 0) {
                        xa *= -1;
                    } else if (xa < 0) {
                        xa *= -1;
                    }
                    // удаление блоков если необходимо (после ударов)
                    if (game.brick.bricks.get(brick).hits == 0) {
                        game.brick.bricks.remove(brick);
                    } else {
                        game.brick.updateHits(brick);
                    }
                }
                // если все блоки были уничтожены (удалены)
                if (game.brick.bricks.size() == 0) {
                    Levels.startNewLevel(game);
                }
            }
        }
            // изменение направление движения меча
            x = x + xa;
            y = y + ya;
    }

    /**
     * Проверяет столкновение шара с платформой.
     *
     * <p>Центральная часть платформы отражает шар вверх. Боковые части могут
     * менять горизонтальное направление и немного корректировать скорость игры,
     * что делает отскок менее однообразным.</p>
     *
     * @return {@code true}, если шар пересекается с любой частью платформы
     */
    private boolean collision() { // надо проверить
        if (game.bar.getBounds().intersects(getBounds())) {
            return true;
        } else if (game.bar.getBoundsLeft().intersects(getBounds())) {
            if (xa > 0) {
                xa *= -1;
                if (x * xa < 0 && xa + 1 != 0) {
                    xa++;
                }
                if (game.speed < 10) {
                    game.speed += 1;
                }
            } else if (xa < 0) {
                if (x * xa < 0 && xa - 1 != 0) {
                    xa--;
                }
                if (game.speed > 2) {
                    game.speed -= 1;
                }
            }
            return true;
        } else if (game.bar.getBoundsRight().intersects(getBounds())) {
            if (xa < 0) {
                xa *= -1;
                if (x * xa > 0 && xa - 1 != 0) {
                    xa--;
                }
                if (game.speed < 10) {
                    game.speed += 1;
                }
            } else if (xa > 0) {
                if (x * xa > 0 && xa + 1 != 0) {
                    xa++;
                }
                if (game.speed > 2) {
                    game.speed -= 1;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Проверяет столкновение шара с кирпичами текущего уровня.
     *
     * <p>Если кирпич содержит бонус, метод создает падающий бонус перед тем,
     * как основная логика удара изменит состояние кирпича.</p>
     *
     * @return {@code true}, если найдено пересечение с кирпичом
     */
    private boolean collisionWithBricks() {
        for (int i = 0; i < game.brick.bricks.size(); i++) {
            if (game.brick.bricks.get(i).getBounds().intersects(getBounds())) {
                brick = i;
                // если блок имееет внутри приз (reward), то мы создаем приз (reward)
                if (game.brick.bricks.get(i).hasRewards()) {
                    game.rewards.createReward(game.brick.bricks.get(i).rewardType, game.brick.bricks.get(i).x - 3, game.brick.bricks.get(i).y);
                }
                return game.brick.bricks.get(i).getBounds().intersects(getBounds());
            }
        }
        return false;
    }

    /**
     * Отрисовывает шар.
     *
     * <p>В обычном режиме шар белый. В режиме {@code UltraBall} цвет меняется
     * между красным, желтым и зеленым, чтобы бонус был визуально заметен.</p>
     *
     * @param graphics графический контекст, в который рисуется шар
     */
    public void paintBall(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        if (ultraBallMode) {
            if (ultraBallColor == 0) {
                graphics.setColor(Color.RED);
                ultraBallColor++;
            } else if (ultraBallColor == 1) {
                graphics.setColor(Color.YELLOW);
                ultraBallColor++;
            } else if (ultraBallColor == 2) {
                graphics.setColor(Color.GREEN);
                ultraBallColor = 0;
            }
        }
        graphics.fillOval(x, y, diameter, diameter);
    }

    /**
     * Возвращает прямоугольник столкновения шара.
     *
     * @return текущие границы шара
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }
}
