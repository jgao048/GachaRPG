package dev.failures.main.Listeners;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import dev.failures.main.GachaRPG;
import dev.failures.main.Handlers.PlayerHandler;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CreateProfileEvent implements Listener {
    private GachaRPG main;

    public CreateProfileEvent(GachaRPG main) { this.main = main; }

    @EventHandler
    private void playerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String pid = p.getUniqueId().toString();
        MongoCollection<Document> db = GachaRPG.col;

        if(db.find(Filters.eq("uuid",pid)).first() == null) {
            PlayerHandler player = new PlayerHandler(p, 1, 0, 100, 0);
            Gson gson = new Gson();
            String playerJson = gson.toJson(player);

            Document playerData = new Document("uuid",pid).append("data",playerJson);
            db.insertOne(playerData);
        }

    }
}
