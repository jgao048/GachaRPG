package dev.failures.main.commands;

import com.mongodb.client.MongoCollection;
import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.storage.MongoDB;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {
    GachaRPG main;
    MongoDB db;

    public StatsCommand(GachaRPG main, MongoDB db) {
        this.main = main;
        this.db = db;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        PlayerHandler data = db.getData(p);

        Gui statsGUI = Gui.gui()
                .title(Component.text("&0Character Information"))
                .rows(6)
                .create();
        statsGUI.setDefaultClickAction(event -> event.setCancelled(true));
        statsGUI.open(p);
        return false;
    }
}
