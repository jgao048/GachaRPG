package dev.failures.main.listeners;

import dev.failures.main.GachaRPG;
import dev.failures.main.storage.DataKeys;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BucketListener implements Listener {

    @EventHandler
    private void genBucketUsage(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(p.getInventory().getItemInMainHand().getType() == Material.AIR) return;
        ItemStack itemHand = p.getInventory().getItemInMainHand();

        if(!DataKeys.BUCKET_TYPE.itemDataContainsStringKey(itemHand)) return;
        if(getBlockFace(p) == null) return;

        e.setCancelled(true);

        String direction = getBlockFace(p).toString();
        String bucketType = DataKeys.BUCKET_TYPE.getItemDataString(itemHand);
        Material bucketMat = Material.valueOf(bucketType.toUpperCase());

        double x = 0; double y = 0; double z = 0; boolean notUD = true;
        if(direction.equals("SOUTH")) z = 1;
        else if(direction.equals("NORTH")) z = -1;
        else if(direction.equals("WEST")) x = -1;
        else if(direction.equals("EAST")) x = 1;
        else if(direction.equals("UP")) y = 1;
        else y = -1;

        if(direction.equals("UP")) notUD = false;
        else if(direction.equals("DOWN")) notUD = false;

        Location startPosition = e.getClickedBlock().getLocation();
        double finalX = x; double finalY = y; double finalZ = z; boolean finalUD = notUD;
        final int[] counter = {0};

        new BukkitRunnable() {
            @Override
            public void run() {
                int maxHeight = p.getWorld().getMaxHeight();
                Location newPosition = startPosition.add(finalX, finalY, finalZ);
                if(newPosition.getY() == maxHeight || !newPosition.getBlock().getType().equals(Material.AIR)) {
                    cancel();
                } else if(counter[0] == 20 && finalUD) {
                    cancel();
                } else {
                    newPosition.getBlock().setType(bucketMat);
                    counter[0]++;
                }
            }
        }.runTaskTimer(GachaRPG.getInstance(), 0, 20);

    }

    public BlockFace getBlockFace(Player player) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
        if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return null;
        Block targetBlock = lastTwoTargetBlocks.get(1);
        Block adjacentBlock = lastTwoTargetBlocks.get(0);
        return targetBlock.getFace(adjacentBlock);
    }

}
