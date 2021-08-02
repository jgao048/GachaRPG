package dev.failures.main.commands;

import dev.failures.main.storage.DataKeys;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GenbucketCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        ItemStack genBucket = new ItemStack(Material.LAVA_BUCKET);
        DataKeys.BUCKET_TYPE.setItemDataString(genBucket, args[0]);

        p.getInventory().addItem(genBucket);
        return false;
    }
}
