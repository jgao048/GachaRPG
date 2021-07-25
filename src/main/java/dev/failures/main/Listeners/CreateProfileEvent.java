package dev.failures.main.Listeners;

import com.google.gson.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import dev.failures.main.GachaRPG;
import dev.failures.main.Handlers.PlayerHandler;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class CreateProfileEvent implements Listener {
    private GachaRPG main;
    private MongoCollection<Document> db;

    public CreateProfileEvent(GachaRPG main, MongoCollection<Document> collection) { this.main = main; db = collection; }

    @EventHandler
    private void playerJoin(AsyncPlayerPreLoginEvent e) {
        String pid = e.getUniqueId().toString();
        PlayerHandler player = new PlayerHandler(1, 0, 100, 0);

        main.getLogger().info("Test: " + player);
        Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
            @Override
            public void run() {
                if(db.find(Filters.eq("uuid",pid)).first() == null) {
                    String playerJson = GachaRPG.gson.toJson(player);
                    Document playerData = new Document("uuid",pid).append("data", playerJson);
                    db.insertOne(playerData);
                }
            }
        });
    }
}
