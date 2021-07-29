package dev.failures.main.handlers;

import dev.failures.main.listeners.CraftingSystem;
import dev.failures.main.storage.ArmorValues;
import dev.failures.main.storage.NamespacedKeys;
import dev.failures.main.utils.PDUtil;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
        PDUtil itemStr = new PDUtil(NamespacedKeys.STRENGTH);
        PDUtil itemAgi = new PDUtil(NamespacedKeys.AGILITY);
        PDUtil itemInt = new PDUtil(NamespacedKeys.INTELLIGENCE);
        PDUtil itemVit = new PDUtil(NamespacedKeys.VITALITY);
        PDUtil itemLevel = new PDUtil(NamespacedKeys.LEVEL);

        int str = ArmorValues.getBaseStats(createdArmor.getType()).get("str");
        int agi = ArmorValues.getBaseStats(createdArmor.getType()).get("agi");
        int intel = ArmorValues.getBaseStats(createdArmor.getType()).get("inte");
        int vit = ArmorValues.getBaseStats(createdArmor.getType()).get("vit");


        if(ArmorValues.getMainStat(createdArmor) == "str") str = str + (level-1);
        else if(ArmorValues.getMainStat(createdArmor) == "agi") agi = agi + (level-1);
        else if(ArmorValues.getMainStat(createdArmor) == "intel") intel = intel + (level-1);
        else if(ArmorValues.getMainStat(createdArmor) == "vit") vit = vit + (level-1);

        itemStr.setItemDataInteger(createdArmor, str);
        itemAgi.setItemDataInteger(createdArmor, agi);
        itemInt.setItemDataInteger(createdArmor, intel);
        itemVit.setItemDataInteger(createdArmor, vit);
        itemLevel.setItemDataInteger(createdArmor, level);

        ItemMeta im = createdArmor.getItemMeta();

        CraftingSystem.updateItemLore(im, level, str, agi, intel, vit, true);
        createdArmor.setItemMeta(im);
        return createdArmor;
    }
}
