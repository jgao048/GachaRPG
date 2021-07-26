package dev.failures.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.failures.main.commands.StatsCommand;
import dev.failures.main.listeners.MobDeathEvent;
import dev.failures.main.listeners.CreateProfileEvent;
import dev.failures.main.storage.MongoDB;
import org.bukkit.plugin.java.JavaPlugin;

public final class GachaRPG extends JavaPlugin {
    public static GachaRPG instance;
    public static Gson gson;
    private MongoDB mongo;

    @Override
    public void onEnable() {
        mongo = new MongoDB();
        instance = this;
        gson = new GsonBuilder().create();

        registerCommands();
        registerListeners();
        getLogger().info("GachaRPG has been enabled.");

    }

    public static GachaRPG getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        getLogger().info("GachaRPG has been disabled.");
    }

    public void registerCommands() {
        getCommand("stats").setExecutor(new StatsCommand(this, mongo));
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new CreateProfileEvent(this, mongo), this);
        getServer().getPluginManager().registerEvents(new MobDeathEvent(this), this);
    }
}
