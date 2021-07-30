package dev.failures.main.storage;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum ArmorValues {
    DIAMOND_HELMET(3,-2,-2,2, 1),
    DIAMOND_CHEST(3,-2,-2,2, 1),
    DIAMOND_LEG(3,-2,-2,2, 1),
    DIAMOND_BOOT(3,-2,-2,2, 1),

    GOLD_HELMET(1,1,1,3, 1),
    GOLD_CHEST(1,1,1,3, 1),
    GOLD_LEG(1,1,1,3, 1),
    GOLD_BOOT(1,1,1,3, 1),

    IRON_HELMET(1,3,1,1, 1),
    IRON_CHEST(1,3,1,1, 1),
    IRON_LEG(1,3,1,1, 1),
    IRON_BOOT(1,3,1,1, 1),

    LEATHER_HELMET(1,1,3,1, 1),
    LEATHER_CHEST(1,1,3,1, 1),
    LEATHER_LEG(1,1,3,1, 1),
    LEATHER_BOOT(1,1,3,1, 1);

    private static final List<Material> armors = Arrays.asList(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS);
    private final int str;
    private final int agi;
    private final int intel;
    private final int vit;
    private final int level;

    ArmorValues(int str, int agi, int intel, int vit, int level) {
        this.str = str;
        this.agi = agi;
        this.intel = intel;
        this.vit = vit;
        this.level = level;
    }

    public static ArmorValues getArmorValues(Material type) {
        if(type.equals(Material.DIAMOND_HELMET)) return DIAMOND_HELMET;
        if(type.equals(Material.DIAMOND_CHESTPLATE)) return DIAMOND_CHEST;
        if(type.equals(Material.DIAMOND_LEGGINGS)) return DIAMOND_LEG;
        if(type.equals(Material.DIAMOND_BOOTS)) return DIAMOND_BOOT;

        if(type.equals(Material.GOLDEN_HELMET)) return GOLD_HELMET;
        if(type.equals(Material.GOLDEN_CHESTPLATE)) return GOLD_CHEST;
        if(type.equals(Material.GOLDEN_LEGGINGS)) return GOLD_LEG;
        if(type.equals(Material.GOLDEN_BOOTS)) return GOLD_BOOT;

        if(type.equals(Material.IRON_HELMET)) return IRON_HELMET;
        if(type.equals(Material.IRON_CHESTPLATE)) return IRON_CHEST;
        if(type.equals(Material.IRON_LEGGINGS)) return IRON_LEG;
        if(type.equals(Material.IRON_BOOTS)) return IRON_BOOT;

        if(type.equals(Material.LEATHER_HELMET)) return LEATHER_HELMET;
        if(type.equals(Material.LEATHER_CHESTPLATE)) return LEATHER_CHEST;
        if(type.equals(Material.LEATHER_LEGGINGS)) return LEATHER_LEG;
        if(type.equals(Material.LEATHER_BOOTS)) return LEATHER_BOOT;
        return null;
    }

    public int getStr() { return str; }
    public int getAgi() { return agi; }
    public int getIntel() { return agi; }
    public int getVit() { return vit; }
    public int getLevel() { return level; }

    public static String getMainStat(ItemStack item) {
        if(item.getType().toString().contains("DIAMOND")) return "str";
        else if(item.getType().toString().contains("GOLDEN")) return "vit";
        else if(item.getType().toString().contains("IRON")) return "agi";
        else if(item.getType().toString().contains("LEATHER")) return "intel";
        return "str";
    }

    public static String getRandomOffStat(ItemStack item) {
        ArrayList<String> offStat = new ArrayList<>();
        offStat.add("str"); offStat.add("agi"); offStat.add("intel"); offStat.add("vit");
        if(getMainStat(item).equalsIgnoreCase("str")) offStat.remove(0);
        else if(getMainStat(item).equalsIgnoreCase("agi")) offStat.remove(1);
        else if(getMainStat(item).equalsIgnoreCase("intel")) offStat.remove(2);
        else offStat.remove(3);
        int index = (int)(Math.random() * offStat.size());
        return offStat.remove(index);
    }

    public static void addMainStat(ItemStack item, int amount) {
        if(getMainStat(item).equalsIgnoreCase("str")) DataKeys.setStrength(item, DataKeys.getStrength(item) + 1);
        else if(getMainStat(item).equalsIgnoreCase("agi")) DataKeys.setAgility(item, DataKeys.getAgility(item) + 1);
        else if(getMainStat(item).equalsIgnoreCase("intel")) DataKeys.setIntel(item, DataKeys.getIntel(item) + 1);
        else DataKeys.setVitality(item, DataKeys.getVitality(item) + 1);
    }

    public static void addOffStat(ItemStack item, int amount) {
        if(getRandomOffStat(item).equalsIgnoreCase("str")) DataKeys.setStrength(item, DataKeys.getStrength(item) + 1);
        else if(getRandomOffStat(item).equalsIgnoreCase("agi")) DataKeys.setAgility(item, DataKeys.getAgility(item) + 1);
        else if(getRandomOffStat(item).equalsIgnoreCase("intel")) DataKeys.setIntel(item, DataKeys.getIntel(item) + 1);
        else DataKeys.setVitality(item, DataKeys.getVitality(item) + 1);
    }

    public static List<Material> getArmors() {
        return armors;
    }
}
