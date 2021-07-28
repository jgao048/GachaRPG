package dev.failures.main.handlers;

import dev.failures.main.storage.MongoDB;
import dev.failures.main.storage.Values;
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
    private void addPlayer(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        CompletableFuture<PlayerData> cf = db.getData(p);
        cf.whenComplete((resultSet, exception) -> {
            if(resultSet == null) {
                p.sendMessage("null?");
                onlinePlayerSaves.put(p, new PlayerData(1, 0, 100, 0, 10, 10, 10, 10));
                p.setLevel(1);
                p.setExp(0);
            } else {
                onlinePlayerSaves.put(p, resultSet);
            }

            int str = onlinePlayerSaves.get(p).getStr();
            double healthCalc = (str*Values.HEATLH_PER_STR) + 20;
            if(p.getHealth() != healthCalc) p.setHealth(healthCalc);

            p.sendMessage("Regen: " + p.getSaturatedRegenRate());
            p.sendMessage("Speed: " + p.getWalkSpeed());

            int mana = onlinePlayerSaves.get(p).getInt();
            int speed = onlinePlayerSaves.get(p).getAgi();
            int regen = onlinePlayerSaves.get(p).getVit();
        });
    }

    @EventHandler
    private void removePlayer(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        db.saveData(onlinePlayerSaves.remove(p),p.getUniqueId().toString());
    }

    public HashMap<Player, PlayerData> getOnlinePlayerSaves() {
        return onlinePlayerSaves;
    }
}
