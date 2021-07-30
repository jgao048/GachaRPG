package dev.failures.main.handlers;

import dev.failures.main.storage.GameValues;
import dev.failures.main.storage.MongoDB;
import dev.failures.main.utils.ChatUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class PlayerHandler implements Listener {
    private final HashMap<Player, PlayerData> onlinePlayerSaves = new HashMap<>();
    MongoDB db;

    public PlayerHandler(MongoDB db) {
        this.db = db;
    }


    @EventHandler
    private void addPlayerToOnlineSave(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        CompletableFuture<PlayerData> cf = db.getData(p);
        cf.whenComplete((resultSet, exception) -> {
            if(resultSet == null) {
                onlinePlayerSaves.put(p, new PlayerData(1, 0, 100, 0, 10, 10, 10, 10));
                p.setLevel(1);
                p.setExp(0);
            } else {
                onlinePlayerSaves.put(p, resultSet);
            }
            updatePlayerStats(p);
        });
    }

    @EventHandler
    private void savePlayerToDatabase(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        db.saveData(onlinePlayerSaves.remove(p),p.getUniqueId().toString());
    }

    public HashMap<Player, PlayerData> getOnlinePlayerSaves() {
        return onlinePlayerSaves;
    }

    public void updatePlayerStats(Player p) {
        int str = onlinePlayerSaves.get(p).getStr();
        double healthCalc = onlinePlayerSaves.get(p).getCurrentHealth();
        if(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() != healthCalc) {
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(healthCalc);
        }

        int agi = onlinePlayerSaves.get(p).getAgi();
        double speedCalc = getOnlinePlayerSaves().get(p).getCurrentSpeed();
        if(p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() != speedCalc) {
            p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speedCalc);
        }

        int mana = onlinePlayerSaves.get(p).getInt();

        int regen = onlinePlayerSaves.get(p).getVit();
        int regenCalc = getOnlinePlayerSaves().get(p).getCurrentRegenHP();
        if(p.getUnsaturatedRegenRate() != regenCalc) p.setUnsaturatedRegenRate(regenCalc);
        if(p.getSaturatedRegenRate() != regenCalc) p.setSaturatedRegenRate(regenCalc); //don't know why this isn't used but ill set in case :)
    }

    public void updatePlayerExp(Player player) {
        int currentLevel = onlinePlayerSaves.get(player).getLevel();
        double currentExp = onlinePlayerSaves.get(player).getExp();
        double expNeeded = GameValues.BASE_EXP_NEEDED * Math.pow(GameValues.EXP_GROWTH,currentLevel-1);

        if(currentExp >= expNeeded) {
            int remainder = (int) (currentExp - expNeeded);
            int newLevel = currentLevel+1;
            player.setLevel(newLevel);
            player.setExp(0);
            onlinePlayerSaves.get(player).addLevel(1);
            onlinePlayerSaves.get(player).setExp(remainder);
            onlinePlayerSaves.get(player).addSkillPoints(1);
            player.sendTitle(ChatUtil.colorize("&aLEVEL UP!"), ChatUtil.colorize("&7You have gained a skill point"), 10, 40,20);
        } else {
            int expMinecraft = player.getExpToLevel();
            double percent = currentExp / expNeeded;

            int roundedExp = (int) Math.round(expMinecraft * percent);
            player.setExp((float) percent);
        }
    }
}
