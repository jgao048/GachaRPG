package dev.failures.main.listeners;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PlayerData;
import dev.failures.main.storage.MongoDB;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class CreateProfileEvent implements Listener {
    private final GachaRPG main;
    private final MongoDB db;

    public CreateProfileEvent(GachaRPG main, MongoDB db) { this.main = main; this.db = db; }

    @EventHandler
    private void playerJoin(AsyncPlayerPreLoginEvent e) {
        String pid = e.getUniqueId().toString();
        PlayerData player = new PlayerData(1, 0, 100, 0);
        db.saveData(player, pid);
    }
}
