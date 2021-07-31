package dev.failures.main.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dev.failures.main.handlers.DungeonHandler;
import dev.failures.main.handlers.PartyHandler;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.utils.ChatUtil;

public class DungeonCommand implements CommandExecutor {
    private PlayerHandler playerHandler;
    private PartyHandler partyHandler;
    private DungeonHandler dungeonHandler;
    private ArrayList<String> dungeonNames = new ArrayList<>();;

    public DungeonCommand(PlayerHandler playerHandler, PartyHandler partyHandler, DungeonHandler dungeonHandler) {
        this.playerHandler = playerHandler;
        this.partyHandler = partyHandler;
        this.dungeonHandler = dungeonHandler;
        dungeonNames.add("dungeon1");
        dungeonNames.add("dungeon2");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        // if (args.length == 0) {
        //     return true;
        // }
        if (args[0].equals("init")) {
            if (args.length == 1) {
                p.sendMessage(ChatUtil.text("Initialized worlds"));
                dungeonHandler.initalizeDungeons(dungeonNames);
            }
            else {
                p.sendMessage(ChatUtil.text("No other arguments"));
            }
            return false;
        }
        else if (args[0].equals("create")) {
            if (args.length == 2) {
                p.sendMessage(ChatUtil.text("Create a world named: " + args[1]));
                dungeonHandler.generateWorld(args[1], p);
            }
            else {
                p.sendMessage(ChatUtil.text("You need a dungeon name"));
            }
            return false;
        }
        else if (args[0].equals("load")) {
            if (args.length == 2) {
                p.sendMessage(ChatUtil.text("Loaded a world named: " + args[1]));
                dungeonHandler.loadWorld(args[1]);
            }
            else {
                p.sendMessage(ChatUtil.text("You need a dungeon name"));
            }
            return false;
        }
        else if (args[0].equals("unload")) {
            if (args.length == 2) {
                p.sendMessage(ChatUtil.text("Unloaded a world named: " + args[1]));
                dungeonHandler.unloadWorld(args[1]);
            }
            else {
                p.sendMessage(ChatUtil.text("You need a dungeon name"));
            }
            return false;
        }
        else if (args[0].equals("delete")) {
            if (args.length == 2) {
                p.sendMessage(ChatUtil.text("Deleted a world named: " + args[1]));
                dungeonHandler.deleteWorld(args[1]);
            }
            else {
                p.sendMessage(ChatUtil.text("You need a dungeon name"));
            }
            return false;
        }
        return false;
    }

}
