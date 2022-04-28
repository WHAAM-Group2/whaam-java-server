package server.controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.changestream.FullDocument;

import org.bson.Document;

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

    public void endGame() {

        MongoCollection<Document> collection = mongoClient
                .getDatabase("configuration")
                .getCollection("setup");

        collection.findOneAndUpdate(new Document("name", "setup"), new Document("$set", new Document("status", false)));

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

}
