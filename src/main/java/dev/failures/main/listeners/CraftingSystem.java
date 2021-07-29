package dev.failures.main.listeners;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.storage.ArmorValues;
import dev.failures.main.storage.NamespacedKeys;
import dev.failures.main.storage.GUIValues;
import dev.failures.main.storage.StatValues;
import dev.failures.main.utils.ChatUtil;
import dev.failures.main.utils.PDUtil;
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

    public CraftingSystem(GachaRPG main, PlayerHandler playerHandler) {
        this.main = main;
        this.playerHandler = playerHandler;
    }

    @EventHandler
    private void craftingSystem(CraftItemEvent e) {
        ItemStack itemCrafted = e.getCurrentItem();
        if(ArmorValues.getBaseStats(itemCrafted.getType()) == null) return;

        Material type = itemCrafted.getType();

        int str = ArmorValues.getBaseStats(type).get("str");
        int agi = ArmorValues.getBaseStats(type).get("agi");
        int inte = ArmorValues.getBaseStats(type).get("inte");
        int vit = ArmorValues.getBaseStats(type).get("vit");
        int level = ArmorValues.getBaseStats(type).get("level");

        PDUtil itemStr = new PDUtil(NamespacedKeys.STRENGTH);
        PDUtil itemAgi = new PDUtil(NamespacedKeys.AGILITY);
        PDUtil itemInt = new PDUtil(NamespacedKeys.INTELLIGENCE);
        PDUtil itemVit = new PDUtil(NamespacedKeys.VITALITY);
        PDUtil itemLevel = new PDUtil(NamespacedKeys.LEVEL);

        itemStr.setItemDataInteger(itemCrafted, str);
        itemAgi.setItemDataInteger(itemCrafted, agi);
        itemInt.setItemDataInteger(itemCrafted, inte);
        itemVit.setItemDataInteger(itemCrafted, vit);
        itemLevel.setItemDataInteger(itemCrafted, level);

        ItemMeta im = itemCrafted.getItemMeta();
        updateItemLore(im, level, str, agi, inte, vit);
        itemCrafted.setItemMeta(im);
        e.setCurrentItem(itemCrafted);
    }

    @EventHandler
    private void changeCraftResults(PrepareItemCraftEvent e) {
        if(e.getInventory().getResult().equals(new ItemStack(Material.DIAMOND_HELMET))) {

        }
    }


    @EventHandler
    private void anvilUsage(PrepareAnvilEvent e) {
        if(e.getResult() == null) return;
        ItemStack itemCrafted = e.getResult();
        Material type = itemCrafted.getType();

        int str = ArmorValues.getBaseStats(type).get("str");
        int agi = ArmorValues.getBaseStats(type).get("agi");
        int inte = ArmorValues.getBaseStats(type).get("inte");
        int vit = ArmorValues.getBaseStats(type).get("vit");
        int level = ArmorValues.getBaseStats(type).get("level");


        ItemMeta im = itemCrafted.getItemMeta();
        updateItemLore(im, level, str, agi, inte, vit);
        itemCrafted.setItemMeta(im);
        e.setResult(itemCrafted);
    }

    @EventHandler
    private void eTableUsage(PlayerInteractEvent e) {
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE)) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatUtil.colorize("&7Enchantment Tables have been disabled."));
        }
    }

    @EventHandler
    private void afterEnchantmentTable(InventoryClickEvent e) {
        if(e.getInventory().getType().equals(InventoryType.ANVIL)) {
            if(e.getSlot() == 2) {
                Player p = (Player) e.getWhoClicked();
                Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                    @Override
                    public void run() {
                        p.setLevel(0);
                        p.giveExpLevels(playerHandler.getOnlinePlayerSaves().get(p).getLevel());
                        updateExpBar(p);
                    }
                },10);
            }
        }
    }


    private void updateItemLore(ItemMeta im, int level, int str, int agi, int inte, int vit) {
        if(!im.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            //im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            //im.setUnbreakable(true);
        }
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatUtil.colorize("&cRequires Level " + level)));
        lore.add(Component.text(" "));
        if(im.getEnchants().size() > 0) {
            lore.add(Component.text(ChatUtil.colorize("&7&m             &r&7「Enchants」&m             ")));

            for(Map.Entry<Enchantment, Integer> ench : im.getEnchants().entrySet()) {
                String enchName = WordUtils.capitalize(ench.getKey().getKey().getKey());
                String colorEnchant = getColor(enchName);
                lore.add(Component.text(ChatUtil.colorize("&7➸ &f" + enchName + " " + ench.getValue())));
            }
            lore.add(Component.text(ChatUtil.colorize(" ")));
        }
        lore.add(Component.text(ChatUtil.colorize("&7&m             &7「Statistics」&m             ")));
        lore.add(Component.text(ChatUtil.colorize("&7┌ &c+" + str + "❁ Strength")));
        lore.add(Component.text(ChatUtil.colorize("&7├ &a+" + agi + "✦ Agility")));
        lore.add(Component.text(ChatUtil.colorize("&7├ &9+" + inte + "❉ Intelligence")));
        lore.add(Component.text(ChatUtil.colorize("&7└ &d+" + vit + "✚ Vitality")));
        im.lore(lore);
    }


    private String getColor(String enchName) {
        if(enchName.equalsIgnoreCase("protection")) return GUIValues.PROTECTION_COLOR;
        if(enchName.equalsIgnoreCase("thorns")) return "&4";
        return "&7";
    }

    private void updateExpBar(Player player) {
        int currentLevel = playerHandler.getOnlinePlayerSaves().get(player).getLevel();
        double currentExp = playerHandler.getOnlinePlayerSaves().get(player).getExp();
        double expNeeded = StatValues.BASE_EXP_NEEDED * Math.pow(StatValues.EXP_GROWTH,currentLevel-1);

        if(currentExp >= expNeeded) {
            int remainder = (int) (currentExp - expNeeded);
            int newLevel = currentLevel+1;
            player.setLevel(newLevel);
            player.setExp(0);
            playerHandler.getOnlinePlayerSaves().get(player).addLevel(1);
            playerHandler.getOnlinePlayerSaves().get(player).setExp(remainder);
            playerHandler.getOnlinePlayerSaves().get(player).addSkillPoints(1);
            player.sendTitle(ChatUtil.colorize("&aLEVEL UP!"), ChatUtil.colorize("&7You have gained a skill point"), 10, 40,20);
        } else {
            int expMinecraft = player.getExpToLevel();
            double percent = currentExp / expNeeded;

            int roundedExp = (int) Math.round(expMinecraft * percent);
            player.setExp((float) percent);
        }
    }
}
