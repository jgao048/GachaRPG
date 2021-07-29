package dev.failures.main.storage;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public enum ArmorValues {
    DIAMOND_HELMET(3,1,1,1, 1),
    DIAMOND_CHEST(3,1,1,1, 3),
    DIAMOND_LEG(3,1,1,1, 1),
    DIAMOND_BOOT(3,1,1,1, 1),

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

    private int str;
    private int agi;
    private int inte;
    private int vit;
    private int level;

    ArmorValues(int str, int agi, int inte, int vit, int level) {
        this.str = str;
        this.agi = agi;
        this.inte = inte;
        this.vit = vit;
        this.level = level;
    }

    public static HashMap<String, Integer> getBaseStats(Material type) {
        if(type.equals(Material.DIAMOND_HELMET)) return get(DIAMOND_HELMET);
        if(type.equals(Material.DIAMOND_CHESTPLATE)) return get(DIAMOND_CHEST);
        if(type.equals(Material.DIAMOND_LEGGINGS)) return get(DIAMOND_LEG);
        if(type.equals(Material.DIAMOND_BOOTS)) return get(DIAMOND_BOOT);

        if(type.equals(Material.GOLDEN_HELMET)) return get(GOLD_HELMET);
        if(type.equals(Material.GOLDEN_CHESTPLATE)) return get(GOLD_CHEST);
        if(type.equals(Material.GOLDEN_LEGGINGS)) return get(GOLD_LEG);
        if(type.equals(Material.GOLDEN_BOOTS)) return get(GOLD_BOOT);

        if(type.equals(Material.IRON_HELMET)) return get(IRON_HELMET);
        if(type.equals(Material.IRON_CHESTPLATE)) return get(IRON_CHEST);
        if(type.equals(Material.IRON_LEGGINGS)) return get(IRON_LEG);
        if(type.equals(Material.IRON_BOOTS)) return get(IRON_BOOT);

        if(type.equals(Material.LEATHER_HELMET)) return get(LEATHER_HELMET);
        if(type.equals(Material.LEATHER_CHESTPLATE)) return get(LEATHER_CHEST);
        if(type.equals(Material.LEATHER_LEGGINGS)) return get(LEATHER_LEG);
        if(type.equals(Material.LEATHER_BOOTS)) return get(LEATHER_BOOT);

        return null;
    }

    public static HashMap<String, Integer> get(ArmorValues armor) {
        HashMap<String, Integer> values = new HashMap<>();
        values.put("str", armor.str);
        values.put("agi", armor.agi);
        values.put("inte", armor.inte);
        values.put("vit", armor.vit);
        values.put("level", armor.level);
        return values;
    }
}
