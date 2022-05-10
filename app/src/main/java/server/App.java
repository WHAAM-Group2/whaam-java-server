/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package server;

import java.io.IOException;

import com.mongodb.client.MongoClient;

import server.controller.GameController;
import server.controller.MongoController;
import server.misc.ShortcutFunctions;

public class App {

    MongoClient mongoClient;

    public static void main(String[] args) {

        try {

            ShortcutFunctions.initializeOpencv();

            MongoController mongo = new MongoController(args.length > 0 ? args[0] : "mongodb://localhost:27017");
            new GameController(mongo);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
