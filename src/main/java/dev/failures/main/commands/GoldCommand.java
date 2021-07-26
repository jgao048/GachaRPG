package dev.failures.main.commands;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GoldCommand implements CommandExecutor {
    private GachaRPG main;
    private PlayerHandler ph;

    public GoldCommand(GachaRPG main, PlayerHandler ph) {
        this.main = main;
        this.ph = ph;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        double balance = ph.getOnlinePlayerSaves().get(p).getGold();
        p.sendMessage(MessageUtil.colorize("&#B0FF7CYou currently have " + balance + " Gold."));
        return false;
    }
}
