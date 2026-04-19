package org.example;

import org.example.arkanoid.Arkanoid;

/**
 * Точка входа в приложение.
 *
 * <p>Класс не содержит игровой логики. Его задача - передать управление
 * выбранной реализации игры в пакете {@code org.example.arkanoid}.</p>
 *
 * @author Олег Чумин
 */
public class ArkanoidGameStart {
    /**
     * Закрытый конструктор для служебного класса запуска.
     *
     * <p>Экземпляры {@code ArkanoidGameStart} не нужны: приложение запускается через
     * статический метод {@link #main(String[])}.</p>
     */
    private ArkanoidGameStart() {
    }

    /**
     * Запускает Swing-версию Arkanoid.
     *
     * @param args аргументы командной строки; текущая версия игры их не использует
     * @throws InterruptedException если ожидание между кадрами игрового цикла было прервано
     */
    public static void main(String[] args) throws InterruptedException {
        Arkanoid.main(args);
    }
}
