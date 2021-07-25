package dev.failures.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.failures.main.Commands.FlyCommand;
import dev.failures.main.Listeners.MobDeathEvent;
import dev.failures.main.Listeners.CreateProfileEvent;
import dev.failures.main.Storage.MongoDB;
import org.bson.Document;
import org.bukkit.plugin.java.JavaPlugin;

public final class GachaRPG extends JavaPlugin {
    public static GachaRPG instance;
    public static Gson gson;
    private MongoDB mongo;

    @Override
    public void onEnable() {
        mongo = new MongoDB();
        registerCommands();
        registerListeners();

        instance = this;
        gson = new GsonBuilder().create();
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
        getCommand("fly").setExecutor(new FlyCommand(this));
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new CreateProfileEvent(this, mongo.getCollection()), this);
        getServer().getPluginManager().registerEvents(new MobDeathEvent(this), this);
    }
}
