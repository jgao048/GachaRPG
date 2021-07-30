package dev.failures.main.listeners;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PartyHandler;
import dev.failures.main.storage.MessageValues;
import dev.failures.main.utils.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PartyListeners implements Listener {
    private GachaRPG main;
    private PartyHandler partyHandler;

    public PartyListeners(GachaRPG main, PartyHandler partyHandler) {
        this.main = main;
        this.partyHandler = partyHandler;
    }

    @EventHandler
    private void partyMemberQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if(partyHandler.isLeader(p)) {
            ArrayList<Player> members = partyHandler.getParties().remove(p);
            for(Player member: members) {
                if(p.equals(member)) continue;
                member.sendMessage(MessageValues.LEADER_LEAVE);
                partyHandler.getLeaders().remove(member);
            }
        } else if(partyHandler.hasParty(p)) {
            Player leader = partyHandler.getLeader(p);
            ArrayList<Player> members = partyHandler.getPartyMembers(leader);
            partyHandler.removePartyMember(p);
            for(Player member : members) {
                member.sendMessage(MessageValues.PLAYER_LEAVE.replace("%name%", p.getName()));
            }
        }
    }


}
