package dev.failures.main.handlers;

import dev.failures.main.GachaRPG;
import dev.failures.main.storage.GUIValues;
import dev.failures.main.storage.StatValues;
import dev.failures.main.utils.PDUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    public double getCurrentHealth() { return (StatValues.HEATLH_PER_STR*statStrength) + 20; }

    public double getCurrentSpeed() { return (StatValues.SPEED_PER_AGI*statAgility) + 0.1; }

    public int getCurrentRegenHP() {
        return (StatValues.BASE_REGEN_TICKS) - (StatValues.REGEN_PER_VIT*statVitality);
    }

    public void resetSkillPoints(Player p) {
        int refund = skillPoints;

        PDUtil itemStr = new PDUtil(new NamespacedKey(GachaRPG.getInstance(), "strength"));
        PDUtil itemAgi = new PDUtil(new NamespacedKey(GachaRPG.getInstance(), "agility"));
        PDUtil itemInt = new PDUtil(new NamespacedKey(GachaRPG.getInstance(), "intelligence"));
        PDUtil itemVit = new PDUtil(new NamespacedKey(GachaRPG.getInstance(), "vitality"));

        int strHas = 10;
        int agiHas = 10;
        int intHas = 10;
        int vitHas = 10;

        for(ItemStack armor: p.getInventory().getArmorContents()) {
            if(armor == null) continue;
            strHas += itemStr.getItemDataInteger(armor);
            agiHas += itemAgi.getItemDataInteger(armor);
            intHas += itemInt.getItemDataInteger(armor);
            vitHas += itemVit.getItemDataInteger(armor);
        }

        refund += statStrength - strHas;
        statStrength = strHas;

        refund += statAgility - agiHas;
        statAgility = agiHas;

        refund += statIntelligence - intHas;
        statIntelligence = intHas;

        refund += statVitality - vitHas;
        statVitality = vitHas;

        setSkillPoints(refund);
    }

}
