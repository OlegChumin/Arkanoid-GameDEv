package org.example.arkanoid;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Менеджер бонусов.
 *
 * <p>Класс отвечает за размещение бонусов в кирпичах, создание падающих
 * объектов бонусов, запуск эффектов после столкновения с платформой и остановку
 * временных эффектов. Текущая реализация хранит активные бонусы в статическом
     * списке конкретного экземпляра игры.</p>
 */
public class RewardsNew {
    /**
     * Оставшееся время действия бонуса {@code UltraBall} в условных игровых тиках.
     */
    public int ultraBallTime = 10;

    /**
     * Количество бонусов {@code UltraBall}, добавляемых при случайной раскладке.
     */
    public int defaultRewardUltraBallNum = 3;

    /**
     * Количество бонусов увеличения шара, добавляемых при случайной раскладке.
     */
    public int defaultRewardBigBallNum = 3;

    /**
     * Количество бонусов уменьшения шара, добавляемых при случайной раскладке.
     */
    public int defaultRewardSmallBallNum = 2;

    /**
     * Количество бонусов увеличения платформы, добавляемых при случайной раскладке.
     */
    public int defaultRewardBigBarNum = 3;

    /**
     * Количество бонусов уменьшения платформы, добавляемых при случайной раскладке.
     */
    public int defaultRewardSmallBarNum = 2;

    /**
     * Количество бонусов дополнительной жизни, добавляемых при случайной раскладке.
     */
    public int defaultRewardExtraLiveNum = 1;

    /**
     * Оставшееся количество бонусов {@code UltraBall}, которое нужно разместить.
     */
    int rewardUltraBallNum = defaultRewardUltraBallNum; // приз Ультра шар

    /**
     * Оставшееся количество бонусов увеличения шара, которое нужно разместить.
     */
    int rewardBigBallNum = defaultRewardBigBallNum; // приз большой шар

    /**
     * Оставшееся количество бонусов уменьшения шара, которое нужно разместить.
     */
    int rewardSmallBallNum = defaultRewardSmallBallNum; // приз малый шар

    /**
     * Оставшееся количество бонусов увеличения платформы, которое нужно разместить.
     */
    int rewardBigBarNum = defaultRewardBigBarNum; // приз малая платформа

    /**
     * Оставшееся количество бонусов уменьшения платформы, которое нужно разместить.
     */
    int rewardSmallBarNum = defaultRewardSmallBarNum; // приз большая платформа

    /**
     * Оставшееся количество бонусов дополнительной жизни, которое нужно разместить.
     */
    int rewardExtraLiveNum = defaultRewardExtraLiveNum; // приз дополнительная жизнь

    /**
     * Генератор случайных индексов кирпичей для размещения бонусов.
     */
    private final Random random = new Random();

    /**
     * Игровая панель, к которой относится менеджер бонусов.
     */
   private Arkanoid game;

    /**
     * Падающий объект бонуса.
     *
     * <p>Экземпляр создается после удара по кирпичу с бонусом. Пока
     * {@link #rewardBrickOn} равно {@code true}, бонус падает вниз и рисуется
     * как объект на поле. После столкновения с платформой включается эффект.</p>
     */
    public class Reward {
        /**
         * Текущая координата X бонуса.
         */
        int x;

        /**
         * Текущая координата Y бонуса.
         */
        int y;

        /**
         * Ширина визуального объекта бонуса.
         */
        int width;

        /**
         * Высота визуального объекта бонуса.
         */
        int height;

        /**
         * Тип бонуса.
         *
         * <p>Поддерживаемые значения: {@code UltraBall}, {@code BigBall},
         * {@code SmallBall}, {@code BigBar}, {@code SmallBar}, {@code ExtraLive}.</p>
         */
        String type; // поле отвечающее за тип приза (достижения) в игре

        /**
         * Признак того, что бонус еще падает как объект на поле.
         */
        boolean rewardBrickOn = false;

        /**
         * Признак того, что эффект бонуса уже активен.
         */
        boolean rewardOn = false;

