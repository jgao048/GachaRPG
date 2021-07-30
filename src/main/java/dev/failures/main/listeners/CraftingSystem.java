package dev.failures.main.listeners;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.storage.ArmorValues;
import dev.failures.main.storage.DataKeys;
import dev.failures.main.storage.TextureValues;
import dev.failures.main.storage.GameValues;
import dev.failures.main.utils.ChatUtil;
import dev.failures.main.utils.PDUtil;
import dev.triumphteam.gui.components.nbt.LegacyNbt;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CraftingSystem implements Listener {
    private GachaRPG main;
    private PlayerHandler playerHandler;
    private LegacyNbt CraftItemStack;

    public CraftingSystem(GachaRPG main, PlayerHandler playerHandler) {
        this.main = main;
        this.playerHandler = playerHandler;
    }

    @EventHandler
    private void updateLoreCrafted(CraftItemEvent e) {
        if(e.getCurrentItem() == null) return;
        if(!isArmor(e.getCurrentItem())) return;
        ItemStack itemCrafted = e.getCurrentItem();
        updateItemLore(itemCrafted, true);
    }

    @EventHandler
    private void changeCraftResults(PrepareItemCraftEvent e) {
        if(e.getInventory().getResult() == null) return;
        if(!isArmor(e.getInventory().getResult())) return;
        ItemStack itemCrafted = e.getInventory().getResult();
        ItemStack itemPlaced = e.getInventory().getContents()[5];
        Material itemCraftedType = e.getInventory().getResult().getType();

        if(DataKeys.hasStrength(itemPlaced)) {
            randomizeStats(itemPlaced, itemCrafted);
            itemCrafted.addEnchantments(itemPlaced.getEnchantments());
            updateItemLore(itemCrafted, false);
            e.getInventory().setResult(itemCrafted);
        } else { //create new armor
            int strBase = ArmorValues.getBaseStats(itemCraftedType).get("str");
            int agiBase = ArmorValues.getBaseStats(itemCraftedType).get("agi");
            int intelBase = ArmorValues.getBaseStats(itemCraftedType).get("intel");
            int vitBase = ArmorValues.getBaseStats(itemCraftedType).get("vit");
            int levelBase = ArmorValues.getBaseStats(itemCraftedType).get("level");

            DataKeys.setStrength(itemCrafted, strBase);
            DataKeys.setAgility(itemCrafted, agiBase);
            DataKeys.setIntel(itemCrafted, intelBase);
            DataKeys.setVitality(itemCrafted, vitBase);
            DataKeys.setLevel(itemCrafted, levelBase);

            updateItemLore(itemCrafted, true);
            e.getInventory().setResult(itemCrafted);
        }

    }

    @EventHandler
    private void anvilUsage(PrepareAnvilEvent e) {
        if(e.getResult() == null) return;
        ItemStack itemCrafted = e.getResult();
        updateItemLore(itemCrafted,true);
        e.setResult(itemCrafted);
    }

    @EventHandler
    private void preventETableUsage(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE)) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatUtil.colorize("&7Enchantment Tables have been disabled."));
        }
    }

    @EventHandler
    private void refundExpAnvil(InventoryClickEvent e) {
        if(e.getInventory().getType().equals(InventoryType.ANVIL)) {
            if(e.getSlot() == 2) {
                Player p = (Player) e.getWhoClicked();
                Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                    @Override
                    public void run() {
                        p.setLevel(0);
                        p.giveExpLevels(playerHandler.getOnlinePlayerSaves().get(p).getLevel());
                        playerHandler.updatePlayerExp(p);
                    }
                },10);
            }
        }
    }


    public static void updateItemLore(ItemStack craftedItem, boolean visible) {
        ItemMeta im = craftedItem.getItemMeta();
        if(!im.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        int str = DataKeys.getStrength(craftedItem);
        int agi = DataKeys.getAgility(craftedItem);
        int intel = DataKeys.getIntel(craftedItem);
        int vit = DataKeys.getVitality(craftedItem);
        int level = DataKeys.getLevel(craftedItem);

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatUtil.colorize("&cRequires Level " + level)));
        lore.add(Component.text(" "));
        if(im.getEnchants().size() > 0) {
            lore.add(Component.text(ChatUtil.colorize("&7&m             &r&7「Enchants」&m             ")));
            for(Map.Entry<Enchantment, Integer> enchant : im.getEnchants().entrySet()) {
                String enchantName = WordUtils.capitalize(enchant.getKey().getKey().getKey());
                lore.add(Component.text(ChatUtil.colorize("&7➸ &f" + enchantName + " " + enchant.getValue())));
            }
            lore.add(Component.text(ChatUtil.colorize(" ")));
        }
        lore.add(Component.text(ChatUtil.colorize("&7&m             &7「Statistics」&m             ")));
        if(visible) {
            String strString = String.valueOf(str); if(str > 0) strString = "+" + strString;
            String agiString = String.valueOf(agi); if(agi > 0) agiString = "+" + agiString;
            String intString = String.valueOf(intel); if(intel > 0) intString = "+" + intString;
            String vitString = String.valueOf(vit); if(vit > 0) vitString = "+" + vitString;

            lore.add(Component.text(ChatUtil.colorize("&7┌ &c❁ " + strString + " Strength")));
            lore.add(Component.text(ChatUtil.colorize("&7├ &a✦ " + agiString + " Agility")));
            lore.add(Component.text(ChatUtil.colorize("&7├ &9❉ " + intString + " Intelligence")));
            lore.add(Component.text(ChatUtil.colorize("&7└ &d✚ " + vitString + " Vitality")));
        } else {
            lore.add(Component.text(ChatUtil.colorize("&7┌ &c❁ ? Strength")));
            lore.add(Component.text(ChatUtil.colorize("&7├ &a✦ ? Agility")));
            lore.add(Component.text(ChatUtil.colorize("&7├ &9❉ ? Intelligence")));
            lore.add(Component.text(ChatUtil.colorize("&7└ &d✚ ? Vitality")));
        }
        im.lore(lore);
        craftedItem.setItemMeta(im);
    }

    private void randomizeStats(ItemStack placed, ItemStack crafted) {
        String pool = "";
        if(crafted.getType().toString().contains("DIAMOND")) {
            pool = getRandom("str", 4, "agi", 2, "inte", 2, "vit", 2);
        } else if(crafted.getType().toString().contains("GOLDEN")) {
            pool = getRandom("str", 2, "agi", 2, "inte", 2, "vit", 4);
        } else if(crafted.getType().toString().contains("IRON")) {
            pool = getRandom("str", 2, "agi", 4, "inte", 2, "vit", 2);
        } else if(crafted.getType().toString().contains("LEATHER")) {
            pool = getRandom("str", 2, "agi", 2, "inte", 4, "vit", 2);
        }

        int str = DataKeys.getStrength(placed);
        int agi = DataKeys.getAgility(placed);
        int intel = DataKeys.getIntel(placed);
        int vit = DataKeys.getVitality(placed);
        int level = DataKeys.getLevel(placed) + 1;

        if(pool == "str") {
            str++;
        } else if(pool == "agi") {
            agi++;
        } else if(pool == "inte") {
            intel++;
        } else {
            vit++;
        }
        DataKeys.setStrength(crafted, str);
        DataKeys.setAgility(crafted, agi);
        DataKeys.setIntel(crafted, intel);
        DataKeys.setVitality(crafted, vit);
        DataKeys.setLevel(crafted, level);
    }

    private String getRandom(String s, int sa, String a, int aa, String i, int ia, String v, int va) {
        ArrayList<String> values = new ArrayList<>();
        for(int j = 0 ; j < sa ; j++) {
            values.add(s);
        }
        for(int j = 0 ; j < ia ; j++) {
            values.add(a);
        }
        for(int j = 0 ; j < aa ; j++) {
            values.add(i);
        }
        for(int j = 0 ; j < va ; j++) {
            values.add(v);
        }
        int index = (int)(Math.random() * values.size());
        return values.get(index);
    }

    private boolean isArmor(ItemStack item) {
        return ArmorValues.getArmors().contains(item.getType());
    }
}
