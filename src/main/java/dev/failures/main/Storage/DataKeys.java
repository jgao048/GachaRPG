package dev.failures.main.Storage;

import dev.failures.main.GachaRPG;
import org.bukkit.NamespacedKey;

public class DataKeys {
    public static NamespacedKey PLAYER_GOLD = new NamespacedKey(GachaRPG.getInstance(), "playerGold");
    public static NamespacedKey PLAYER_LEVEL = new NamespacedKey(GachaRPG.getInstance(), "playerLevel");
    public static NamespacedKey PLAYER_STRENGTH = new NamespacedKey(GachaRPG.getInstance(), "playerStrength");
    public static NamespacedKey PLAYER_EXPERIENCE = new NamespacedKey(GachaRPG.getInstance(), "playerExperience");
}
