package dev.failures.main.storage;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public enum WeaponValues {
    DIAMOND_SWORD(10, 1, 1),
    NETHER_SWORD(5, 1, 1),
    GOLDEN_SWORD(8, 1, 1),
    IRON_SWORD(9, 1, 1),
    WOODEN_SWORD(15, 2, 2);

    private static final List<Material> weapons = Arrays.asList(Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.WOODEN_SWORD);
    private final double damage;
    private final double criticalChance;
    private final double criticalMulti;

    WeaponValues(double damage, double criticalChance, double criticalMulti) {
        this.damage = damage;
        this.criticalChance = criticalChance;
        this.criticalMulti = criticalMulti;
    }

    public double getDamage() { return damage; }
    public double getCriticalChance() { return criticalChance; }
    public double getCriticalMulti() { return criticalMulti; }

    public WeaponValues getValues(ItemStack item) {
        if(item.getType().equals(Material.DIAMOND_SWORD)) return DIAMOND_SWORD;
        else if(item.getType().equals(Material.NETHERITE_SWORD)) return NETHER_SWORD;
        else if(item.getType().equals(Material.GOLDEN_SWORD)) return GOLDEN_SWORD;
        else if(item.getType().equals(Material.IRON_SWORD)) return IRON_SWORD;
        else if(item.getType().equals(Material.WOODEN_SWORD)) return WOODEN_SWORD;
        return null;
    }

}
