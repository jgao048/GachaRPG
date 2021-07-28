package dev.failures.main.commands;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PlayerData;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.storage.Values;
import dev.failures.main.utils.ChatUtil;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        statsGUI.setItem(2, 7, ItemBuilder.skull()
                .name(Component.text(ChatUtil.colorize("&cStrength")))
                .amount(str)
                .texture(Values.strHead)
                .lore(Arrays.asList(Component.text(ChatUtil.colorize("&7Increases health")), Component.text(ChatUtil.colorize("&7Max Health: &f" + currHP))))
                .asGuiItem());
        statsGUI.setItem(3, 7, ItemBuilder.skull()
                .name(Component.text(ChatUtil.colorize("&aAgility")))
                .amount(agi)
                .texture(Values.agiHead)
                .lore(Arrays.asList(Component.text(ChatUtil.colorize("&7Increases speed")), Component.text(ChatUtil.colorize("&7Speed: &f" + currSpeed))))
                .asGuiItem());
        statsGUI.setItem(4, 7, ItemBuilder.skull()
                .name(Component.text(ChatUtil.colorize("&9Intelligence")))
                .amount(inte)
                .texture(Values.intHead)
                .lore(Arrays.asList(Component.text(ChatUtil.colorize("&7Increases mana regeneration"))))
                .asGuiItem());
        statsGUI.setItem(5, 7, ItemBuilder.skull()
                .name(Component.text(ChatUtil.colorize("&dVitality")))
                .amount(vit)
                .texture(Values.vitHead)
                .lore(Arrays.asList(Component.text(ChatUtil.colorize("&7Increases health regeneration")), Component.text(ChatUtil.colorize("&7Health every &f" + currRegenSeconds + "&7 seconds"))))
                .asGuiItem());
        statsGUI.open(p);
        return false;
    }
}
