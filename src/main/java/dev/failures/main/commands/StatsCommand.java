package dev.failures.main.commands;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PlayerData;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.storage.GameValues;
import dev.failures.main.storage.TextureValues;
import dev.failures.main.utils.ChatUtil;
import dev.failures.main.utils.GuiUtil;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class StatsCommand implements CommandExecutor {
    GachaRPG main;
    PlayerHandler playerHandler;

    public StatsCommand(GachaRPG main, PlayerHandler ph) {
        this.main = main;
        this.playerHandler = ph;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        PlayerData data = playerHandler.getOnlinePlayerSaves().get(p);

        Gui statsGUI = Gui.gui()
                .title(Component.text(ChatUtil.colorize("&0      &0Character Information")))
                .rows(6)
                .create();

        statsGUI.setDefaultClickAction(event -> event.setCancelled(true));
        statsGUI.getFiller().fill(ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(ChatUtil.text("")).asGuiItem());

        int str = playerHandler.getOnlinePlayerSaves().get(p).getStr();
        double currHP = playerHandler.getOnlinePlayerSaves().get(p).getCurrentHealth();
        int agi = playerHandler.getOnlinePlayerSaves().get(p).getAgi();
        double currSpeed = playerHandler.getOnlinePlayerSaves().get(p).getCurrentSpeed();
        int intel = playerHandler.getOnlinePlayerSaves().get(p).getInt();
        int vit = playerHandler.getOnlinePlayerSaves().get(p).getVit();
        int currRegenTicks = playerHandler.getOnlinePlayerSaves().get(p).getCurrentRegenHP();
        double currRegenSeconds = currRegenTicks/((double) 20);
        int skillPoints = playerHandler.getOnlinePlayerSaves().get(p).getSkillPoints();
        double balance = playerHandler.getOnlinePlayerSaves().get(p).getGold();
        int level = playerHandler.getOnlinePlayerSaves().get(p).getLevel();
        double currExp = playerHandler.getOnlinePlayerSaves().get(p).getExp();
        double expNeeded = GameValues.BASE_EXP_NEEDED * Math.pow(GameValues.EXP_GROWTH,level-1);

        statsGUI.setItem(2, 7, ItemBuilder.skull()
                .name(ChatUtil.text("&c&lSTRENGTH &c❁"))
                .texture(TextureValues.strHead)
                .lore(ChatUtil.getListComponent(Arrays.asList("&7Increases max health by &f" + GameValues.HEATLH_PER_STR + " &7for","&7every point in Strength.","","&7Current Strength: &f" + str,"&7Current Health: &f" + currHP,"","&7Click to add a point into Strength")))
                .asGuiItem(event -> {
                    if(skillPoints < 1) return;
                    playerHandler.getOnlinePlayerSaves().get(p).setSkillPoints(skillPoints-1);
                    playerHandler.getOnlinePlayerSaves().get(p).addStr(1);
                    p.performCommand("stats");
                }));

        statsGUI.setItem(3, 7, ItemBuilder.skull()
                .name(ChatUtil.text("&a&lAgility &a✦"))
                .texture(TextureValues.agiHead)
                .lore(ChatUtil.getListComponent(Arrays.asList("&7Increases move speed by &f" + GameValues.SPEED_PER_AGI + " &7for","&7every point in Agility.","","&7Current Agility: &f" + agi,"&7Current Speed: &f" + String.format("%,.2f",currSpeed),"","&7Click to add a point into Agility")))
                .asGuiItem(event -> {
                    if(skillPoints < 1) return;
                    playerHandler.getOnlinePlayerSaves().get(p).setSkillPoints(skillPoints-1);
                    playerHandler.getOnlinePlayerSaves().get(p).addAgi(1);
                    p.performCommand("stats");
                }));

        statsGUI.setItem(4, 7, ItemBuilder.skull()
                .name(ChatUtil.text("&9&lINTELLIGENCE &9❉"))
                .texture(TextureValues.intHead)
                .lore(ChatUtil.getListComponent(Arrays.asList("&7Increases mana regen by &f" + GameValues.MANA_PER_INT + "s &7for","&7every point in Intelligence.","","&7Current Intelligence: &f" + intel,"&7Current Regen: &f" + currRegenSeconds + "s","","&7Click to add a point into Intelligence")))
                .asGuiItem(event -> {
                    if(skillPoints < 1) return;
                    playerHandler.getOnlinePlayerSaves().get(p).setSkillPoints(skillPoints-1);
                    playerHandler.getOnlinePlayerSaves().get(p).addInt(1);
                    p.performCommand("stats");
                }));


        statsGUI.setItem(5, 7, ItemBuilder.skull()
                .name(ChatUtil.text(("&d&lVITALITY &d✚")))
                .texture(TextureValues.vitHead)
                .lore(ChatUtil.getListComponent(Arrays.asList("&7Increases health regen by &f" + GameValues.REGEN_PER_VIT/(double)20 + "s &7for","&7every point in Vitality.","","&7Current Vitality: &f" + vit,"&7Current Regen: &f" + currRegenSeconds + "s","","&7Click to add a point into Vitality")))
                .asGuiItem(event -> {
                    if(skillPoints < 1) return;
                    playerHandler.getOnlinePlayerSaves().get(p).setSkillPoints(skillPoints-1);
                    playerHandler.getOnlinePlayerSaves().get(p).addVit(1);
                    p.performCommand("stats");
                }));

        statsGUI.setItem(2, 5, ItemBuilder.skull()
                .name(ChatUtil.text("&e&lPlayer Level"))
                .owner(p)
                .lore(ChatUtil.getListComponent(Arrays.asList("&7You can gain levels by filling up","&7your exp bar and you can get exp","&7from killing mobs and players.","","&7Current Level: &f" + level, "&7Exp: &f" + currExp + "&7/" +expNeeded)))
                .asGuiItem());


        statsGUI.setItem(3, 5, ItemBuilder.skull()
                .name(ChatUtil.text("&a&lBalance"))
                .texture(TextureValues.balanceHead)
                .lore(ChatUtil.getListComponent(Arrays.asList("&7Can be obtained through killing mobs","&7and selling items to other players.","","&7Current Balance: &f" + ChatUtil.withCommas(balance))))
                .asGuiItem());


        statsGUI.setItem(4, 5, ItemBuilder.skull()
                .name(ChatUtil.text("&d&lSkill Points"))
                .texture(TextureValues.spHead)
                .lore(ChatUtil.getListComponent(Arrays.asList("&7You get a skill point every time you","&7level up. You can use these points to","&7upgrade the stats on the right","","&7You have &f" + skillPoints + " &7skill points available.")))
                .asGuiItem());


        statsGUI.setItem(5, 5, ItemBuilder.skull()
                .name(Component.text(ChatUtil.colorize("&4&lRESET SKILL POINTS")))
                .texture(TextureValues.resetHead)
                .asGuiItem(event -> {
                    playerHandler.getOnlinePlayerSaves().get(p).resetSkillPoints(p);
                    playerHandler.updatePlayerStats(p);
                    p.performCommand("stats");
                }));

        int row = 5;
        for(ItemStack armorPiece : p.getInventory().getArmorContents()) {
            if(armorPiece == null) {
                statsGUI.setItem(row, 3, ItemBuilder.skull()
                        .name(Component.text(ChatUtil.colorize("&7Slot is empty")))
                        .texture(TextureValues.emptyHead)
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
