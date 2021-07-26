package dev.failures.main.listeners;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PlayerData;
import dev.failures.main.handlers.PlayerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class LevelSystem implements Listener {
    private GachaRPG main;
    PlayerHandler ph;

    public LevelSystem(GachaRPG main, PlayerHandler ph) {
        this.main = main;
        this.ph = ph;
    }

    @EventHandler
    private void mobExpDrop(EntityDeathEvent e) {
        if(e.getEntity().getKiller() == null) return;
        Player p = e.getEntity().getKiller();

        //method 1
        //PlayerData data = ph.getOnlinePlayerSaves().get(p);
        //data.addExp(100);
        //ph.getOnlinePlayerSaves().put(p, data);

        ph.getOnlinePlayerSaves().get(p).addExp(100); //method 2

    }
}
