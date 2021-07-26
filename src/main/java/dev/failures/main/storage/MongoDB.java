package dev.failures.main.storage;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PlayerData;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MongoDB {
    private final MongoCollection<Document> collection;
    private PlayerData playerData;

    public MongoDB() {
        MongoClient mongoClient = MongoClients.create("mongodb+srv://admin:gacharpg123@gacharpg.r4lca.mongodb.net/gacharpg?retryWrites=true&w=majority");
        MongoDatabase database = mongoClient.getDatabase("gacharpg");
        collection = database.getCollection("playerdata");
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }

    public PlayerData getData(Player p) {
        String pid = p.getUniqueId().toString();
        Bukkit.getScheduler().runTaskAsynchronously(GachaRPG.getInstance(), new Runnable() {
            @Override
            public void run() {
                String data = collection.find(Filters.eq("uuid", pid)).first().getString("data");
                playerData = GachaRPG.gson.fromJson(data, PlayerData.class);
            }
        });
        return playerData;
    }

    public void saveData(PlayerData p, String pid) {
        Bukkit.getScheduler().runTaskAsynchronously(GachaRPG.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(collection.find(Filters.eq("uuid",pid)).first() == null) {
                    String playerJson = GachaRPG.gson.toJson(p);
                    Document playerData = new Document("uuid",pid).append("data", playerJson);
                    collection.insertOne(playerData);
                }
            }
        });
    }
}
