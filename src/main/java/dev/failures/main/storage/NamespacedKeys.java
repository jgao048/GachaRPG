package dev.failures.main.storage;

import com.destroystokyo.paper.Namespaced;
import dev.failures.main.GachaRPG;
import org.bukkit.NamespacedKey;

public class NamespacedKeys {
    public static NamespacedKey STRENGTH = new NamespacedKey(GachaRPG.getInstance(), "strength");
    public static NamespacedKey AGILITY = new NamespacedKey(GachaRPG.getInstance(), "agility");
    public static NamespacedKey INTELLIGENCE = new NamespacedKey(GachaRPG.getInstance(), "intelligence");
    public static NamespacedKey VITALITY = new NamespacedKey(GachaRPG.getInstance(), "vitality");
    public static NamespacedKey LEVEL = new NamespacedKey(GachaRPG.getInstance() , "level");
}
