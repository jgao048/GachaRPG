package dev.failures.main.listeners;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PartyHandler;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.storage.GUIValues;
import dev.failures.main.storage.StatValues;
import dev.failures.main.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerRespawnEvent;

public class LevelSystem implements Listener {
    private GachaRPG main;
    PlayerHandler ph;
    PartyHandler partyH;


    public LevelSystem(GachaRPG main, PlayerHandler ph, PartyHandler partyH) {
        this.main = main;
        this.ph = ph;
        this.partyH = partyH;
    }

    @EventHandler
    private void mobExpDrop(EntityDeathEvent e) {
        if(e.getEntity() instanceof Player) {
            e.setDroppedExp(0);
            return;
        }
        if(e.getEntity().getKiller() == null) return;
        Player p = e.getEntity().getKiller();
        e.setDroppedExp(0);

        for(Player partyMem : partyH.getNearbyMembers(p,10,e.getEntity())) {
            ph.getOnlinePlayerSaves().get(partyMem).addExp(StatValues.EXP_DROP * StatValues.EXP_SHARED);
            updateExpBar(partyMem);
        }

        ph.getOnlinePlayerSaves().get(p).addExp(StatValues.EXP_DROP);
        ph.getOnlinePlayerSaves().get(p).addGold(10);
        updateExpBar(p);
    }

    @EventHandler
    private void resetExpBar(PlayerRespawnEvent e) {
        Bukkit.getScheduler().runTaskLater(main, new Runnable() {
            @Override
            public void run() {
                e.getPlayer().giveExpLevels(ph.getOnlinePlayerSaves().get(e.getPlayer()).getLevel());
                updateExpBar(e.getPlayer());
            }
        },10);
    }

    @EventHandler
    private void removeHunger(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        e.setFoodLevel(20);
    }

    private void updateExpBar(Player player) {
        int currentLevel = ph.getOnlinePlayerSaves().get(player).getLevel();
        double currentExp = ph.getOnlinePlayerSaves().get(player).getExp();
        double expNeeded = StatValues.BASE_EXP_NEEDED * Math.pow(StatValues.EXP_GROWTH,currentLevel-1);

        if(currentExp >= expNeeded) {
            int remainder = (int) (currentExp - expNeeded);
            int newLevel = currentLevel+1;
            player.setLevel(newLevel);
            player.setExp(0);
            ph.getOnlinePlayerSaves().get(player).addLevel(1);
            ph.getOnlinePlayerSaves().get(player).setExp(remainder);
            ph.getOnlinePlayerSaves().get(player).addSkillPoints(1);
            player.sendTitle(ChatUtil.colorize("&aLEVEL UP!"), ChatUtil.colorize("&7You have gained a skill point"), 10, 40,20);
        } else {
            int expMinecraft = player.getExpToLevel();
            double percent = currentExp / expNeeded;

            int roundedExp = (int) Math.round(expMinecraft * percent);
            player.setExp((float) percent);
        }
    }
}
