package dev.failures.main.handlers;

import dev.failures.main.utils.ChatUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PartyHandler {
    private final HashMap<Player, ArrayList<Player>> parties = new HashMap<>();
    private final HashMap<Player, Player> memberLeaders = new HashMap<>();

    public void addPartyMember(Player leader, Player member) {
        ArrayList<Player> partyMembers = getPartyMembers(leader);
        partyMembers.add(member);
        parties.put(leader, partyMembers);
        memberLeaders.put(member, leader);
    }

    public void removePartyMember(Player member) {
        Player leaderOfMember = memberLeaders.get(member);
        getPartyMembers(leaderOfMember).remove(member);
        memberLeaders.remove(member);
    }

    public void createParty(Player leader) {
        ArrayList<Player> partyMembers = new ArrayList<>();
        partyMembers.add(leader);
        parties.put(leader, partyMembers);
    }

    public void sendMessageToParty(Player leader, String message, boolean toLeader) {
        for(Player member: getPartyMembers(leader)) {
            if(member.equals(leader) && !toLeader) continue;
            ChatUtil.msg(member, message);
        }
    }

    public void disbandParty(Player leader) {
        ArrayList<Player> members = getPartyMembers(leader);
        for(Player member:members) {
            if(member.equals(leader)) continue;
            memberLeaders.remove(member);
        }
        parties.remove(leader);
    }

    public ArrayList<Player> getNearbyMembers(Player killer, int radius, Entity killedMob) {
        List<Entity> nearbyKilled = killedMob.getNearbyEntities(radius, radius, radius);

        ArrayList<Player> partyMembers = new ArrayList<>();
        if(isLeader(killer)) {
            partyMembers = getPartyMembers(killer);
        } else if(hasParty(killer)) {
            partyMembers = getPartyMembers(getLeader(killer));
        }

        ArrayList<Player> nearbyPartyMembers = new ArrayList<>();
        for(Entity entity: nearbyKilled) {
            if(!(entity instanceof Player)) continue;
            Player nearbyPlayer = (Player) entity;
            if(nearbyPlayer.equals(killer)) continue;
            if(partyMembers.contains(nearbyPlayer)) nearbyPartyMembers.add(nearbyPlayer);
        }
        return nearbyPartyMembers;
    }

    public ArrayList<Player> getPartyMembers(Player leader) {
        return parties.get(leader);
    }

    public int getPartySize(Player leader) {
        return parties.get(leader).size();
    }

    public Player getLeader(Player member) {
        return memberLeaders.get(member);
    }

    public boolean hasParty(Player player) {
        return memberLeaders.containsKey(player) || isLeader(player);
    }

    public boolean isLeader(Player player) {
        return parties.containsKey(player);
    }

    public HashMap<Player, ArrayList<Player>> getParties() {
        return parties;
    }

    public HashMap<Player, Player> getLeaders() {
        return memberLeaders;
    }

}
