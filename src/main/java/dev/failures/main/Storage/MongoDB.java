package dev.failures.main.Storage;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDB {
    private MongoCollection<Document> collection;
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDB() {
        mongoClient = MongoClients.create("mongodb+srv://admin:gacharpg123@gacharpg.r4lca.mongodb.net/gacharpg?retryWrites=true&w=majority");
        database = mongoClient.getDatabase("gacharpg");
        collection = database.getCollection("playerdata");
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }
}
