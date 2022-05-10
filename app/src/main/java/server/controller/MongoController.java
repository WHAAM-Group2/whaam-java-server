package server.controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.changestream.FullDocument;

import org.bson.Document;

import server.model.Status;

public class MongoController extends Thread {

    MongoClient mongoClient;
    String connectionString;
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public MongoController(String connectionString) {

        this.connectionString = connectionString;
        start();

    }

    @Override
    public void run() {

        System.out.println("Connecting to MongoDB...");

        mongoClient = MongoClients.create(connectionString);

        MongoCollection<Document> collection = mongoClient
                .getDatabase("configuration")
                .getCollection("setup");

        while (!interrupted()) {

            var watchCursor = collection.watch()
                    .fullDocument(FullDocument.UPDATE_LOOKUP);

            watchCursor.forEach(document -> {

                pcs.firePropertyChange("configuration", null, document.getFullDocument());

            });

        }

    }

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
