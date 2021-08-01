package dev.failures.main.commands;

import dev.failures.main.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class LoreCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        if(p.getInventory().getItemInMainHand().getType() == Material.AIR) return false;
        if(args.length < 1) return false;
        ItemStack itemHand = p.getInventory().getItemInMainHand();
        StringBuilder input = new StringBuilder(args[0]);
        for(int i = 1 ; i < args.length ;i++) {
            input.append(" ").append(args[i]);
        }
        List<String> lore = Arrays.asList(input.toString().split(","));
        ItemMeta im = itemHand.getItemMeta();
        im.lore(ChatUtil.getListComponent(lore));

        p.getInventory().getItemInMainHand().setItemMeta(im);
        return false;
    }
}
