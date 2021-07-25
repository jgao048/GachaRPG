package dev.failures.main.Handlers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import dev.failures.main.GachaRPG;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.logging.Filter;

public class PlayerHandler {
    MongoCollection<Document> data = GachaRPG.col;
    Player p;
    int playerLevel;
    double playerExperience;
    double playerGold;

    public PlayerHandler(Player player) {
        p = player;
    }

    public double getGold() {
        Filter filter;
        return data.find(Filters.eq("uuid",p.getUniqueId().toString())).first().getDouble("gold");
    }

}