        /**
         * Создает пустой объект бонуса.
         */
        public Reward() {
        }
    }

    /**
     * Последний созданный бонус.
     *
     * <p>Поле сохранено для совместимости с текущей структурой кода, где
     * создаваемый бонус сначала назначается сюда, а затем добавляется в общий
     * список.</p>
     */
    Reward currentReward = new Reward();

    /**
     * Общий список текущих падающих и активных бонусов.
     */
    public final List<Reward> currentRewards = new ArrayList<>();

    /**
     * Создает менеджер бонусов для указанной игровой панели.
     *
     * @param game игровая панель, где существуют кирпичи, платформа и шар
     */
    public RewardsNew(Arkanoid game) {
        this.game = game;
    }

    /**
     * Раскладывает заданное количество бонусов по случайным кирпичам.
     *
     * <p>Метод проходит по каждому типу бонуса и выбирает кирпичи без уже
     * назначенного бонуса. После размещения счетчики возвращаются к дефолтным
     * значениям, чтобы следующий уровень мог использовать те же настройки.</p>
     */
    public void createAllRewards() {
        if (game.brick.bricks.isEmpty()) {
            return;
        }
        // создаем все достижения (награды, призы) на стартовом уровне. Устанавливаем произвольную (random)
        // позицию по rewards в каждом уровне
        while (rewardUltraBallNum > 0) {
            int randomBrickIndex = random.nextInt(game.brick.bricks.size());
            if ("".equals(game.brick.bricks.get(randomBrickIndex).rewardType)) {
                game.brick.bricks.get(randomBrickIndex).rewardType = "UltraBall";
                rewardUltraBallNum--;
            }
        }
        while (rewardBigBallNum > 0) {
            int randomBrickIndex = random.nextInt(game.brick.bricks.size());
            if ("".equals(game.brick.bricks.get(randomBrickIndex).rewardType)) {
                game.brick.bricks.get(randomBrickIndex).rewardType = "BigBall";
                rewardBigBallNum--;
            }
        }
        while (rewardSmallBallNum > 0) {
            int randomBrickIndex = random.nextInt(game.brick.bricks.size());
            if ("".equals(game.brick.bricks.get(randomBrickIndex).rewardType)) {
                game.brick.bricks.get(randomBrickIndex).rewardType = "SmallBall";
                rewardSmallBallNum--;
            }
        }
        while (rewardBigBarNum > 0) {
            int randomBrickIndex = random.nextInt(game.brick.bricks.size());
            if ("".equals(game.brick.bricks.get(randomBrickIndex).rewardType)) {
                game.brick.bricks.get(randomBrickIndex).rewardType = "BigBar";
                rewardBigBarNum--;
            }
        }
        while (rewardSmallBarNum > 0) {
            int randomBrickIndex = random.nextInt(game.brick.bricks.size());
            if ("".equals(game.brick.bricks.get(randomBrickIndex).rewardType)) {
                game.brick.bricks.get(randomBrickIndex).rewardType = "SmallBar";
                rewardSmallBarNum--;
            }
        }
        while (rewardExtraLiveNum > 0) {
            int randomBrickIndex = random.nextInt(game.brick.bricks.size());
            if ("".equals(game.brick.bricks.get(randomBrickIndex).rewardType)) {
                game.brick.bricks.get(randomBrickIndex).rewardType = "ExtraLive";
                rewardExtraLiveNum--;
            }
        }
        rewardUltraBallNum = defaultRewardUltraBallNum; // почему здесь я опять присвоил defaul значения
        rewardBigBallNum = defaultRewardBigBallNum;
        rewardSmallBallNum = defaultRewardSmallBallNum;
        rewardBigBarNum = defaultRewardBigBarNum;
        rewardSmallBarNum = defaultRewardSmallBarNum;
        rewardExtraLiveNum = defaultRewardExtraLiveNum;
    }

