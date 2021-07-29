package dev.failures.main.listeners;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.storage.ArmorValues;
import dev.failures.main.storage.NamespacedKeys;
import dev.failures.main.storage.GUIValues;
import dev.failures.main.storage.StatValues;
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
import org.bukkit.inventory.ShapedRecipe;
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
    private void updatedItemAfterCraft(CraftItemEvent e) {
        if(e.getCurrentItem() == null) return;
        if(!isArmor(e.getCurrentItem())) return;
        ItemStack crafted = e.getCurrentItem();

        PDUtil itemStr = new PDUtil(NamespacedKeys.STRENGTH);
        PDUtil itemAgi = new PDUtil(NamespacedKeys.AGILITY);
        PDUtil itemInt = new PDUtil(NamespacedKeys.INTELLIGENCE);
        PDUtil itemVit = new PDUtil(NamespacedKeys.VITALITY);
        PDUtil itemLevel = new PDUtil(NamespacedKeys.LEVEL);

        int str = itemStr.getItemDataInteger(crafted);
        int agi = itemAgi.getItemDataInteger(crafted);
        int inte = itemInt.getItemDataInteger(crafted);
        int vit = itemVit.getItemDataInteger(crafted);
        int level = itemLevel.getItemDataInteger(crafted);

        ItemMeta im = crafted.getItemMeta();
        updateItemLore(im, level, str, agi, inte, vit, true);
        crafted.setItemMeta(im);
    }


    @EventHandler
    private void changeCraftResults(PrepareItemCraftEvent e) {
        if(e.getInventory().getResult() == null) return;
        if(!isArmor(e.getInventory().getResult())) return;
        ItemStack itemCrafted = e.getInventory().getResult();
        ItemStack itemPlaced = e.getInventory().getContents()[5];
        Material itemCraftedType = e.getInventory().getResult().getType();


        PDUtil itemStr = new PDUtil(NamespacedKeys.STRENGTH);
        PDUtil itemAgi = new PDUtil(NamespacedKeys.AGILITY);
        PDUtil itemInt = new PDUtil(NamespacedKeys.INTELLIGENCE);
        PDUtil itemVit = new PDUtil(NamespacedKeys.VITALITY);
        PDUtil itemLevel = new PDUtil(NamespacedKeys.LEVEL);

        if(itemStr.itemDataContainsIntegerKey(itemPlaced)) {
            randomizeStats(itemPlaced, itemCrafted);
            e.getInventory().setResult(itemCrafted);
        } else { //create new armor
            int str = ArmorValues.getBaseStats(itemCraftedType).get("str");
            int agi = ArmorValues.getBaseStats(itemCraftedType).get("agi");
            int inte = ArmorValues.getBaseStats(itemCraftedType).get("inte");
            int vit = ArmorValues.getBaseStats(itemCraftedType).get("vit");
            int level = ArmorValues.getBaseStats(itemCraftedType).get("level");

            itemStr.setItemDataInteger(itemCrafted, str);
            itemAgi.setItemDataInteger(itemCrafted, agi);
            itemInt.setItemDataInteger(itemCrafted, inte);
            itemVit.setItemDataInteger(itemCrafted, vit);
            itemLevel.setItemDataInteger(itemCrafted, level);

            ItemMeta im = itemCrafted.getItemMeta();

            updateItemLore(im, level, str, agi, inte, vit, true);
            itemCrafted.setItemMeta(im);
            e.getInventory().setResult(itemCrafted);
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
        updateItemLore(im, level, str, agi, inte, vit, true);
        itemCrafted.setItemMeta(im);
        e.setResult(itemCrafted);
    }

    @EventHandler
    private void preventETableUsage(PlayerInteractEvent e) {
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE)) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatUtil.colorize("&7Enchantment Tables have been disabled."));
        }
    }

    @EventHandler
    private void afterAnvilUsage(InventoryClickEvent e) {
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


    public static void updateItemLore(ItemMeta im, int level, int str, int agi, int inte, int vit, boolean visible) {
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
        if(visible) {
            String stre = String.valueOf(str);
            if(str > 0) stre = "+" + stre;
            String agil = String.valueOf(agi);
            if(agi > 0) agil = "+" + agil;
            String intel = String.valueOf(inte);
            if(inte > 0) intel = "+" + intel;
            String vita = String.valueOf(vit);
            if(vit > 0) vita = "+" + vita;
            lore.add(Component.text(ChatUtil.colorize("&7┌ &c❁ " + stre + " Strength")));
            lore.add(Component.text(ChatUtil.colorize("&7├ &a✦ " + agil + " Agility")));
            lore.add(Component.text(ChatUtil.colorize("&7├ &9❉ " + intel + " Intelligence")));
            lore.add(Component.text(ChatUtil.colorize("&7└ &d✚ " + vita + " Vitality")));
        } else {
            lore.add(Component.text(ChatUtil.colorize("&7┌ &c❁ ? Strength")));
            lore.add(Component.text(ChatUtil.colorize("&7├ &a✦ ? Agility")));
            lore.add(Component.text(ChatUtil.colorize("&7├ &9❉ ? Intelligence")));
            lore.add(Component.text(ChatUtil.colorize("&7└ &d✚ ? Vitality")));
        }
        im.lore(lore);
    }

    private void randomizeStats(ItemStack placed, ItemStack crafted) {
        PDUtil itemStr = new PDUtil(NamespacedKeys.STRENGTH);
        PDUtil itemAgi = new PDUtil(NamespacedKeys.AGILITY);
        PDUtil itemInt = new PDUtil(NamespacedKeys.INTELLIGENCE);
        PDUtil itemVit = new PDUtil(NamespacedKeys.VITALITY);
        PDUtil itemLevel = new PDUtil(NamespacedKeys.LEVEL);

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

        int str = itemStr.getItemDataInteger(placed);
        int agi = itemAgi.getItemDataInteger(placed);
        int inte = itemInt.getItemDataInteger(placed);
        int vit = itemVit.getItemDataInteger(placed);
        int level = itemLevel.getItemDataInteger(placed) +1;

        if(pool == "str") {
            str++;
        } else if(pool == "agi") {
            agi++;
        } else if(pool == "inte") {
            inte++;
        } else {
            vit++;
        }
        itemStr.setItemDataInteger(crafted, str);
        itemAgi.setItemDataInteger(crafted, agi);
        itemInt.setItemDataInteger(crafted, inte);
        itemVit.setItemDataInteger(crafted, vit);
        itemLevel.setItemDataInteger(crafted, level);

        ItemMeta im = crafted.getItemMeta();
        updateItemLore(im, level, str, agi, inte, vit, false);
        crafted.setItemMeta(im);
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

    private static String getColor(String enchName) {
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

    private boolean isArmor(ItemStack item) {
        List<Material> armors = Arrays.asList(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS);
        return armors.contains(item.getType());
    }
}
