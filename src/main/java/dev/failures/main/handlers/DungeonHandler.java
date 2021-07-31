package dev.failures.main.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class DungeonHandler {
    private final HashMap<Player, MultiverseWorld> activeDungeons = new HashMap<>();
    private final MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
    
    HashMap<String, Location> playerSpawns = new HashMap<>();
    HashMap<String, ArrayList<Location>> mobSpawns = new HashMap<>();
    PlayerHandler playerHandler;
    PartyHandler partyHandler;
    MVWorldManager worldManager = core.getMVWorldManager();

    public DungeonHandler(PlayerHandler playerHandler, PartyHandler partyHandler) {
        this.playerHandler = playerHandler;
        this.partyHandler = partyHandler;
    }

    public void initalizeDungeons(ArrayList<String> dungeonNames) {
        for (String name : dungeonNames) {
            worldManager.addWorld(
                name, // The worldname
                World.Environment.NORMAL, // The overworld environment type.
                null, // The world seed. Any seed is fine for me, so we just pass null.
                WorldType.FLAT, // Nothing special. If you want something like a flat world, change this.
                false, // This means we want to structures like villages to generator, Change to false if you don't want this.
                null // String for plugin. Specifies a custom generator. We are not using any so we just pass null.
            );
        }
    }

    public void generateWorld(String dungeonName, Player p) {
        Player leader = partyHandler.getLeader(p);
        worldManager.cloneWorld(dungeonName, leader.getName().toString());
        activeDungeons.put(leader, worldManager.getMVWorld(leader.getName().toString()));
    }

    public void pasteMap(World dungeon) {

    }

    public void activateDungeon(World dungeon) {

    }

    public void resetDungeon(World dungeon) {

    }

    public void saveWorlds() {
        worldManager.saveWorldsConfig();
    }

    public void loadWorld(String dungeonName) {
        worldManager.loadWorld(dungeonName);
    }

    public void unloadWorld(String dungeonName) {
        worldManager.unloadWorld(dungeonName);
    }

    public void deleteWorld(String dungeonName) {
        worldManager.deleteWorld(dungeonName);
    }

    public void spawnMobs(MultiverseWorld dungeon, int amount, LivingEntity mobs) {

    }

    public Location getSpawnPoint(World dungeon) {
        return new Location(dungeon, 0f, 0f, 0f);
    }

    public World getDungeonWorld(Player p){
        return null;
    }

    public HashMap<Player, MultiverseWorld> getActiveDungeons() {
        return activeDungeons;
    }
    
    public int getPlayersAlive(World dungeon) {
        return 0;
    }

}
