package dev.failures.main.storage;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
        values.put("intel", armor.intel);
        values.put("vit", armor.vit);
        values.put("level", armor.level);
        return values;
    }

    public static String getMainStat(ItemStack item) {
        if(item.getType().toString().contains("DIAMOND")) return "str";
        else if(item.getType().toString().contains("GOLDEN")) return "vit";
        else if(item.getType().toString().contains("IRON")) return "agi";
        else if(item.getType().toString().contains("LEATHER")) return "intel";
        return "none";
    }

    public static List<Material> getArmors() {
        return armors;
    }
}