    /**
     * Создает видимый падающий бонус после удара по кирпичу.
     *
     * <p>Размер объекта зависит от типа бонуса. Созданный бонус добавляется
     * в {@link #currentRewards} и начинает падать при следующей отрисовке.</p>
     *
     * @param type тип бонуса
     * @param x начальная координата X
     * @param y начальная координата Y
     */
    public void createReward(String type, int x, int y) {
        Reward reward = new Reward();
        reward.type = type;
        reward.x = x;
        reward.y = y;
        currentReward = reward;
        currentReward.rewardBrickOn = true;
        if ("UltraBall".equals(type)) {
            reward.width = Bricks.Brick.WIDTH + 6;
            reward.height = Bricks.Brick.HEIGHT;
        } else if ("BigBall".equals(type)) {
            reward.width = Bricks.Brick.WIDTH + 5;
            reward.height = Bricks.Brick.WIDTH + 5;
        } else if ("SmallBall".equals(type)) {
            reward.width = Bricks.Brick.WIDTH - 8;
            reward.height = Bricks.Brick.WIDTH - 8;
        } else if ("BigBar".equals(type) || "SmallBar".equals(type)) {
            reward.width = Bricks.Brick.WIDTH + 5;
            reward.height = Bricks.Brick.HEIGHT - 2;
        } else if ("ExtraLive".equals(type)) {
            reward.width = Bricks.Brick.WIDTH + 6;
            reward.height = Bricks.Brick.HEIGHT;
        }
        currentRewards.add(currentReward);
    }

    /**
     * Запускает эффект бонуса после столкновения с платформой.
     *
     * <p>Мгновенные бонусы удаляются из списка сразу после применения. Бонус
     * {@code UltraBall} остается в списке как активный временный эффект, чтобы
     * его таймер мог обновляться в {@link #paintReward()}.</p>
     *
     * @param type тип бонуса, который нужно применить
     */
    public void startReward(String type) {
        game.timeCounter = 0;
        for (int i = currentRewards.size() - 1; i >= 0; i--) {
            Reward reward = currentRewards.get(i);
            if ("UltraBall".equals(reward.type) && type.equals(reward.type)) {
                reward.rewardBrickOn = false;
                reward.rewardOn = true;
                game.ball.ultraBallMode = true;
                ultraBallTime = 10;
            } else if ("BigBall".equals(reward.type) && type.equals(reward.type)) {
                currentRewards.remove(i);
                if (game.ball.diameter == 5) {
                    game.ball.diameter += 5;
                } else {
                    game.ball.diameter += 10;
                }
            } else if ("SmallBall".equals(reward.type) && type.equals(reward.type)) {
                currentRewards.remove(i);
                if (game.ball.diameter <= 10 && game.ball.diameter > 5) {
                    game.ball.diameter -= 5;
                } else if (game.ball.diameter > 10) {
                    game.ball.diameter -= 10;
                }
            } else if ("BigBar".equals(reward.type) && type.equals(reward.type)) {
                currentRewards.remove(i);
                if (game.bar.barWidth == 15) {
                    game.bar.barWidth += 15;
                    game.bar.barSideWidth += 5;
                    game.bar.barMainColor = Color.WHITE;
                    game.bar.barSideColor = Color.GRAY;
                } else {
                    game.bar.barWidth += 15;
                    game.bar.barMainColor = Color.CYAN;
                    game.bar.barSideColor = Color.BLUE;
                }
            } else if ("SmallBar".equals(reward.type) && type.equals(reward.type)) {
                currentRewards.remove(i);
                if (game.bar.barWidth == 30) {
                    game.bar.barWidth -= 15;
                    game.bar.barSideWidth -= 5;
                    game.bar.barMainColor = Color.WHITE;
                    game.bar.barSideColor = Color.BLUE;
                } else if (game.bar.barWidth == 45) {
                    game.bar.barWidth -= 15;
                    game.bar.barMainColor = Color.WHITE;
                    game.bar.barSideColor = Color.GRAY;
                }
            } else if ("ExtraLive".equals(reward.type) && type.equals(reward.type)) {
                currentRewards.remove(i);
                game.bar.lives++; // добавляет 1 жизнь или + 1 bar
            }
        }
    }

