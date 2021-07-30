package dev.failures.main.commands;

import dev.failures.main.handlers.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdminCommand implements CommandExecutor, TabCompleter {
    PlayerHandler playerHandler;

    public AdminCommand(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        if(!p.hasPermission("*")) return false;
        if(args.length < 4) return false;

        if (Bukkit.getPlayer(args[1]) == null) return false;
        Player target = Bukkit.getPlayer(args[1]);
        int amount = Integer.parseInt(args[3]);

        if(args[0].equalsIgnoreCase("set")) {
            if(args[2].equalsIgnoreCase("level")) playerHandler.getOnlinePlayerSaves().get(target).setLevel(amount);
            else if(args[2].equalsIgnoreCase("str")) playerHandler.getOnlinePlayerSaves().get(target).setStr(amount);
            else if(args[2].equalsIgnoreCase("agi")) playerHandler.getOnlinePlayerSaves().get(target).setAgi(amount);
            else if(args[2].equalsIgnoreCase("intel")) playerHandler.getOnlinePlayerSaves().get(target).setInt(amount);
            else if(args[2].equalsIgnoreCase("vit")) playerHandler.getOnlinePlayerSaves().get(target).setVit(amount);
            else if(args[2].equals("sp")) playerHandler.getOnlinePlayerSaves().get(target).setSkillPoints(amount);
        } else if(args[0].equalsIgnoreCase("add")) {
            if(args[2].equalsIgnoreCase("level")) playerHandler.getOnlinePlayerSaves().get(target).addLevel(amount);
            else if(args[2].equalsIgnoreCase("str")) playerHandler.getOnlinePlayerSaves().get(target).addStr(amount);
            else if(args[2].equalsIgnoreCase("agi")) playerHandler.getOnlinePlayerSaves().get(target).addAgi(amount);
            else if(args[2].equalsIgnoreCase("intel")) playerHandler.getOnlinePlayerSaves().get(target).addInt(amount);
            else if(args[2].equalsIgnoreCase("vit")) playerHandler.getOnlinePlayerSaves().get(target).addVit(amount);
            else if(args[2].equals("sp")) playerHandler.getOnlinePlayerSaves().get(target).addSkillPoints(amount);
        }

        playerHandler.updatePlayerExp(target);
        playerHandler.updatePlayerStats(target);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> auto = new ArrayList<>();
        if(!(sender instanceof Player)) return auto;
        Player p = (Player) sender;
        if(!command.getName().equalsIgnoreCase("admin")) return auto;
        if(args.length == 1) {
            auto.clear();
            auto.add("set");
            auto.add("add");
            return auto;
        } else if(args.length == 2) {
            auto.clear();
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for(int i = 0 ; i < players.length ; i++) {
                auto.add(players[i].getName());
            }
            return auto;
        } else if(args.length == 3) {
            auto.clear();
            auto.add("str");
            auto.add("intel");
            auto.add("agi");
            auto.add("vit");
            auto.add("sp");
            auto.add("level");
            return auto;
        } else if(args.length == 4) {
            auto.clear();
            for(int i = 0 ; i < 100 ; i++) {
                auto.add("" + i);
            }
            return auto;
        }
        return auto;
    }
}
