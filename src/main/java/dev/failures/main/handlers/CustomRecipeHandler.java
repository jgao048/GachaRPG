package dev.failures.main.handlers;

import dev.failures.main.GachaRPG;
import dev.failures.main.utils.PDUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Arrays;
import java.util.List;

public class CustomRecipeHandler {
    private static List<Material> armors = Arrays.asList(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS);

    public static void createRecipes() {
        for(Material armor : armors) {
            ItemStack customArmor = new ItemStack(armor);

            ShapedRecipe armorRecipe = new ShapedRecipe(NamespacedKey.minecraft("" + customArmor.getType().toString().replace("_","").toLowerCase()), customArmor);
            armorRecipe.shape("DDD", "DAD", "DDD");
            armorRecipe.setIngredient('D', Material.DIAMOND);
            armorRecipe.setIngredient('A', customArmor.getType());
            Bukkit.getServer().addRecipe(armorRecipe);
        }

    }
}
