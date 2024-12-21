package com.economy;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;

public class DatabaseManager {
    private static final Dotenv DOTENV = Dotenv.load();

    private MongoClient mongoClient;
    private MongoDatabase database;

    public static String getMongoUri(){
        return DOTENV.get("MONGO_URI");
    }

    public void connect() {
        mongoClient = MongoClients.create(getMongoUri());
        database = mongoClient.getDatabase("economy");
    }

    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }
}
