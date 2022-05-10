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

    private int mistakesCount;

    public Game(GameController gameController) {

        this.gameController = gameController;
        this.stopwatch = Stopwatch.createUnstarted();

        startGame();

    }

    public void startGame() {

        Date date = new Date();
        startTime = date.getTime();
        mistakesCount = 0;

        Person player = new Person("Wael");
        boolean danger = false;
        int count = 0;

        stopwatch.start();

        while (true) {

            gameController.updatePlayer(player);
            player.showGauge();

            // if (stopwatch.elapsed().toSeconds() > 5) {

            //     gameController.endGame();
            //     break;

            // }

            if (!gameController.getArduino().getWin()) {
                
                if (!gameController.getMusic().getMp().getRunningStatus()) {
                    
                    if (player.getLerpPercent() > 10) {
                        gameController.endGame(0, Status.LOSS);
                        break;
                    }

                }

            } else {

                gameController.endGame(stopwatch.elapsed().toSeconds(), Status.WIN);
                System.out.println("Game ended.");
                break;
            }

        }

    }

    public void endGame() {

        Date date = new Date();
        endTime = date.getTime();

        score = (endTime - startTime);

        setMistakesCount(0);

    }

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

    public int getMistakesCount() {
        return this.mistakesCount;
    }

    public void setMistakesCount(int mistakesCount) {
        this.mistakesCount = mistakesCount;
    }

    public void addMistake() {
        mistakesCount++;
    }

}
