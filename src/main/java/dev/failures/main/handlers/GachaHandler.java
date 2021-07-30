package dev.failures.main.handlers;

import dev.failures.main.listeners.CraftingSystem;
import dev.failures.main.storage.ArmorValues;
import dev.failures.main.storage.DataKeys;
import dev.failures.main.utils.PDUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class GachaHandler {
    public static List<ItemStack> level20 = Arrays.asList(
            createPerfectArmor(Material.DIAMOND_HELMET, 20),
            createPerfectArmor(Material.DIAMOND_CHESTPLATE, 20),
            createPerfectArmor(Material.DIAMOND_LEGGINGS, 20),
            createPerfectArmor(Material.DIAMOND_BOOTS, 20)
    );

    public static void randomizeRewards(Player p) {
        int index = (int)(Math.random() * level20.size());
        p.getInventory().addItem(level20.get(index));
    }


    private static ItemStack createPerfectArmor(Material material, int level) {
        ItemStack createdArmor = new ItemStack(material);

        int baseStr = ArmorValues.getArmorValues(createdArmor.getType()).getStr();
        int baseAgi = ArmorValues.getArmorValues(createdArmor.getType()).getAgi();
        int baseInt = ArmorValues.getArmorValues(createdArmor.getType()).getIntel();
        int baseVit = ArmorValues.getArmorValues(createdArmor.getType()).getVit();

        if(ArmorValues.getMainStat(createdArmor).equalsIgnoreCase("str")) baseStr += level-1;
        else if(ArmorValues.getMainStat(createdArmor).equalsIgnoreCase("agi")) baseAgi += level-1;
        else if(ArmorValues.getMainStat(createdArmor).equalsIgnoreCase("intel")) baseInt += level-1;
        else if(ArmorValues.getMainStat(createdArmor).equalsIgnoreCase("vit")) baseVit += level-1;

        DataKeys.setStrength(createdArmor, baseStr);
        DataKeys.setAgility(createdArmor, baseAgi);
        DataKeys.setIntel(createdArmor, baseInt);
        DataKeys.setVitality(createdArmor, baseVit);
        DataKeys.setLevel(createdArmor, level);

        CraftingSystem.updateItemLore(createdArmor, true);
        return createdArmor;
    }
}
