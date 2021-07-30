package dev.failures.main.handlers;

import dev.failures.main.GachaRPG;
import dev.failures.main.storage.ArmorValues;
import dev.failures.main.utils.PDUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Arrays;
import java.util.List;

public class CustomItemHandler {
    public static void createRecipes() {
        for(Material armor : ArmorValues.getArmors()) {
            ItemStack customArmor = new ItemStack(armor);

            ShapedRecipe armorRecipe = new ShapedRecipe(NamespacedKey.minecraft("" + customArmor.getType().toString().replace("_","").toLowerCase()), customArmor);
            armorRecipe.shape("DDD", "DAD", "DDD");
            Material customMaterial = Material.DIAMOND;
            if(armor.toString().contains("GOLDEN")) customMaterial = Material.GOLD_INGOT;
            else if(armor.toString().contains("LEATHER")) customMaterial = Material.LEATHER;
            else if(armor.toString().contains("IRON")) customMaterial = Material.IRON_INGOT;
            armorRecipe.setIngredient('D', customMaterial);
            armorRecipe.setIngredient('A', customArmor.getType());
            Bukkit.getServer().addRecipe(armorRecipe);
        }

    }
}
