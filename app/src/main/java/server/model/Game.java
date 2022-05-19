package server.model;

import java.util.Date;

import com.google.common.base.Stopwatch;

import server.controller.GameController;

import server.model.motion.Person;

public class Game {

    private long startTime;
    private long endTime;
    private double score;
    private GameController gameController;
    private Stopwatch stopwatch;
    private Person player;

    private int mistakesCount;

    private int difficultyTimer;
    private int difficultyLERP;

    public Game(GameController gameController, Person player) {

        this.gameController = gameController;
        this.player = player;

        this.stopwatch = Stopwatch.createUnstarted();

        difficultyTimer = 1200;
        difficultyLERP = 7;

        startGame();

    }

    public void startGame() {

        Date date = new Date();
        startTime = date.getTime();
        mistakesCount = 0;

        boolean danger = false;

        Stopwatch dangerTimer = Stopwatch.createUnstarted();

        stopwatch.start();

        while (true) {

            gameController.updatePlayer(player);
            player.showGauge();

            if (!gameController.getMusicStatus()) {

                if (danger) {

                    if (dangerTimer.elapsed().toMillis() < difficultyTimer
                            || stopwatch.elapsed().toSeconds() >= 60) {
                        continue;
                    }

                    if (player.getLerpPercent() > difficultyLERP) {
                        gameController.endGame(0, Status.LOSS);
                        break;
                    }

                } else {

                    danger = true;
                    dangerTimer.start();

                }

            } else {

                if (danger) {
                    danger = false;
                    dangerTimer.stop();
                    dangerTimer.reset();
                }

            }

            if (gameController.getArduino().getWin()) {
                gameController.endGame(stopwatch.elapsed().toSeconds(), Status.WIN);
                System.out.println("Game ended.");
                break;
            }

            // if (!gameController.getArduino().getWin()) {

            // } else {

            // gameController.endGame(stopwatch.elapsed().toSeconds(), Status.WIN);
            // System.out.println("Game ended.");
            // break;

            // }

        }

    }

    // public void endGame() {

    // Date date = new Date();
    // endTime = date.getTime();

    // score = (endTime - startTime);

    // setMistakesCount(0);

    // }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double calculateScore() {

        return getScore() / 1000;

    }

}
