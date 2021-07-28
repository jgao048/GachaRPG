package dev.failures.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.failures.main.commands.GoldCommand;
import dev.failures.main.commands.PartyCommand;
import dev.failures.main.commands.StatsCommand;
import dev.failures.main.handlers.PartyHandler;
import dev.failures.main.handlers.PlayerData;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.listeners.CreateProfileEvent;
import dev.failures.main.listeners.LevelSystem;
import dev.failures.main.listeners.PartyListeners;
import dev.failures.main.storage.MongoDB;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class GachaRPG extends JavaPlugin {
    public static GachaRPG instance;
    public static Gson gson;
    private MongoDB mongo;
    public static HashMap<Player, PlayerData> onlinePlayers;

    @Override
    public void onEnable() {
        mongo = new MongoDB();
        instance = this;
        gson = new GsonBuilder().create();
        PlayerHandler ph = new PlayerHandler(mongo);
        PartyHandler partyHandler = new PartyHandler();


        registerCommands(ph, partyHandler);
        registerListeners(ph, partyHandler);
        getLogger().info("GachaRPG has been enabled.");

    }

    public static GachaRPG getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        getLogger().info("GachaRPG has been disabled.");
    }

    public void registerCommands(PlayerHandler ph, PartyHandler partyHandler) {
        getCommand("stats").setExecutor(new StatsCommand(this, ph));
        getCommand("gold").setExecutor(new GoldCommand(this, ph));
        getCommand("party").setExecutor(new PartyCommand(this, ph, partyHandler));
    }

    public void registerListeners(PlayerHandler ph, PartyHandler partyHandler) {
        getServer().getPluginManager().registerEvents(ph, this);
        getServer().getPluginManager().registerEvents(new LevelSystem(this, ph, partyHandler), this);
        getServer().getPluginManager().registerEvents(new CreateProfileEvent(this, mongo), this);
        getServer().getPluginManager().registerEvents(new PartyListeners(this, partyHandler), this);
    }
}
