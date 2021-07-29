package dev.failures.main.commands;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PlayerData;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.storage.GUIValues;
import dev.failures.main.utils.ChatUtil;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class StatsCommand implements CommandExecutor {
    GachaRPG main;
    PlayerHandler ph;

    public StatsCommand(GachaRPG main, PlayerHandler ph) {
        this.main = main;
        this.ph = ph;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        PlayerData data = ph.getOnlinePlayerSaves().get(p);

        Gui statsGUI = Gui.gui()
                .title(Component.text(ChatUtil.colorize("&0Character Information")))
                .rows(6)
                .create();
        statsGUI.setDefaultClickAction(event -> event.setCancelled(true));

        int str = ph.getOnlinePlayerSaves().get(p).getStr();
        double currHP = ph.getOnlinePlayerSaves().get(p).getCurrentHealth();
        int agi = ph.getOnlinePlayerSaves().get(p).getAgi();
        double currSpeed = ph.getOnlinePlayerSaves().get(p).getCurrentSpeed();

        int inte = ph.getOnlinePlayerSaves().get(p).getInt();
        int vit = ph.getOnlinePlayerSaves().get(p).getVit();
        int currRegenTicks = ph.getOnlinePlayerSaves().get(p).getCurrentRegenHP();
        double currRegenSeconds = currRegenTicks/((double) 20);

        statsGUI.setItem(2, 6, ItemBuilder.skull()
                .name(Component.text(ChatUtil.colorize("&cStrength ❁")))
                .amount(str)
                .texture(GUIValues.strHead)
                .lore(Arrays.asList(Component.text(ChatUtil.colorize("&7Increases max health to take more damage")), Component.text(ChatUtil.colorize("&7Current max health is &f" + currHP))))
                .asGuiItem());
        statsGUI.setItem(3, 6, ItemBuilder.skull()
                .name(Component.text(ChatUtil.colorize("&aAgility ✦")))
                .amount(agi)
                .texture(GUIValues.agiHead)
                .lore(Arrays.asList(Component.text(ChatUtil.colorize("&7Increases speed to increase survivability")), Component.text(ChatUtil.colorize("&7Current speed is &f" + String.format("%,.2f",currSpeed)))))
                .asGuiItem());
        statsGUI.setItem(4, 6, ItemBuilder.skull()
                .name(Component.text(ChatUtil.colorize("&9Intelligence ❉")))
                .amount(inte)
                .texture(GUIValues.intHead)
                .lore(Arrays.asList(Component.text(ChatUtil.colorize("&7Increases mana regeneration to cast more spells")), Component.text(ChatUtil.colorize("&7Current mana regen is &f5"))))
                .asGuiItem());
        statsGUI.setItem(5, 6, ItemBuilder.skull()
                .name(Component.text(ChatUtil.colorize("&dVitality ✚")))
                .amount(vit)
                .texture(GUIValues.vitHead)
                .lore(Arrays.asList(Component.text(ChatUtil.colorize("&7Increases health regeneration to stay alive longer")), Component.text(ChatUtil.colorize("&7Current health regen is &f" + currRegenSeconds + "s"))))
                .asGuiItem());

        int skillPoints = ph.getOnlinePlayerSaves().get(p).getSkillPoints();
        /*
        statsGUI.setItem(1, 8, ItemBuilder.skull()
                .name(Component.text(ChatUtil.colorize("&eSkill Points")))
                .amount(skillPoints)
                .texture(Values.spHead)
                .lore(Arrays.asList(Component.text(ChatUtil.colorize("&7Use your points below"))))
                .asGuiItem());
        */
        statsGUI.setItem(6, 9, ItemBuilder.skull()
                .name(Component.text(ChatUtil.colorize("&4Reset Skill Points")))
                .texture(GUIValues.resetHead)
                .asGuiItem(event -> {
                    ph.getOnlinePlayerSaves().get(p).resetSkillPoints(p);
                    ph.updatePlayerStats(p);
                    p.performCommand("stats");
                }));

        if(skillPoints > 0) {
            statsGUI.setItem(2, 7, ItemBuilder.skull()
                    .name(Component.text(ChatUtil.colorize("&a&l+1")))
                    .amount(skillPoints)
                    .texture(GUIValues.spUpgrade)
                    .asGuiItem(event -> {
                        ph.getOnlinePlayerSaves().get(p).setSkillPoints(skillPoints-1);
                        ph.getOnlinePlayerSaves().get(p).addStr(1);
                        ph.updatePlayerStats(p);
                        p.performCommand("stats");
                    }));
            statsGUI.setItem(3, 7, ItemBuilder.skull()
                    .name(Component.text(ChatUtil.colorize("&a&l+1")))
                    .amount(skillPoints)
                    .texture(GUIValues.spUpgrade)
                    .asGuiItem(event -> {
                        ph.getOnlinePlayerSaves().get(p).setSkillPoints(skillPoints-1);
                        ph.getOnlinePlayerSaves().get(p).addAgi(1);
                        ph.updatePlayerStats(p);
                        p.performCommand("stats");
                    }));
            statsGUI.setItem(4, 7, ItemBuilder.skull()
                    .name(Component.text(ChatUtil.colorize("&a&l+1")))
                    .amount(skillPoints)
                    .texture(GUIValues.spUpgrade)
                    .asGuiItem(event -> {
                        ph.getOnlinePlayerSaves().get(p).setSkillPoints(skillPoints-1);
                        ph.getOnlinePlayerSaves().get(p).addInt(1);
                        ph.updatePlayerStats(p);
                        p.performCommand("stats");
                    }));
            statsGUI.setItem(5, 7, ItemBuilder.skull()
                    .name(Component.text(ChatUtil.colorize("&a&l+1")))
                    .amount(skillPoints)
                    .texture(GUIValues.spUpgrade)
                    .asGuiItem(event -> {
                        ph.getOnlinePlayerSaves().get(p).setSkillPoints(skillPoints-1);
                        ph.getOnlinePlayerSaves().get(p).addVit(1);
                        ph.updatePlayerStats(p);
                        p.performCommand("stats");
                    }));
        }

        int row = 5;
        for(ItemStack armorPiece : p.getInventory().getArmorContents()) {
            if(armorPiece == null) {
                statsGUI.setItem(row, 3, ItemBuilder.skull()
                        .name(Component.text(ChatUtil.colorize("&7Empty")))
                        .texture(GUIValues.emptyHead)
                        .asGuiItem());
            } else {
                statsGUI.setItem(row, 3, ItemBuilder.from(armorPiece).asGuiItem());
            }
            row--;
        }

        statsGUI.open(p);
        return false;
    }
}