    /**
     * Останавливает эффект бонуса и возвращает связанные параметры к дефолтным значениям.
     *
     * @param type тип бонуса, который нужно остановить
     */
    public void stopReward(String type) {
        if ("UltraBall".equals(type)) {
            for (int i = currentRewards.size() - 1; i >= 0; i--) {
                if (type.equals(currentRewards.get(i).type)) {
                    game.ball.ultraBallMode = false;
                    currentRewards.get(i).rewardOn = false;
                    game.text.rewardsLabel.setText("");
                    currentRewards.remove(i);
                }
            }
            game.ball.ultraBallMode = false;
        } else if ("BigBall".equals(type) || "SmallBall".equals(type)) {
            game.ball.diameter = Ball.DEFAULT_DIAMETER;
        } else if ("BigBar".equals(type) || "SmallBar".equals(type)) {
            game.bar.barWidth = 30;
            game.bar.barSideWidth = 20;
            game.bar.barMainColor = Color.WHITE;
            game.bar.barSideColor = Color.GRAY;
        }
    }

    /**
     * Останавливает все временные бонусы и восстанавливает параметры шара и платформы.
     *
     * <p>Метод вызывается при потере жизни и переходе на следующий уровень,
     * чтобы новый раунд начинался из стабильного состояния.</p>
     */
    public void stopAllRewards() {
        stopReward("UltraBall");
        stopReward("BigBall");
        stopReward("SmallBall");
        stopReward("BigBar");
        stopReward("SmallBar");
    }

