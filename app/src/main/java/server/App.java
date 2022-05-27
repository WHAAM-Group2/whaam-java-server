package server;

import java.io.IOException;

import com.mongodb.client.MongoClient;

import server.controller.GameController;
import server.controller.MongoController;
import server.misc.ShortcutFunctions;

/**
 * @author Wael Mahrous
 * 
 *         Main class. Needs argument to connect to correct camera. Will
 *         thereafter start a mongocontroller and gamecontroller.
 */

public class App {

    MongoClient mongoClient;

    public static int cameraPort = 0;

    public static void main(String[] args) {

        if (args.length > 0) {
            cameraPort = Integer.parseInt(args[0]);
        }

        try {

            ShortcutFunctions.initializeOpencv();

            MongoController mongo = new MongoController("mongodb://localhost:27017");
            new GameController(mongo);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
