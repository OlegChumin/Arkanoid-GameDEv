package org.example.arkanoid;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;
import javax.swing.WindowConstants;

/**
 * Основная Swing-панель игры Arkanoid.
 *
 * <p>Класс объединяет все игровые объекты: шар, платформу, кирпичи, бонусы,
 * текстовые метки, уровни и обработчики ввода. Он же отвечает за отрисовку
 * кадра и за шаги игрового цикла.</p>
 *
 * @author Олег Чумин
 */
public class Arkanoid extends JPanel {
    /**
     * Версия сериализации Swing-компонента.
     *
     * <p>{@link JPanel} реализует {@link java.io.Serializable}, поэтому
     * наследник явно объявляет идентификатор версии.</p>
     */
    private static final long serialVersionUID = 1L;

    /**
     * Ширина игрового окна в пикселях.
     */
    public static final int WIDTH = 410; // это константа отвечает за ширину окна

    /**
     * Высота игрового окна в пикселях.
     */
    public static final int HEIGHT = 450; // это константа отвечает за высоту окна

    /**
     * Базовая задержка между кадрами игрового цикла.
     *
     * <p>Чем меньше значение, тем быстрее выполняется цикл.</p>
     */
    public static final int DEFAULT_SPEED = 6; // начальная скорость движения объектов в игре

    /**
     * Текущая задержка между кадрами для конкретной игровой панели.
     *
     * <p>Платформа и бонусы могут менять это значение во время игры.</p>
     */
    public int speed = DEFAULT_SPEED; // изменяема скорость игры

    /**
     * Признак паузы.
     *
     * <p>Если значение равно {@code true}, метод {@link #tick()} не двигает
     * шар и не обновляет игровую логику.</p>
     */
    boolean paused = false;

    /**
     * Признак ожидания первого клика на текущем раунде.
     *
     * <p>Пока значение равно {@code true}, шар стоит на месте. Клик мышью
     * переводит игру в активное состояние.</p>
     */
    boolean waitingToStart = true;

    /**
     * Счетчик кадров активной игры.
     *
     * <p>Используется для периодического сдвига кирпичей и обновления
     * таймеров бонусов.</p>
     */
    long timeCounter = 0;

    /**
     * Горизонтальная скорость шара перед постановкой на паузу.
     */
    int pausedBallXa = 0;

    /**
     * Вертикальная скорость шара перед постановкой на паузу.
     */
    int pausedBallYa = 0;

    /**
     * Генератор случайных значений для выбора начального направления шара.
     */
    private final Random random = new Random();

    /**
     * Создает игровую панель, настраивает базовые свойства Swing-компонента и фон.
     */
    public Arkanoid() {
        setLayout(null);
        setFocusable(true);
        setVisible(true);
        setBackground(new Color(0, 10, 59)); // цвет фона
    }

    /**
     * Шар, которым игрок разрушает кирпичи.
     */
    transient Ball ball = new Ball(this);

    /**
     * Платформа игрока.
     */
    transient Bar bar = new Bar(this);

    /**
     * Контейнер всех кирпичей текущего уровня.
     */
    transient Bricks brick = new Bricks(this);

    /**
     * Менеджер бонусов, выпадающих из кирпичей.
     */
    transient RewardsNew rewards = new RewardsNew(this);

    /**
     * Обработчики клавиатуры и мыши для текущей панели.
     */
    transient ListenersHandler listeners = new ListenersHandler(this); //?

    /**
     * Текстовые метки и верхняя информационная область.
     */
    transient Text text = new Text(this);

    /**
     * Генератор и переключатель уровней.
     */
    transient Levels levels = new Levels(this);


    /**
     * Выполняет движение шара на один шаг.
     *
     * <p>Метод выделен отдельно от {@link #tick()}, чтобы движение можно было
     * вызывать без перерисовки и счетчиков при тестировании игровой логики.</p>
     */
    void move() {
        ball.move();
    }

    /**
     * Отрисовывает полный кадр игры.
     *
     * @param graphics графический контекст Swing, переданный системой отрисовки
     */
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D g2D = (Graphics2D) graphics;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ball.paintBall(g2D);
        bar.paintBar(g2D);
        brick.paintBrick(g2D);
        rewards.paint(g2D);
        text.paint(g2D);
    }

    /**
     * Завершает игровую сессию после потери последней жизни.
     *
     * <p>Метод показывает диалоговое окно и завершает JVM. Это поведение
     * подходит для учебного desktop-приложения, но в более крупном приложении
     * его лучше заменить переходом в состояние {@code GAME_OVER}.</p>
     */
    public void gameOver() {
        JOptionPane.showMessageDialog(this, "Game Over", "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    /**
     * Запускает раунд или переключает паузу.
     *
     * <p>Первый клик задает шару начальное направление и снимает состояние
     * ожидания старта. Следующие клики сохраняют текущую скорость шара,
     * останавливают его на паузе и затем восстанавливают сохраненную скорость.</p>
     *
     */
    public void startGame() {
        if (waitingToStart) {
            //выбираем случайное направление меча при старте игры
            int xDirection = random.nextInt(2) == 0 ? 1 : -1;
            ball.ya = -1;
            ball.xa = xDirection;
            waitingToStart = false;
            text.startLabel.setText("");
            text.levelLabel.setText("LEVEL " + Levels.currentLevel);
        } else { // пауза в игре
            if (!paused) {
                pausedBallXa = ball.xa;
                pausedBallYa = ball.ya;
                ball.ya = 0;
                ball.xa = 0;
                text.startLabel.setText("Game Paused by GameDev2D");
                text.startLabel.setForeground(Color.RED);
                paused = true;
            } else {
                // возобновляем игру
                ball.xa = pausedBallXa;
                ball.ya = pausedBallYa;
                text.startLabel.setText("");
                text.startLabel.setForeground(Color.GREEN);
                paused = false;
            }
        }
    }

    /**
     * Выполняет один логический кадр игры.
     *
     * <p>Если игра не запущена или стоит на паузе, метод ничего не делает.
     * В активном состоянии метод двигает шар, перерисовывает панель, увеличивает
     * счетчик кадров, периодически сдвигает кирпичи вниз и обновляет таймеры
     * бонусов.</p>
     */
    void tick() {
        if (!paused && !waitingToStart) {
            move();
            repaint();
            timeCounter++;
            if (timeCounter % 100 == 0) {
                if ((timeCounter / 100) % 15 == 0) {
                    for (Bricks.Brick gameBrick : brick.bricks) {
                        gameBrick.y += 10;
                    }
                }
                rewards.paintReward();
            }
        }
    }

    /**
     * Сбрасывает общее состояние игры к начальным значениям.
     *
     * <p>Тесты вызывают этот метод перед созданием новой панели, чтобы результат
     * одного теста не влиял на следующий. Метод оставлен только для тех частей
     * модели, которые пока остаются статическими.</p>
     */
    public static void resetStaticState() {
        Levels.currentLevel = 1;
    }

    /**
     * Открывает игровое окно и запускает бесконечный игровой цикл.
     *
     * @param args аргументы командной строки; текущая версия их не использует
     * @throws InterruptedException если поток был прерван во время задержки между кадрами
     */
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Arkanoid game by GameDev2D");
        Arkanoid game = new Arkanoid();
        frame.getContentPane().add(game);
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); // способ отцентровать окно игры или приложения
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        game.requestFocusInWindow();

        while (true) {
            game.tick();
            Thread.sleep(game.speed);
        }
    }
}
