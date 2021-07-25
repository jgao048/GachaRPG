package dev.failures.main.Listeners;

import dev.failures.main.GachaRPG;
import dev.failures.main.Handlers.PlayerHandler;
import dev.failures.main.Storage.DataKeys;
import dev.failures.main.Utils.PDUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobDeathEvent implements Listener {
    private GachaRPG main;

    public MobDeathEvent(GachaRPG main) {
        this.main = main;
    }

    private void playerKillMob(EntityDeathEvent e) {
        if(e.getEntity().getKiller() == null) return;

        Player p = e.getEntity().getKiller();


    }



}
