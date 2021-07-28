package dev.failures.main.listeners;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PartyHandler;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.storage.Values;
import dev.failures.main.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
        if(e.getEntity().getKiller() == null) return;
        Player p = e.getEntity().getKiller();
        e.setDroppedExp(0);

        for(Player partyMem : partyH.getNearbyMembers(p,10,e.getEntity())) {
            ph.getOnlinePlayerSaves().get(partyMem).addExp(Values.EXP_DROP * Values.EXP_SHARED);
            updateExpBar(partyMem);
        }

        ph.getOnlinePlayerSaves().get(p).addExp(Values.EXP_DROP);
        ph.getOnlinePlayerSaves().get(p).addGold(10);
        updateExpBar(p);
    }

    @EventHandler
    private void giveDeathExp(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        p.sendMessage("Respawn boy!");
        int level = ph.getOnlinePlayerSaves().get(p).getLevel();
        p.setLevel(level);
        updateExpBar(p);
    }

    private void updateExpBar(Player player) {
        int currentLevel = ph.getOnlinePlayerSaves().get(player).getLevel();
        double currentExp = ph.getOnlinePlayerSaves().get(player).getExp();
        double expNeeded = Values.BASE_EXP_NEEDED * Math.pow(Values.EXP_GROWTH,currentLevel-1);

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
