package dev.failures.main.handlers;

public class PlayerData {
    int playerLevel;
    double playerExperience;
    double playerGold;
    int skillPoints;

    public PlayerData(int level, double exp, double gold, int sp) {
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

    public void setExp(double amount) { playerExperience = amount; }

    public void addExp(double amount) { playerExperience = playerExperience + amount; }

    public void setLevel(int amount) { playerLevel = amount; }

    public void addLevel(int amount) { playerLevel = playerLevel + amount; }

    public void setSkillPoints(int amount) { skillPoints = amount; }

    public void addSkillPoints(int amount) { skillPoints = skillPoints + amount; }
}
