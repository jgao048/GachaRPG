package dev.failures.main.Handlers;

import dev.failures.main.Storage.DataKeys;
import dev.failures.main.Utils.PDUtil;
import org.bukkit.entity.Player;

public class PlayerHandler {
    Player p;
    PDUtil playerGold;
    PDUtil playerExp;
    PDUtil playerLevel;
    PDUtil playerStr;

    public PlayerHandler(Player player) {
        p = player;
        playerGold = new PDUtil(DataKeys.PLAYER_GOLD);
        playerExp = new PDUtil(DataKeys.PLAYER_EXPERIENCE);
        playerLevel = new PDUtil(DataKeys.PLAYER_LEVEL);
        playerStr = new PDUtil(DataKeys.PLAYER_STRENGTH);
    }

    public void addGold(double amount) {
        double currentBalance = getBalance();
        setGold(currentBalance + amount);
    }

    public double getBalance() {
        return playerGold.getPlayerDataDouble(p);
    }

    public void setGold(double amount) {
        playerGold.setPlayerDataDouble(p, amount);
    }

    public int getLevel() {
        return playerLevel.getPlayerDataInteger(p);
    }

    public void setLevel(int level) {
        playerLevel.setPlayerDataInteger(p, level);
        recalculate();
    }

    public void addLevel(int amount) {
        int currentLevel = getLevel();
        setLevel(currentLevel + amount);
        recalculate();
    }

    public double getExp() {
        return playerExp.getPlayerDataDouble(p);
    }

    public void addExp(double amount) {
        double currentExp = getExp();
        setExp(currentExp + amount);
        recalculate();
    }

    public void setExp(double exp) {
        playerExp.setPlayerDataDouble(p, exp);
        recalculate();
    }

    public void recalculate() {
        //recalculate exp and level
    }

}
