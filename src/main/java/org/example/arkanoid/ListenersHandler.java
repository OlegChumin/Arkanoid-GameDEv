package org.example.arkanoid;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Регистратор обработчиков ввода.
 *
 * <p>Класс подключает к игровой панели обработчики клавиатуры, движения мыши
 * и кликов мышью. Вся обработка ввода сведена в один класс, чтобы игровая
 * панель не реализовывала интерфейсы Swing напрямую.</p>
 */
public class ListenersHandler {
    /**
     * Игровая панель, состояние которой меняют обработчики ввода.
     */
    private Arkanoid game;

    /**
     * Код клавиши стрелки влево.
     */
    public static final int LEFT = 37; // надо проверить при отладке

    /**
     * Код клавиши стрелки вправо.
     */
    public static final int RIGHT = 39; // надо проверить при отладке

    /**
     * Создает и подключает все обработчики ввода к игровой панели.
     *
     * @param game игровая панель, которая принимает события клавиатуры и мыши
     */
    public ListenersHandler(Arkanoid game) {
        KeyListener keyListener = new MyKeyListener(); // здесь создаем объект для отслеживания лево и
        // право стрелок клавиатуры
        MouseMotionListener mouseMotionListener = new MyMouseMotionListener();
        MouseListener mouseListener = new MyMouseListener();
        game.addKeyListener(keyListener); // передаем объект в игру game
        game.addMouseMotionListener(mouseMotionListener);
        game.addMouseListener(mouseListener);
        this.game = game;
    }

    /**
     * Обработчик клавиатуры для движения платформы.
     */
    public class MyKeyListener implements KeyListener {
        /**
         * Создает обработчик клавиатуры, связанный с внешним {@link ListenersHandler}.
         */
        public MyKeyListener() {
        }

        /**
         * Обрабатывает ввод символа с клавиатуры.
         *
         * <p>В игре символы не используются, поэтому метод намеренно пустой.</p>
         *
         * @param e событие ввода символа
         */
        @Override
        public void keyTyped(KeyEvent e) {
            // здесь реализации у нас не будет
        }

        /**
         * Обрабатывает нажатие клавиши.
         *
         * <p>Стрелка влево сдвигает платформу влево, стрелка вправо - вправо.
         * Остальные клавиши игнорируются.</p>
         *
         * @param e событие нажатия клавиши
         */
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == LEFT) {
                game.bar.move(LEFT);
            } else if (e.getKeyCode() == RIGHT) {
                game.bar.move(RIGHT);
            }
        }

        /**
         * Обрабатывает отпускание клавиши.
         *
         * <p>Текущая версия игры реагирует только на факт нажатия, поэтому
         * отпускание клавиши не меняет состояние.</p>
         *
         * @param e событие отпускания клавиши
         */
        @Override
        public void keyReleased(KeyEvent e) {
            // здесь реализации у нас не будет
        }
    }

    /**
     * Обработчик движения мыши для прямого позиционирования платформы.
     */
    public class MyMouseMotionListener implements MouseMotionListener {
        /**
         * Создает обработчик движения мыши, связанный с внешним {@link ListenersHandler}.
         */
        public MyMouseMotionListener() {
        }

        /**
         * Обрабатывает перетаскивание мышью.
         *
         * <p>Перетаскивание в игре не используется, потому что платформа
         * следует за обычным движением курсора.</p>
         *
         * @param e событие перетаскивания мыши
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            // реализации нет
        }

        /**
         * Обрабатывает движение мыши по игровой панели.
         *
         * <p>Координата платформы выставляется так, чтобы ее центр находился
         * под курсором.</p>
         *
         * @param e событие движения мыши
         */
        @Override
        public void mouseMoved(MouseEvent e) {
            // доска - бита - платформа следует за указателем mouse
            game.bar.x = e.getX() - (game.bar.barWidth / 2);
        }
    }

    /**
     * Обработчик кликов мышью для старта и паузы игры.
     */
    public class MyMouseListener implements MouseListener {
        /**
         * Создает обработчик мыши, связанный с внешним {@link ListenersHandler}.
         */
        public MyMouseListener() {
        }

        /**
         * Обрабатывает клик мышью по игровой панели.
         *
         * <p>Клик запускает раунд, ставит игру на паузу или снимает игру с
         * паузы в зависимости от текущего состояния.</p>
         *
         * @param e событие клика мышью
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            game.startGame();
        }

        /**
         * Обрабатывает нажатие кнопки мыши.
         *
         * <p>Метод намеренно пустой: игра реагирует на полный клик, а не на
         * отдельное нажатие.</p>
         *
         * @param e событие нажатия кнопки мыши
         */
        @Override
        public void mousePressed(MouseEvent e) {
            // это метод не используем
        }

        /**
         * Обрабатывает отпускание кнопки мыши.
         *
         * <p>Метод намеренно пустой: в текущем управлении отпускание кнопки
         * мыши не имеет отдельного эффекта.</p>
         *
         * @param e событие отпускания кнопки мыши
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            // это метод не используем
        }

        /**
         * Обрабатывает вход курсора в область панели.
         *
         * <p>Метод намеренно пустой: вход курсора не запускает и не
         * останавливает игру.</p>
         *
         * @param e событие входа курсора в компонент
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            // это метод не используем
        }

        /**
         * Обрабатывает выход курсора из области панели.
         *
         * <p>Метод намеренно пустой: выход курсора не меняет состояние игры.</p>
         *
         * @param e событие выхода курсора из компонента
         */
        @Override
        public void mouseExited(MouseEvent e) {
            // это метод не используем
        }
    }
}
