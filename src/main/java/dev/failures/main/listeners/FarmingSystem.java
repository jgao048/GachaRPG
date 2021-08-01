package dev.failures.main.listeners;

import dev.failures.main.GachaRPG;
import dev.failures.main.storage.DataKeys;
import dev.failures.main.utils.ChatUtil;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class FarmingSystem implements Listener {
    @EventHandler
    private void cactusFarming(BlockGrowEvent e) {
        if(DataKeys.CHUNK_LOCATION.getChunkDataString(e.getBlock().getChunk()).equals("")) return;
        String collectorType = DataKeys.CHUNK_TYPE.getChunkDataString(e.getBlock().getChunk());

        ArrayList<Material> typeCheckList = new ArrayList<>();
        String[] tryTypes = collectorType.split(",");
        for(String tryType: tryTypes) {
            typeCheckList.add(Material.valueOf(tryType));
        }

        if(typeCheckList.contains(e.getNewState().getType())) {
            List<String> values = Arrays.asList(DataKeys.CHUNK_LOCATION.getChunkDataString(e.getBlock().getChunk()).split(","));
            World world = Bukkit.getWorld(values.get(0));
            Location loc = new Location(Bukkit.getWorld(values.get(0)), Double.parseDouble(values.get(1)), Double.parseDouble(values.get(2)), Double.parseDouble(values.get(3)));
            Block block = world.getBlockAt(loc);
            Chest chest = (Chest) block.getState();

            if(e.getNewState().getType().equals(Material.WHEAT)) {
                Ageable age = (Ageable) e.getNewState().getBlockData();
                if(age.getAge() != age.getMaximumAge()) return;
                chest.getBlockInventory().addItem(new ItemStack(e.getNewState().getType()));
                age.setAge(0);
                e.getNewState().setBlockData(age);
            } else {
                chest.getBlockInventory().addItem(new ItemStack(e.getNewState().getType()));
                e.getNewState().setType(Material.AIR);
            }
        }
    }

    @EventHandler
    private void collectorPlace(BlockPlaceEvent e) {
        if(!DataKeys.CHEST_TYPE.itemDataContainsStringKey(e.getItemInHand())) return;

        if(DataKeys.CHUNK_LOCATION.getChunkDataString(e.getBlock().getChunk()).equals("")) {
            String location = e.getBlock().getLocation().getWorld().getName();
            location += "," + e.getBlock().getLocation().getX();
            location += "," + e.getBlock().getLocation().getY();
            location += "," + e.getBlock().getLocation().getZ();
            DataKeys.CHUNK_LOCATION.setChunkDataString(e.getBlock().getChunk(), location);

            String chestType = DataKeys.CHEST_TYPE.getItemDataString(e.getItemInHand());
            DataKeys.CHUNK_TYPE.setChunkDataString(e.getBlock().getChunk(), chestType);
            Chest chest = (Chest) e.getBlock().getState();
        } else if(DataKeys.CHEST_TYPE.getItemDataString(e.getItemInHand()).equals(DataKeys.CHUNK_TYPE.getChunkDataString(e.getBlock().getChunk()))) {
            Bukkit.getScheduler().runTaskLater(GachaRPG.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Block chestblock = e.getBlock();
                    Chest chest = (Chest) chestblock.getState();
                    Inventory inv = chest.getInventory();
                    if(inv instanceof DoubleChestInventory) {
                        e.getPlayer().sendMessage(ChatUtil.colorize("&7You have combined two collectors."));
                    } else{
                        e.getPlayer().sendMessage(ChatUtil.colorize("&7You already have a collector in this chunk."));

                        ItemStack item = new ItemStack(Material.CHEST);
                        DataKeys.CHEST_TYPE.setItemDataString(item, DataKeys.CHUNK_TYPE.getChunkDataString(e.getBlock().getChunk()));
                        e.getPlayer().getInventory().addItem(item);
                        e.getBlock().setType(Material.AIR);
                    }
                }
            },1);
        } else {
            e.getPlayer().sendMessage(ChatUtil.colorize("&7You already have a collector in this chunk."));
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void collectorDestroy(BlockBreakEvent e) {
        if(DataKeys.CHUNK_LOCATION.getChunkDataString(e.getBlock().getChunk()).equals("")) return;

        List<String> values = Arrays.asList(DataKeys.CHUNK_LOCATION.getChunkDataString(e.getBlock().getChunk()).split(","));
        Location loc = new Location(Bukkit.getWorld(values.get(0)), Double.parseDouble(values.get(1)), Double.parseDouble(values.get(2)), Double.parseDouble(values.get(3)));
        if(e.getBlock().getLocation().equals(loc)) {
            e.setDropItems(false);
            ItemStack chestCollector = new ItemStack(Material.CHEST);
            String chestType = DataKeys.CHUNK_TYPE.getChunkDataString(e.getBlock().getChunk());
            DataKeys.CHEST_TYPE.setItemDataString(chestCollector, chestType);
            loc.getWorld().dropItemNaturally(loc, chestCollector);
            DataKeys.CHUNK_LOCATION.setChunkDataString(e.getBlock().getChunk(), "");
            DataKeys.CHUNK_TYPE.setChunkDataString(e.getBlock().getChunk(), "");
            return;
        }

        Location chestLoc = e.getBlock().getLocation();
        Location loc1 = chestLoc.add(0,0,1);
        Location loc2 = chestLoc.add(0,0,-1);
        Location loc3 = chestLoc.add(1,0,0);
        Location loc4 = chestLoc.add(-1,0,0);

        if(loc1 == loc || loc2 == loc || loc3 == loc || loc4 == loc) {
            e.setDropItems(false);
            ItemStack chestCollector = new ItemStack(Material.CHEST);
            String chestType = DataKeys.CHUNK_TYPE.getChunkDataString(e.getBlock().getChunk());
            GachaRPG.getInstance().getLogger().info("Test: "+  chestType);
            DataKeys.CHEST_TYPE.setItemDataString(chestCollector, chestType);
            chestLoc.getWorld().dropItemNaturally(loc, chestCollector);
        }

    }

}
