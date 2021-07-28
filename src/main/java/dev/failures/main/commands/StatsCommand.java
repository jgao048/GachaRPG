package dev.failures.main.commands;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PlayerData;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.utils.ChatUtil;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {
    GachaRPG main;
    PlayerHandler ph;

    public StatsCommand(GachaRPG main, PlayerHandler ph) {
        this.main = main;
        this.ph = ph;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        PlayerData data = ph.getOnlinePlayerSaves().get(p);

        Gui statsGUI = Gui.gui()
                .title(Component.text(ChatUtil.colorize("&0Character Information")))
                .rows(6)
                .create();
        statsGUI.setDefaultClickAction(event -> event.setCancelled(true));
        statsGUI.addItem();
        statsGUI.open(p);
        return false;
    }
}
