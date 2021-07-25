package dev.failures.main;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.failures.main.Commands.FlyCommand;
import dev.failures.main.Listeners.MobDeathEvent;
import dev.failures.main.Listeners.CreateProfileEvent;
import org.bson.Document;
import org.bukkit.plugin.java.JavaPlugin;

public final class GachaRPG extends JavaPlugin {
    public static GachaRPG instance;
    public static MongoCollection<Document> col;

    @Override
    public void onEnable() {
        MongoClient mongoClient = MongoClients.create("mongodb+srv://admin:gacharpg123@gacharpg.r4lca.mongodb.net/gacharpg?retryWrites=true&w=majority");
        MongoDatabase database = mongoClient.getDatabase("gacharpg");
        col = database.getCollection("playerdata");

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
        getServer().getPluginManager().registerEvents(new CreateProfileEvent(this), this);
        getServer().getPluginManager().registerEvents(new MobDeathEvent(this), this);
    }
}
