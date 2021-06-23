package dev.failures.main;

import dev.failures.main.Handlers.ConfigHandler;
import dev.failures.main.Listeners.MobDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class GachaRPG extends JavaPlugin {
    public static GachaRPG instance;
    public ConfigHandler playerFile;

    @Override
    public void onEnable() {
        instance = this;
        playerFile = new ConfigHandler(this, "stats.yml");
        getLogger().info("GachaRPG has been enabled.");

        getServer().getPluginManager().registerEvents(new MobDeathEvent(this), this);
    }

    public static GachaRPG getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        getLogger().info("GachaRPG has been disabled.");
    }
}
