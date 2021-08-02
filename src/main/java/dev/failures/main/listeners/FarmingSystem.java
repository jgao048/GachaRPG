package dev.failures.main.listeners;

import dev.failures.main.GachaRPG;
import dev.failures.main.storage.DataKeys;
import dev.failures.main.utils.ChatUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        if(!e.getBlock().getType().equals(Material.CHEST)) return;
        //if(!DataKeys.CHEST_TYPE.itemDataContainsStringKey(e.getItemInHand())) return;

        if(DataKeys.CHUNK_LOCATION.getChunkDataString(e.getBlock().getChunk()).equals("")) {
            e.getPlayer().sendMessage("Chunk collector in new chunk!");
            String location = e.getBlock().getLocation().getWorld().getName();
            location += "," + e.getBlock().getLocation().getX();
            location += "," + e.getBlock().getLocation().getY();
            location += "," + e.getBlock().getLocation().getZ();
            DataKeys.CHUNK_LOCATION.setChunkDataString(e.getBlock().getChunk(), location);

            String chestType = DataKeys.CHEST_TYPE.getItemDataString(e.getItemInHand());
            DataKeys.CHUNK_TYPE.setChunkDataString(e.getBlock().getChunk(), chestType);
            Chest chest = (Chest) e.getBlock().getState();
        } else {
            e.getPlayer().sendMessage("Chunk collector already exists, checking!");
            List<String> values = Arrays.asList(DataKeys.CHUNK_LOCATION.getChunkDataString(e.getBlock().getChunk()).split(","));
            Location loc = new Location(Bukkit.getWorld(values.get(0)), Double.parseDouble(values.get(1)), Double.parseDouble(values.get(2)), Double.parseDouble(values.get(3)));

            Location chestLoc = e.getBlock().getLocation();
            Location loc1 = chestLoc.clone().add(0,0,1);
            Location loc2 = chestLoc.clone().add(0,0,-1);
            Location loc3 = chestLoc.clone().add(1,0,0);
            Location loc4 = chestLoc.clone().add(-1,0,0);

            Bukkit.getScheduler().runTaskLater(GachaRPG.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Block chestBlock = e.getBlock();
                    Chest chest = (Chest) chestBlock.getState();
                    Inventory inv = chest.getInventory();
                    if(inv instanceof DoubleChestInventory) {
                        if(loc1.equals(loc) || loc2.equals(loc) || loc3.equals(loc) || loc4.equals(loc)) {
                            if(!DataKeys.CHEST_TYPE.itemDataContainsStringKey(e.getItemInHand())) {
                                e.getPlayer().sendMessage(ChatUtil.colorize("&7That isn't a collector chest, please place it somewhere else."));
                                e.getBlock().setType(Material.AIR);
                                e.getPlayer().getInventory().addItem(new ItemStack(Material.CHEST));
                            } else {
                                if(DataKeys.CHEST_TYPE.getItemDataString(e.getItemInHand()).equals(DataKeys.CHUNK_TYPE.getChunkDataString(e.getBlock().getChunk()))) {
                                    e.getPlayer().sendMessage(ChatUtil.colorize("&7You have combined two collectors."));
                                } else {
                                    e.getPlayer().sendMessage(ChatUtil.colorize("&7You can only combine same type collectors."));
                                    e.getBlock().setType(Material.AIR);
                                    ItemStack ch = new ItemStack(Material.CHEST);
                                    DataKeys.CHEST_TYPE.setItemDataString(ch, DataKeys.CHEST_TYPE.getItemDataString(e.getItemInHand()));
                                    e.getPlayer().getInventory().addItem(new ItemStack(Material.CHEST));
                                }
                            }
                        }
                    } else {
                        if(!DataKeys.CHEST_TYPE.itemDataContainsStringKey(e.getItemInHand())) {
                            e.getPlayer().sendMessage("You should've just placed a regular chest");
                            return;
                        } else if(DataKeys.CHEST_TYPE.getItemDataString(e.getItemInHand()).equals(DataKeys.CHUNK_TYPE.getChunkDataString(e.getBlock().getChunk()))) {
                            e.getPlayer().sendMessage(ChatUtil.colorize("&7You already have a collector in this chunk."));
                            e.getBlock().setType(Material.AIR);
                            ItemStack ch = new ItemStack(Material.CHEST);
                            DataKeys.CHEST_TYPE.setItemDataString(ch, DataKeys.CHEST_TYPE.getItemDataString(e.getItemInHand()));
                            e.getPlayer().getInventory().addItem(new ItemStack(Material.CHEST));
                        }
                    }
                }
            },1);
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
        Location loc1 = chestLoc.clone().add(0,0,1);
        Location loc2 = chestLoc.clone().add(0,0,-1);
        Location loc3 = chestLoc.clone().add(1,0,0);
        Location loc4 = chestLoc.clone().add(-1,0,0);

        if(loc.equals(loc1) || loc.equals(loc2) || loc.equals(loc3) || loc.equals(loc4)) {
            e.setDropItems(false);
            ItemStack chestCollector = new ItemStack(Material.CHEST);
            String chestType = DataKeys.CHUNK_TYPE.getChunkDataString(e.getBlock().getChunk());
            DataKeys.CHEST_TYPE.setItemDataString(chestCollector, chestType);
            chestLoc.getWorld().dropItemNaturally(chestLoc, chestCollector);
        }

    }

}
