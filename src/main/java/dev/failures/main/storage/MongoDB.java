package dev.failures.main.storage;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PlayerData;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class MongoDB {
    private final MongoCollection<Document> collection;

    public MongoDB() {
        MongoClient mongoClient = MongoClients.create("mongodb+srv://admin:gacharpg123@gacharpg.r4lca.mongodb.net/gacharpg?retryWrites=true&w=majority");
        MongoDatabase database = mongoClient.getDatabase("gacharpg");
        collection = database.getCollection("playerdata");
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }

    public CompletableFuture<PlayerData> getData(Player p) {
        String pid = p.getUniqueId().toString();
        return CompletableFuture.supplyAsync(()->{
            String data = collection.find(Filters.eq("uuid", pid)).first().getString("data");
            return GachaRPG.gson.fromJson(data, PlayerData.class);
        });
    }


    public void saveData(PlayerData p, String pid) {
        Bukkit.getScheduler().runTaskAsynchronously(GachaRPG.getInstance(), new Runnable() {
            @Override
            public void run() {
                    String playerJson = GachaRPG.gson.toJson(p);
                    collection.updateOne(Filters.eq("uuid",pid), Updates.set("data",playerJson));
            }
        });
    }
}
