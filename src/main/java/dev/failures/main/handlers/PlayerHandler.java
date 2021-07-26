package dev.failures.main.handlers;

import dev.failures.main.GachaRPG;
import dev.failures.main.storage.MongoDB;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.ResultSet;
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
        if(!e.getPlayer().hasPlayedBefore()) {
            onlinePlayerSaves.put(e.getPlayer(), new PlayerData(1,0,100,0));
            return;
        }
        CompletableFuture<PlayerData> cf = db.getData(e.getPlayer());
        cf.whenComplete((resultSet, exception) -> {
            onlinePlayerSaves.put(e.getPlayer(), resultSet);
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
