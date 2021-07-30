package dev.failures.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.failures.main.armorequip.ArmorListener;
import dev.failures.main.commands.AdminCommand;
import dev.failures.main.commands.GoldCommand;
import dev.failures.main.commands.PartyCommand;
import dev.failures.main.commands.StatsCommand;
import dev.failures.main.handlers.CustomItemHandler;
import dev.failures.main.handlers.PartyHandler;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.listeners.*;
import dev.failures.main.storage.MongoDB;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
public final class GachaRPG extends JavaPlugin {
    public static GachaRPG instance;
    public static Gson gson;
    private MongoDB mongo;
    PlayerHandler playerHandler;

    @Override
    public void onEnable() {
        mongo = new MongoDB();
        instance = this;
        gson = new GsonBuilder().create();
        playerHandler = new PlayerHandler(mongo);
        PartyHandler partyHandler = new PartyHandler();
        CustomItemHandler.createRecipes();

        registerCommands(playerHandler, partyHandler);
        registerListeners(playerHandler, partyHandler);
        getLogger().info("GachaRPG has been enabled.");

    }

    public static GachaRPG getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        for(Player p: Bukkit.getOnlinePlayers()) {
            mongo.saveData(playerHandler.getOnlinePlayerSaves().remove(p), p.getUniqueId().toString());
        }
        getLogger().info("GachaRPG has been disabled.");
    }

    public void registerCommands(PlayerHandler playerHandler, PartyHandler partyHandler) {
        getCommand("stats").setExecutor(new StatsCommand(this, playerHandler));
        getCommand("gold").setExecutor(new GoldCommand(this, playerHandler));
        getCommand("party").setExecutor(new PartyCommand(this, playerHandler, partyHandler));
        getCommand("admin").setExecutor(new AdminCommand(playerHandler));
    }

    public void registerListeners(PlayerHandler playerHandler, PartyHandler partyHandler) {
        getServer().getPluginManager().registerEvents(new CraftingSystem(this, playerHandler), this);
        getServer().getPluginManager().registerEvents(new ArmorListener(new ArrayList<>()), this);
        getServer().getPluginManager().registerEvents(new ItemEquipSystem(this, playerHandler), this);
        getServer().getPluginManager().registerEvents(playerHandler, this);
        getServer().getPluginManager().registerEvents(new LevelSystem(this, playerHandler, partyHandler), this);
        getServer().getPluginManager().registerEvents(new CreateProfileEvent(this, mongo), this);
        getServer().getPluginManager().registerEvents(new PartyListeners(this, partyHandler), this);
    }
}
