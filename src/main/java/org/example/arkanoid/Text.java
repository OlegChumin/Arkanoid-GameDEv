package org.example.arkanoid;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Текстовый слой игры.
 *
 * <p>Класс создает и обновляет Swing-метки, которые отображают стартовое
 * сообщение, уровень, жизни и таймер активного бонуса. Также он рисует линии
 * верхней информационной панели.</p>
 */
public class Text {
    /**
     * Высота верхней информационной панели.
     */
    public static final int MENU_BAR_HEIGHT = 25;

    /**
     * Метка с сообщением старта, паузы или перехода на уровень.
     */
    JLabel startLabel = new JLabel("Level " + Levels.currentLevel + "Click to start", SwingConstants.CENTER);

    /**
     * Метка таймера активного бонуса.
     */
    JLabel rewardsLabel = new JLabel("", SwingConstants.CENTER);

    /**
     * Текстовая часть блока жизней.
     */
    JLabel livesTextLabel = new JLabel("||| Lives: ");

    /**
     * Метка с числом оставшихся жизней.
     */
    JLabel livesLabel = new JLabel("");

    /**
     * Метка текущего уровня.
     */
    JLabel levelLabel = new JLabel("LEVEL " + Levels.currentLevel, SwingConstants.CENTER);

    /**
     * Шрифт стартового сообщения и счетчика жизней.
     */
    Font levelToStartFont = new Font("courier", Font.PLAIN, 13);

    /**
     * Шрифт таймера бонуса.
     */
    Font rewardsFont = new Font("courier", Font.BOLD, 17);

    /**
     * Шрифт метки уровня.
     */
    Font levelFont = new Font("courier", Font.BOLD, 13);

    /**
     * Игровая панель, на которую добавляются текстовые метки.
     */
    private Arkanoid game;

    /**
     * Создает текстовый слой и добавляет все метки на игровую панель.
     *
     * @param game игровая панель, на которой размещается текст
     */
    public Text(Arkanoid game) {
        this.game = game;
        makeStartLabel();
        makeRewardsLabel();
        makeLivesLabel();
        makeLevelLabel();
    }

    /**
     * Создает и размещает стартовую метку.
     *
     * <p>Эта же метка используется для сообщения о паузе и о переходе на
     * следующий уровень.</p>
     */
    void makeStartLabel() {
        startLabel.setVisible(true);
        startLabel.setBounds(0, 155, Arkanoid.WIDTH, 100);
        startLabel.setFont(levelToStartFont);
        startLabel.setForeground(Color.GREEN);
        game.add(startLabel);
    }

    /**
     * Создает и размещает метку таймера бонуса.
     */
    void makeRewardsLabel() {
        rewardsLabel.setVisible(true);
        rewardsLabel.setBounds(0, 0, Arkanoid.WIDTH, 100);
        rewardsLabel.setFont(rewardsFont);
        rewardsLabel.setForeground(Color.CYAN);
        game.add(rewardsLabel);
    }

    /**
     * Создает, размещает и синхронизирует метки жизней.
     *
     * <p>Метод вызывается при создании текстового слоя и при отрисовке, чтобы
     * значение на экране соответствовало текущему числу жизней в {@link Bar}.</p>
     */
    void makeLivesLabel() {
        livesTextLabel.setVisible(true);
        livesTextLabel.setBounds(Arkanoid.WIDTH - 103, -33, Arkanoid.WIDTH, 100);
        livesLabel.setBounds(Arkanoid.WIDTH - 22, -33, Arkanoid.WIDTH, 100);
        livesTextLabel.setFont(levelToStartFont);
        livesLabel.setFont(levelToStartFont);
        livesTextLabel.setForeground(Color.WHITE);
        livesLabel.setForeground(Color.GREEN);
        game.add(livesTextLabel);
        livesTextLabel.setText("" + game.bar.lives);
        game.add(livesLabel);
    }

    /**
     * Создает и размещает метку текущего уровня.
     */
    void makeLevelLabel() {
        levelLabel.setVisible(true);
        levelLabel.setBounds(0, -33, Arkanoid.WIDTH, 100);
        levelLabel.setFont(levelFont);
        levelLabel.setForeground(Color.WHITE);
        game.add(levelLabel);
    }

    /**
     * Отрисовывает верхнюю информационную область.
     *
     * <p>Метод рисует две горизонтальные линии и обновляет отображение жизней.</p>
     *
     * @param graphics графический контекст, в который рисуется текстовый слой
     */
    public void paint(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 5, Arkanoid.WIDTH, 2);
        graphics.fillRect(0, MENU_BAR_HEIGHT, Arkanoid.WIDTH, 2);
        makeLivesLabel();
    }
}
