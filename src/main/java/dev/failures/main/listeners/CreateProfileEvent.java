package dev.failures.main.listeners;

import com.mongodb.client.model.Filters;
import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PlayerData;
import dev.failures.main.storage.MongoDB;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class CreateProfileEvent implements Listener {
    private final GachaRPG main;
    private final MongoDB db;

    public CreateProfileEvent(GachaRPG main, MongoDB db) { this.main = main; this.db = db; }

    @EventHandler
    private void playerJoin(AsyncPlayerPreLoginEvent e) {
        String pid = e.getUniqueId().toString();
        Bukkit.getScheduler().runTaskAsynchronously(GachaRPG.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(db.getCollection().find(Filters.eq("uuid",pid)).first() == null) {
                    PlayerData player = new PlayerData(1, 0, 100, 0, 10, 10, 10, 10);
                    String playerJson = GachaRPG.gson.toJson(player);
                    Document playerData = new Document("uuid", pid).append("data", playerJson);
                    db.getCollection().insertOne(playerData);
                }
            }
        });
    }
}
