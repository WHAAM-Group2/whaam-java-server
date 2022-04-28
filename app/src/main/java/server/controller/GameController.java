package server.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.bson.Document;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.videoio.VideoCapture;

import server.model.Game;
import server.model.motion.DeepNeuralNetworkProcessor;
import server.model.motion.DnnObject;
import server.model.motion.Person;

public class GameController implements PropertyChangeListener {

    private MongoController mongo;
    private DeepNeuralNetworkProcessor processor;
    private VideoCapture camera;
    private Game game;

    public GameController(MongoController mongo) {

        this.mongo = mongo;
        this.mongo.addPropertyChangeListener(this);

        processor = new DeepNeuralNetworkProcessor();
        camera = new VideoCapture(0);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        Document document = (Document) evt.getNewValue();

        if ((boolean) document.get("status")) {

            System.out.println("Starting a game...");

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

    public void endGame() {
        game.endGame();
        mongo.endGame();
        game = null;
    }

}