    /**
     * Отрисовывает иконки падающих бонусов.
     *
     * <p>Каждый тип бонуса имеет собственную простую геометрию и цвет. Метод
     * рисует только те бонусы, которые еще находятся в состоянии падения.</p>
     *
     * @param graphics графический контекст, в который рисуются бонусы
     */
    public void paintBrickReward(Graphics2D graphics) {
        for (int i = 0; i < currentRewards.size(); i++) {
            if ("UltraBall".equals(currentRewards.get(i).type) && currentRewards.get(i).rewardBrickOn) {
                if (game.ball.ultraBallColor == 0) {
                    graphics.setColor(Color.RED);
                    game.ball.ultraBallColor++;
                } else if (game.ball.ultraBallColor == 1) {
                    graphics.setColor(Color.YELLOW);
                    game.ball.ultraBallColor++;
                } else if (game.ball.ultraBallColor == 2) {
                    graphics.setColor(Color.GREEN);
                    game.ball.ultraBallColor = 0;
                }
                graphics.fillRoundRect(currentRewards.get(i).x, currentRewards.get(i).y, currentRewards.get(i).width,
                        currentRewards.get(i).height, 10, 10);
            } else if ("BigBall".equals(currentRewards.get(i).type) && currentRewards.get(i).rewardBrickOn) {
                graphics.setColor(Color.WHITE);
                graphics.fillRoundRect(currentRewards.get(i).x, currentRewards.get(i).y, currentRewards.get(i).width,
                        currentRewards.get(i).height, 100, 100);
            } else if ("SmallBall".equals(currentRewards.get(i).type) && currentRewards.get(i).rewardBrickOn) {
                graphics.setColor(Color.WHITE);
                graphics.fillRoundRect(currentRewards.get(i).x, currentRewards.get(i).y, currentRewards.get(i).width,
                        currentRewards.get(i).height, 100, 100);
            } else if ("BigBar".equals(currentRewards.get(i).type) && currentRewards.get(i).rewardBrickOn) {
                graphics.setColor(Color.CYAN);
                graphics.fillRoundRect(currentRewards.get(i).x, currentRewards.get(i).y, currentRewards.get(i).width,
                        currentRewards.get(i).height, 10, 10);
                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("SansSerif", Font.BOLD, 10));
                graphics.drawString(">        <", currentRewards.get(i).x + 7, currentRewards.get(i).y + 7);
            } else if ("SmallBar".equals(currentRewards.get(i).type) && currentRewards.get(i).rewardBrickOn) {
                graphics.setColor(Color.CYAN);
                graphics.fillRoundRect(currentRewards.get(i).x, currentRewards.get(i).y, currentRewards.get(i).width,
                        currentRewards.get(i).height, 10, 10);
                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("SansSerif", Font.BOLD, 10));
                graphics.drawString(">        <", currentRewards.get(i).x + 7, currentRewards.get(i).y + 7);
            } else if ("ExtraLive".equals(currentRewards.get(i).type) && currentRewards.get(i).rewardBrickOn) {
                graphics.setColor(Color.PINK);
                graphics.fillRoundRect(currentRewards.get(i).x, currentRewards.get(i).y, 8,
                        8, 100, 100);
                graphics.fillRoundRect(currentRewards.get(i).x + 8, currentRewards.get(i).y, 8,
                        8, 100, 100);
                int[] xPoints = {currentRewards.get(i).x, currentRewards.get(i).x + 8, currentRewards.get(i).x + 16};
                int[] yPoints = {currentRewards.get(i).y + 4, currentRewards.get(i).y + 15, currentRewards.get(i).y + 4};
                int nPoints = 3;
                graphics.fillPolygon(xPoints, yPoints, nPoints);
            }
        }
    }

    /**
     * Обновляет таймеры активных бонусов.
     *
     * <p>Сейчас таймер есть только у {@code UltraBall}. Когда счетчик доходит
     * до нуля, эффект автоматически выключается.</p>
     */
    public void paintReward() {
        for (int i = 0; i < currentRewards.size(); i++) {
            if (currentRewards.get(i).rewardOn) {
                if ("UltraBall".equals(currentRewards.get(i).type)) {
                    if (ultraBallTime == 0) {
                        stopReward("UltraBall");
                    }
                    if (ultraBallTime > 0) {
                        game.text.rewardsLabel.setText("" + ultraBallTime);
                        ultraBallTime--;
                    }
                }
            }
        }
    }

    /**
     * Обновляет и отрисовывает падающие бонусы.
     *
     * <p>На каждом кадре бонус либо падает на один пиксель, либо применяется
     * при столкновении с платформой, либо удаляется при выходе за нижнюю
     * границу игрового окна.</p>
     *
     * @param graphics графический контекст, в который рисуются бонусы
     */
    public void paint(Graphics2D graphics) {
        for (int i = 0; i < currentRewards.size(); i++) {
            if (currentRewards.get(i).rewardBrickOn) {
                if (!barCollisson(i)) {
                    paintBrickReward(graphics);
                    if (currentRewards.get(i).y >= Arkanoid.HEIGHT) {
                        currentRewards.remove(i);
                    } else {
                        currentRewards.get(i).y++;
                    }
                } else if (barCollisson(i)) {
                    startReward(currentRewards.get(i).type);
                }
            }
        }
    }

    /**
     * Возвращает границы падающего бонуса.
     *
     * @param i индекс бонуса в списке {@link #currentRewards}
     * @return прямоугольник столкновения бонуса
     */
    public Rectangle getBounds(int i) {
        return new Rectangle(currentRewards.get(i).x, currentRewards.get(i).y,
                currentRewards.get(i).width, currentRewards.get(i).height);
    }

    /**
     * Проверяет столкновение бонуса с любой частью платформы.
     *
     * @param i индекс бонуса в списке {@link #currentRewards}
     * @return {@code true}, если бонус пересекается с центральной, левой или
     *         правой зоной платформы
     */
    private boolean barCollisson(int i) {
        if (game.bar.getBounds().intersects(getBounds(i))) {
            return true;
        } else if (game.bar.getBoundsLeft().intersects(getBounds(i))) {
            return true;
        } else if (game.bar.getBoundsRight().intersects(getBounds(i))) {
            return true;
        } else {
            return false;
        }
    }
}
