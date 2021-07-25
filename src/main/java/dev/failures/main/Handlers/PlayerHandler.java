package dev.failures.main.Handlers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import dev.failures.main.GachaRPG;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.logging.Filter;

public class PlayerHandler {
    Player p;
    int playerLevel;
    double playerExperience;
    double playerGold;

    public PlayerHandler(Player player, int level, double exp, double gold) {
        p = player;
        playerLevel = level;
        playerExperience = exp;
        playerGold = gold;
    }
}
