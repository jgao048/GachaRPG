package dev.failures.main.storage;

import com.destroystokyo.paper.Namespaced;
import dev.failures.main.GachaRPG;
import dev.failures.main.utils.PDUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class DataKeys {

    public static PDUtil CHEST_TYPE = new PDUtil(new NamespacedKey(GachaRPG.getInstance(), "collectorType"));
    public static PDUtil CHUNK_LOCATION = new PDUtil(new NamespacedKey(GachaRPG.getInstance(), "chunkLocation"));
    public static PDUtil CHUNK_TYPE = new PDUtil(new NamespacedKey(GachaRPG.getInstance(), "chunkType"));

    public static PDUtil BUCKET_TYPE = new PDUtil(new NamespacedKey(GachaRPG.getInstance(), "bucketType"));

    public static PDUtil STRENGTH = new PDUtil(new NamespacedKey(GachaRPG.getInstance(), "strength"));
    public static PDUtil AGILITY = new PDUtil(new NamespacedKey(GachaRPG.getInstance(), "agility"));
    public static PDUtil INTELLIGENCE = new PDUtil(new NamespacedKey(GachaRPG.getInstance(), "intelligence"));
    public static PDUtil VITALITY = new PDUtil(new NamespacedKey(GachaRPG.getInstance(), "vitality"));
    public static PDUtil LEVEL = new PDUtil(new NamespacedKey(GachaRPG.getInstance() , "level"));

    public static int getStrength(ItemStack item) {
        return STRENGTH.getItemDataInteger(item);
    }

    public static int getAgility(ItemStack item) {
        return AGILITY.getItemDataInteger(item);
    }

    public static int getIntel(ItemStack item) {
        return INTELLIGENCE.getItemDataInteger(item);
    }

    public static int getVitality(ItemStack item) {
        return VITALITY.getItemDataInteger(item);
    }

    public static int getLevel(ItemStack item) {
        return LEVEL.getItemDataInteger(item);
    }

    public static void setStrength(ItemStack item, int amount) {
        STRENGTH.setItemDataInteger(item, amount);
    }

    public static void setAgility(ItemStack item, int amount) {
        AGILITY.setItemDataInteger(item, amount);
    }

    public static void setIntel(ItemStack item, int amount) {
        INTELLIGENCE.setItemDataInteger(item, amount);
    }

    public static void setVitality(ItemStack item, int amount) {
        VITALITY.setItemDataInteger(item, amount);
    }

    public static void setLevel(ItemStack item, int amount) {
        LEVEL.setItemDataInteger(item, amount);
    }

    public static boolean hasStrength(ItemStack item) {
        return STRENGTH.itemDataContainsIntegerKey(item);
    }

}
