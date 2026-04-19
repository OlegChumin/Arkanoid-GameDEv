package org.example.arkanoid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArkanoidGameTest {
    @BeforeEach
    void setUp() {
        System.setProperty("java.awt.headless", "true");
        Arkanoid.resetStaticState();
    }

    @Test
    void createsFirstLevelWithoutDuplicatingRows() {
        Arkanoid game = newGame();

        assertEquals(5, game.brick.brickRows.size());
        assertEquals(27, game.brick.bricks.size());
        assertEquals(27, game.brick.brickRows.stream().mapToInt(List::size).sum());

        game.levels.createLevel();

        assertEquals(5, game.brick.brickRows.size());
        assertEquals(27, game.brick.bricks.size());
        assertEquals(27, game.brick.brickRows.stream().mapToInt(row -> row.size()).sum());
    }

    @Test
    void startNewLevelResetsBallPaddleAndBuildsNextLevel() {
        Arkanoid game = newGame();
        game.ball.x = 10;
        game.ball.y = 20;
        game.ball.xa = 1;
        game.ball.ya = -1;
        game.bar.x = 42;
        game.speed = 3;

        Levels.startNewLevel(game);

        assertEquals(2, Levels.currentLevel);
        assertEquals(Ball.DEFAULT_X, game.ball.x);
        assertEquals(Ball.DEFAULT_Y, game.ball.y);
        assertEquals(0, game.ball.xa);
        assertEquals(0, game.ball.ya);
        assertEquals(Bar.DEFAULT_X, game.bar.x);
        assertEquals(Arkanoid.DEFAULT_SPEED, game.speed);
        assertTrue(game.waitingToStart);
        assertEquals(45, game.brick.bricks.size());
    }

    @Test
    void losingLifeResetsRoundStateAndTemporaryRewards() {
        Arkanoid game = newGame();
        game.waitingToStart = false;
        game.ball.x = 100;
        game.ball.y = 120;
        game.ball.xa = 1;
        game.ball.ya = 1;
        game.bar.x = 99;
        game.rewards.createReward("BigBall", 10, 10);
        game.rewards.startReward("BigBall");

        Bar.looseLive(game);

        assertEquals(2, game.bar.lives);
        assertEquals(Ball.DEFAULT_X, game.ball.x);
        assertEquals(Ball.DEFAULT_Y, game.ball.y);
        assertEquals(0, game.ball.xa);
        assertEquals(0, game.ball.ya);
        assertEquals(Bar.DEFAULT_X, game.bar.x);
        assertEquals(Arkanoid.DEFAULT_SPEED, game.speed);
        assertEquals(10, game.ball.diameter);
        assertTrue(game.waitingToStart);
    }

    @Test
    void ultraBallRewardStartsAndStopsCleanly() {
        Arkanoid game = newGame();
        game.rewards.createReward("UltraBall", 10, 10);

        game.rewards.startReward("UltraBall");

        assertTrue(game.ball.ultraBallMode);
        assertEquals(1, game.rewards.currentRewards.size());
        assertTrue(game.rewards.currentRewards.get(0).rewardOn);
        assertFalse(game.rewards.currentRewards.get(0).rewardBrickOn);

        game.rewards.stopAllRewards();

        assertFalse(game.ball.ultraBallMode);
        assertEquals(0, game.rewards.currentRewards.size());
    }

    private Arkanoid newGame() {
        Arkanoid game = new Arkanoid();
        game.setSize(Arkanoid.WIDTH, Arkanoid.HEIGHT);
        return game;
    }
}
