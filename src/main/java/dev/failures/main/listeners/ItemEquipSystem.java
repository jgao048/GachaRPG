package dev.failures.main.listeners;

import dev.failures.main.GachaRPG;
import dev.failures.main.armorequip.ArmorEquipEvent;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.storage.ArmorValues;
import dev.failures.main.storage.NamespacedKeys;
import dev.failures.main.utils.ChatUtil;
import dev.failures.main.utils.PDUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class ItemEquipSystem implements Listener {
    private GachaRPG main;
    private PlayerHandler playerHandler;

    public ItemEquipSystem(GachaRPG main, PlayerHandler playerHandler) {
        this.main = main;
        this.playerHandler = playerHandler;
    }

    @EventHandler
    private void equipArmorEvent(ArmorEquipEvent e) {
        Player p = e.getPlayer();


        if(e.getNewArmorPiece() == null || e.getOldArmorPiece() == null) {
            if(e.getNewArmorPiece() == null) {
                getSetValues(e.getOldArmorPiece(), p, -1);
            } else if(e.getOldArmorPiece() == null) {
                if(checkMeetRequirement(e.getNewArmorPiece(), p)) {
                    getSetValues(e.getNewArmorPiece(), p, 1);
                }else {
                    e.setCancelled(true);
                    return;
                }
            } else {
                if(checkMeetRequirement(e.getNewArmorPiece(), p)) {
                    getSetValues(e.getNewArmorPiece(), p, 1);
                    getSetValues(e.getOldArmorPiece(), p, -1);
                } else {
                    e.setCancelled(true);
                    return;
                }
            }
        } else {
            if(e.getNewArmorPiece().getType().equals(Material.AIR)) {
                getSetValues(e.getOldArmorPiece(), p, -1);
            } else if(e.getOldArmorPiece().getType().equals(Material.AIR)) {
                if(checkMeetRequirement(e.getNewArmorPiece(), p)) {
                    getSetValues(e.getNewArmorPiece(), p, 1);
                } else {
                    e.setCancelled(true);
                    return;
                }
            } else {
                if(checkMeetRequirement(e.getNewArmorPiece(), p)) {
                    getSetValues(e.getNewArmorPiece(), p, 1);
                    getSetValues(e.getOldArmorPiece(), p, -1);
                } else {
                    e.setCancelled(true);
                    return;
                }
            }
        }
        playerHandler.updatePlayerStats(p);
    }

    private boolean checkMeetRequirement(ItemStack armor, Player p) {
        PDUtil itemLevel = new PDUtil(new NamespacedKey(main, "level"));
        if(itemLevel.getItemDataInteger(armor) > playerHandler.getOnlinePlayerSaves().get(p).getLevel()) {
            int level =  itemLevel.getItemDataInteger(armor);
            p.sendMessage(ChatUtil.colorize("&7You can't equip this level &f" + level + " &7item."));
            return false;
        }
        return true;
    }


    private void getSetValues(ItemStack item, Player p, Integer modifier) {
        PDUtil itemStr = new PDUtil(NamespacedKeys.STRENGTH);
        PDUtil itemAgi = new PDUtil(NamespacedKeys.AGILITY);
        PDUtil itemInt = new PDUtil(NamespacedKeys.INTELLIGENCE);
        PDUtil itemVit = new PDUtil(NamespacedKeys.VITALITY);

        playerHandler.getOnlinePlayerSaves().get(p).addStr(itemStr.getItemDataInteger(item) * modifier);
        playerHandler.getOnlinePlayerSaves().get(p).addAgi(itemAgi.getItemDataInteger(item) * modifier);
        playerHandler.getOnlinePlayerSaves().get(p).addInt(itemInt.getItemDataInteger(item) * modifier);
        playerHandler.getOnlinePlayerSaves().get(p).addVit(itemVit.getItemDataInteger(item) * modifier);
    }
}
