package dev.failures.main.Handlers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import dev.failures.main.GachaRPG;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.logging.Filter;

public class PlayerHandler {
    int playerLevel;
    double playerExperience;
    double playerGold;
    int skillPoints;

    public PlayerHandler(int level, double exp, double gold, int sp) {
        playerLevel = level;
        playerExperience = exp;
        playerGold = gold;
        skillPoints = sp;
    }

    public int getLevel() {
        return playerLevel;
    }

    public double getExp() {
        return playerExperience;
    }

    public double getGold() {
        return playerGold;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void setGold(double amount) {
        playerGold = amount;
    }

    public void addGold(double amount) {
        playerGold = playerGold + amount;
    }

}
