package dev.failures.main.Commands;

import dev.failures.main.Storage.Permissions;
import dev.failures.main.Utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        if(!p.hasPermission(Permissions.GAMEMODE_PERMISSION)) {
            MessageUtil.sendNoPermission(p);
            return false;
        }



        return false;
    }
}
