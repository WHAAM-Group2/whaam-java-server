package server.model;

import java.util.Date;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;

import server.controller.GameController;
import server.model.motion.DeepNeuralNetworkProcessor;
import server.model.motion.DnnObject;
import server.model.motion.Person;

public class Game {

    private long startTime;
    private long endTime;
    private double score;
    private GameController gameController;

    private int mistakesCount;

    public Game(GameController gameController) {

        this.gameController = gameController;
        startGame();

    }

    public void startGame() {

        Date date = new Date();
        startTime = date.getTime();
        mistakesCount = 0;

        Person player = new Person("Wael");

        while (true) {

            gameController.updatePlayer(player);
            player.showGauge();

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
