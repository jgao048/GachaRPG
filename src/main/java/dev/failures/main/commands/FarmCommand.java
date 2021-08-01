package dev.failures.main.commands;

import dev.failures.main.storage.DataKeys;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FarmCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        ItemStack chest = new ItemStack(Material.CHEST);
        //CACTUS, SUGAR_CANE, WHEAT
        DataKeys.CHEST_TYPE.setItemDataString(chest, args[0].toUpperCase());
        p.getInventory().addItem(chest);
        return false;
    }
}
