package server.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

import org.bson.Document;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.videoio.VideoCapture;

import javaserver.TestClient;
import server.model.Game;
import server.model.MusicSimulator;
import server.model.Status;
import server.model.motion.DeepNeuralNetworkProcessor;
import server.model.motion.DnnObject;
import server.model.motion.Person;

public class GameController implements PropertyChangeListener {

    private MongoController mongo;
    private DeepNeuralNetworkProcessor processor;
    private VideoCapture camera;
    private MusicSimulator music;
    private Game game;
    private TestClient arduino;
    private String username;

    public GameController(MongoController mongo) throws IOException {

        this.mongo = mongo;
        this.mongo.addPropertyChangeListener(this);

        processor = new DeepNeuralNetworkProcessor();
        camera = new VideoCapture(0);

        music = new MusicSimulator();

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        Document document = (Document) evt.getNewValue();

        if ((boolean) document.get("status")) {

            System.out.println("Starting a game...");

            username = ((String) document.get("username"));

            music.getMp().start();

            try {
                arduino = new TestClient(this);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mongo.startGame(username);
            game = new Game(this);

        } else {

            System.out.println("Game ended");

        }

    }

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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void endGame(long l, Status status) {

        if (l != 0) {
            mongo.endGame(username, l);
        } else {
            mongo.endGame(username, 100);
        }

        mongo.updatePlayerStat(username, status);

        music.stopPlaying();

    }

    public boolean getMusicStatus() {
        return music.getMp().getRunningStatus();
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

    public MusicSimulator getMusic() {
        return this.music;
    }

    public void setMusic(MusicSimulator music) {
        this.music = music;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public TestClient getArduino() {
        return this.arduino;
    }

    public void setArduino(TestClient arduino) {
        this.arduino = arduino;
    }

}