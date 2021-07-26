package dev.failures.main.handlers;

import dev.failures.main.storage.MongoDB;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayerHandler implements Listener {
    private final HashMap<Player, PlayerData> onlinePlayerSaves = new HashMap<>();
    MongoDB db;

    public PlayerHandler(MongoDB db) {
        this.db = db;
    }

    @EventHandler
    private void addPlayer(PlayerJoinEvent e) {
        onlinePlayerSaves.put(e.getPlayer(), db.getData(e.getPlayer()));
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
