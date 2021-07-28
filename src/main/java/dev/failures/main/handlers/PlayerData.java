package dev.failures.main.handlers;

import com.sun.java.swing.plaf.windows.WindowsTextAreaUI;
import dev.failures.main.storage.Values;

public class PlayerData {
    int playerLevel;
    double playerExperience;
    double playerGold;
    int skillPoints;
    int statStrength;
    int statAgility;
    int statIntelligence;
    int statVitality;

    public PlayerData(int level, double exp, double gold, int sp, int str, int agi, int intel, int vit) {
        playerLevel = level;
        playerExperience = exp;
        playerGold = gold;
        skillPoints = sp;
        statStrength = str;
        statAgility = agi;
        statIntelligence = intel;
        statVitality = vit;
    }

    public int getStr() {
        return statStrength;
    }

    public int getInt() {
        return statIntelligence;
    }

    public int getAgi() {
        return statAgility;
    }

    public int getVit() {
        return statVitality;
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

    public void setExp(double amount) { playerExperience = amount; }

    public void addExp(double amount) { playerExperience = playerExperience + amount; }

    public void setLevel(int amount) { playerLevel = amount; }

    public void addLevel(int amount) { playerLevel = playerLevel + amount; }

    public void setSkillPoints(int amount) { skillPoints = amount; }

    public void addSkillPoints(int amount) { skillPoints = skillPoints + amount; }

    public void addStr(int amount) {
        statStrength = statStrength + amount;
    }

    public void addAgi(int amount) {
        statAgility = statAgility + amount;
    }

    public void addVit(int amount) { statVitality = statVitality + amount; }

    public void addInt(int amount) { statIntelligence = statIntelligence + amount; }

    public double getCurrentHealth() { return (Values.HEATLH_PER_STR*statStrength) + 20; }

    public double getCurrentSpeed() { return (Values.SPEED_PER_AGI*statAgility) + 0.1; }

    public int getCurrentRegenHP() {
        return (Values.BASE_REGEN_TICKS) - (Values.REGEN_PER_VIT*statVitality);
    }

}
