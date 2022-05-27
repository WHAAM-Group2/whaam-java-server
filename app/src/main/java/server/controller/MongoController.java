package server.controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.changestream.FullDocument;

import org.bson.Document;

import server.model.Status;

/**
 * @author Wael Mahrous
 *         Class that listens and handles the connection with the database
 *         MongoDB.
 */
public class MongoController extends Thread {

    MongoClient mongoClient;
    String connectionString;
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Initiates the connection to the specified connection string.
     * 
     * @param connectionString
     */
    public MongoController(String connectionString) {

        this.connectionString = connectionString;
        start();

    }

    /**
     * Resets game status for everyone on the database. Thereafter actively listens
     * to changes in the configuration document and notifies the game controller.
     */
    @Override
    public void run() {

        System.out.println("Connecting to MongoDB...");

        mongoClient = MongoClients.create(connectionString);

        MongoCollection<Document> collection;

        collection = mongoClient.getDatabase("configuration").getCollection("players");
        collection.updateMany(new Document(), new Document("$set", new Document("status", "not playing")));

        collection = mongoClient
                .getDatabase("configuration")
                .getCollection("setup");

        collection.findOneAndUpdate(new Document("name", "setup"), new Document("$set", new Document("status", false)));

        while (!interrupted()) {

            var watchCursor = collection.watch()
                    .fullDocument(FullDocument.UPDATE_LOOKUP);

            watchCursor.forEach(document -> {

                pcs.firePropertyChange("configuration", null, document.getFullDocument());

            });

        }

    }

    /**
     * Represents an end of game for the database. Will skip updating scoreboard if score is corrupted.
     * @param username
     * @param l
     */

    public void endGame(String username, long l) {

        MongoCollection<Document> collection = mongoClient
                .getDatabase("configuration")
                .getCollection("setup");

        collection.findOneAndUpdate(new Document("name", "setup"), new Document("$set", new Document("status", false)));

        collection = mongoClient
                .getDatabase("configuration")
                .getCollection("players");

        collection.findOneAndUpdate(new Document("username", username),
                new Document("$set", new Document("status", "not playing")));

        if (l > 99) {
            return;
        }

        collection = mongoClient.getDatabase("stats").getCollection("scoreboard");

        if (collection.countDocuments(new Document("username", username)) == 0) {
            collection.insertOne(new Document("username", username).append("score", l).append("player", username));
        } else {
            collection.findOneAndUpdate(new Document("username", username),
                    new Document("$set", new Document("score", l).append("player", username)));
        }

    }

    /**
     * Starts a game for the specified username.
     * @param username
     */

    public void startGame(String username) {

        MongoCollection<Document> collection = mongoClient
                .getDatabase("configuration")
                .getCollection("players");

        collection.find(new Document("username", username));

        if (collection.countDocuments(new Document("username", username)) == 0) {
            collection.insertOne(new Document("username", username).append("status", "playing"));
        } else {
            collection.findOneAndUpdate(new Document("username", username),
                    new Document("$set", new Document("status", "playing")));
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Updates the specified player's latest game stat.
     * @param username
     * @param status
     */
    public void updatePlayerStat(String username, Status status) {

        MongoCollection<Document> collection = mongoClient
                .getDatabase("configuration")
                .getCollection("players");

        switch (status) {

            case WIN:

                collection.findOneAndUpdate(new Document("username", username),
                        new Document("$set", new Document("status", "win")));

                break;

            case LOSS:

                collection.findOneAndUpdate(new Document("username", username),
                        new Document("$set", new Document("status", "loss")));

                break;

            default:
                break;
        }

    }

}
