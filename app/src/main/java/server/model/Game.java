package server.model;

import java.util.Date;

public class Game {

    private long startTime;
    private long endTime;
    private double score;

    private int mistakesCount;

    public void startGame() {

        Date date = new Date();
        startTime = date.getTime();
        mistakesCount = 0;

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
