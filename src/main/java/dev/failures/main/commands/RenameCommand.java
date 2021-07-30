package dev.failures.main.commands;

import dev.failures.main.utils.ChatUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class RenameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        if(p.getInventory().getItemInMainHand().getType() == Material.AIR) return false;
        if(args.length < 1) return false;
        StringBuilder itemName = new StringBuilder(args[0]);
        for(int i = 1 ; i < args.length ; i++) {
            itemName.append(" " +args[i]);
        }
        ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
        im.displayName(Component.text(ChatUtil.colorize(itemName.toString())));
        p.getInventory().getItemInMainHand().setItemMeta(im);
        return false;
    }
}
