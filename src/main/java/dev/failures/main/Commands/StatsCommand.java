package dev.failures.main.Commands;

import com.mongodb.client.MongoCollection;
import dev.failures.main.GachaRPG;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;

public class StatsCommand implements CommandExecutor {
    GachaRPG main;
    MongoCollection<Document> db;

    public StatsCommand(GachaRPG main, MongoCollection<Document> collection) {
        this.main = main;
        db = collection;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        Gui statsGUI = Gui.gui()
                .title(Component.text("&0Character Information"))
                .rows(6)
                .create();
        statsGUI.setDefaultClickAction(event -> event.setCancelled(true));
        statsGUI.open(p);
        return false;
    }
}
