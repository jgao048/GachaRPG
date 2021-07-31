package dev.failures.main.commands;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.GachaHandler;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GoldCommand implements CommandExecutor {
    private GachaRPG main;
    private PlayerHandler playerHandler;

    public GoldCommand(GachaRPG main, PlayerHandler ph) {
        this.main = main;
        this.playerHandler = ph;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        double balance = playerHandler.getOnlinePlayerSaves().get(p).getGold();
        p.sendMessage(ChatUtil.colorize("&#B0FF7CYou currently have " + balance + " Gold."));

        playerHandler.getOnlinePlayerSaves().get(p).addStr(10);
        GachaHandler.randomizeRewards(p);
        return false;
    }
}
