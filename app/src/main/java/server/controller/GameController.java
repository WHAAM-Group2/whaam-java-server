package server.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

import org.bson.Document;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import server.App;
import server.model.Game;
import server.model.Music;
import server.model.Status;
import server.model.motion.DeepNeuralNetworkProcessor;
import server.model.motion.DnnObject;
import server.model.motion.Person;

/**
 * @author Wael Mahrous & Anna Selstam
 *         This class handles the logic/delegation of the program.
 */
public class GameController implements PropertyChangeListener {

    private MongoController mongo;
    private DeepNeuralNetworkProcessor processor;
    private VideoCapture camera;
    private Music music;
    private Game game;
    private ArduinoHandler arduino;
    private String username;
    private boolean gameStatus = false;
    private Person player;

    /**
     * Contructor.
     * 
     * @param mongo - Opens up connection towards the database.
     */
    public GameController(MongoController mongo) throws IOException {

        this.mongo = mongo;
        this.mongo.addPropertyChangeListener(this);

        processor = new DeepNeuralNetworkProcessor();

        camera = new VideoCapture(App.cameraPort);
        player = new Person("Player");

        try {
            arduino = new ArduinoHandler(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new PlayerShower();

    }

    /**
     * When triggered because of a new "signed in" player at the website,
     * it starts a new game and intiates the music.
     * Game then runs the session.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        Document document = (Document) evt.getNewValue();

        if ((boolean) document.get("status")) {

            System.out.println("Starting a game...");

            username = ((String) document.get("username"));

            do {

                System.out.println("Waiting...");

            } while (!getArduino().getSensorCovered());

            music = new Music();
            music.start();

            mongo.startGame(username);
            gameStatus = true;
            game = new Game(this, player);

        } else {

            System.out.println("Game ended");

        }

    }

    /**
     * Assigns a player its movement shown on the frame which displays the camera.
     * 
     * @param player
     */
    public void updatePlayer(Person player) {

        Mat frame = new Mat();
        camera.read(frame);

        List<DnnObject> detectObject = processor.getObjectsInFrame(frame, false);

        try {

            DnnObject person = detectObject.stream().filter(o -> o.getObjectName().equals("person"))
                    .findFirst()
                    .get();

            player.addRect(new Rect(person.getLeftBottom(), person.getRightTop()));
            player.updateMovement();

            Imgproc.rectangle(frame, person.getLeftBottom(), person.getRightTop(), new Scalar(255));

            Imgproc.putText(frame, String.format("HUMAN MOVING: %.0f%%", player.getLerpPercent()),
                    person.getLeftBottom(), 1,
                    1, new Scalar(0, 0, 255));

        } catch (Exception e) {
            // e.printStackTrace();
        }

        HighGui.imshow("BUTCH EYES", frame);

    }

    /**
     * Called on by Game, this updates the details to the database through a
     * method in MongoController, stops the music and plays the correct music
     * according to the status.
     * 
     * @param l      - seconds the game was running
     * @param status - enum, either Win or Loss
     */
    public void endGame(long l, Status status) {

        // If the time actually is time over 0, it will register as normal.
        // Else, it will automatically assign it a number so large, it will never make
        // the highscore.
        
        if (l != 0) {
            mongo.endGame(username, l);
        } else {
            mongo.endGame(username, 100);
        }

        mongo.updatePlayerStat(username, status);
        music.stopGame();

        gameStatus = false;

        switch (status) {

            case WIN:

                music.playFinish();
                break;

            case LOSS:

                music.playLoose();
                break;

            default:
                break;

        }

    }

    /**
     * Class to update and show player movement as no game is ongoing. Looks pretty.
     */
    private class PlayerShower extends Thread {

        public PlayerShower() {
            start();
        }

        @Override
        public void run() {

            while (!interrupted()) {

                if (!gameStatus) {
                    updatePlayer(player);
                    player.showGauge();
                }

            }

        }

    }

    public boolean getMusicStatus() {
        return music.getMusicOn();
    }

    public MongoController getMongo() {
        return this.mongo;
    }

    public void setMongo(MongoController mongo) {
        this.mongo = mongo;
    }

    public DeepNeuralNetworkProcessor getProcessor() {
        return this.processor;
    }

    public void setProcessor(DeepNeuralNetworkProcessor processor) {
        this.processor = processor;
    }

    public VideoCapture getCamera() {
        return this.camera;
    }

    public void setCamera(VideoCapture camera) {
        this.camera = camera;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public ArduinoHandler getArduino() {
        return this.arduino;
    }

    public void setArduino(ArduinoHandler arduino) {
        this.arduino = arduino;
    }

    public Person getPlayer() {
        return this.player;
    }

    public void setPlayer(Person player) {
        this.player = player;
    }

    public Music getMusic() {
        return this.music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isGameStatus() {
        return this.gameStatus;
    }

    public boolean getGameStatus() {
        return this.gameStatus;
    }

    public void setGameStatus(boolean gameStatus) {
        this.gameStatus = gameStatus;
    }

}