package dev.failures.main.listeners;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PartyHandler;
import dev.failures.main.utils.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PartyListeners implements Listener {
    private GachaRPG main;
    private PartyHandler ph;

    public PartyListeners(GachaRPG main, PartyHandler ph) {
        this.main = main;
        this.ph = ph;
    }

    @EventHandler
    private void partyMemberQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        boolean hasParty = ph.hasParty(p);
        boolean isLeader = ph.isLeader(p);
        if(isLeader) {
            ArrayList<Player> members = ph.getParties().remove(p);
            for(Player member: members) {
                if(p.equals(member)) continue;
                member.sendMessage(ChatUtil.colorize("&7Your leader has disconnected, party has been disbanded."));
                ph.getLeaders().remove(member);
            }
        } else if(hasParty) {
            Player leader = ph.getLeader(p);
            ArrayList<Player> members = ph.getPartyMembers(leader);
            ph.removePartyMember(p);
            for(Player member : members) {
                member.sendMessage(ChatUtil.colorize("&f" + p.getName() + " &7has logged off and kicked from the party."));
            }
        }
    }


}
