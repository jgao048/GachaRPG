package dev.failures.main;

import dev.failures.main.Commands.FlyCommand;
import dev.failures.main.Handlers.ConfigHandler;
import dev.failures.main.Listeners.MobDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class GachaRPG extends JavaPlugin {
    public static GachaRPG instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("GachaRPG has been enabled.");

        registerCommands();
        registerListeners();
    }

    public static GachaRPG getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        getLogger().info("GachaRPG has been disabled.");
    }

    public void registerCommands() {
        getCommand("fly").setExecutor(new FlyCommand(this));
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new MobDeathEvent(this), this);
    }
}
