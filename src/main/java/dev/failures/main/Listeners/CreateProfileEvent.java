package dev.failures.main.Listeners;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import dev.failures.main.GachaRPG;
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
        MongoCollection<Document> db = GachaRPG.col;

        Document player = db.find(Filters.eq("uuid",p.getUniqueId().toString())).first();
        if(player == null) {
            Document data = new Document("uuid", p.getUniqueId().toString())
                    .append("name", p.getDisplayName())
                    .append("level", 1)
                    .append("exp", 0)
                    .append("gold", 0);
            GachaRPG.col.insertOne(data);
            p.setLevel(1);
        }
    }
}
