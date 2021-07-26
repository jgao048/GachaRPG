package dev.failures.main.listeners;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PlayerData;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.storage.Values;
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
        e.setDroppedExp(0);

        ph.getOnlinePlayerSaves().get(p).addExp(10);
        ph.getOnlinePlayerSaves().get(p).addGold(10);

        int currentLevel = ph.getOnlinePlayerSaves().get(p).getLevel();
        double currentExp = ph.getOnlinePlayerSaves().get(p).getExp();
        double expNeeded = Values.BASE_EXP_NEEDED * Math.pow(Values.EXP_GROWTH,currentLevel-1);

        if(currentExp >= expNeeded) {
            p.setLevel(currentLevel+1);
            p.setExp(0);
            ph.getOnlinePlayerSaves().get(p).addLevel(1);
            ph.getOnlinePlayerSaves().get(p).setExp(0);

        } else {
            int expMinecraft = p.getExpToLevel();
            double percent = currentExp / expNeeded;

            int roundedExp = (int) Math.round(expMinecraft * percent);
            p.setExp((float) percent);
        }
        /*
        //PlayerData data = ph.getOnlinePlayerSaves().get(p);
        //data.addExp(100);
        //ph.getOnlinePlayerSaves().put(p, data);
        */
    }
}
