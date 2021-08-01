package dev.failures.main.commands;

import dev.failures.main.GachaRPG;
import dev.failures.main.handlers.PartyHandler;
import dev.failures.main.handlers.PlayerData;
import dev.failures.main.handlers.PlayerHandler;
import dev.failures.main.storage.TextureValues;
import dev.failures.main.storage.GameValues;
import dev.failures.main.utils.ChatUtil;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PartyCommand implements CommandExecutor{
    private final GachaRPG main;
    private final PlayerHandler playerHandler;
    private final PartyHandler partyHandler;
    private final HashMap<Player, Player> hasInvite = new HashMap<>();

    public PartyCommand(GachaRPG main, PlayerHandler ph, PartyHandler partyh) {
        this.main = main;
        this.playerHandler = ph;
        this.partyHandler = partyh;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        boolean hasParty = partyHandler.hasParty(p);
        boolean isLeader = partyHandler.isLeader(p);


        if(args.length == 0) {
            if(!checkHasParty(p, hasParty)) return false;
            mainPartyGUI(p);
            return true;
        } else if(args[0].equalsIgnoreCase("create")) {
            if(hasParty) {
                p.sendMessage(ChatUtil.colorize("&7You are currently in a party."));
                return false;
            }
            partyHandler.createParty(p);
            p.sendMessage(ChatUtil.colorize("&aYou have successfully created a party."));
            return true;
        } else if(args[0].equalsIgnoreCase("invite")) {
            if(args.length < 2) {
                p.sendMessage(ChatUtil.colorize("&7Please enter a player's name."));
                return false;
            }
            if(!checkHasParty(p, hasParty)) return false;
            if(!checkHasPermission(p, isLeader)) return false;
            if(!checkIfPlayer(p, args[1])) return false;
            if(partyHandler.getPartySize(p) >= GameValues.MAX_PARTY_SIZE) {
                p.sendMessage(ChatUtil.colorize("&7Your party is full."));
                return false;
            }

            Player invitedPlayer = Bukkit.getPlayer(args[1]);

            if(partyHandler.hasParty(invitedPlayer) || partyHandler.isLeader(invitedPlayer)) {
                p.sendMessage(ChatUtil.colorize("&f" + invitedPlayer.getName() + " &7is already in a party."));
                return false;
            } else if(hasInvite.containsKey(invitedPlayer)) {
                p.sendMessage(ChatUtil.colorize("&7Player already has a party invite."));
                return false;
            }
            hasInvite.put(invitedPlayer, p);
            Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                @Override
                public void run() {
                    if(!hasInvite.containsKey(invitedPlayer)) return;
                    hasInvite.remove(invitedPlayer);
                    invitedPlayer.sendMessage(ChatUtil.colorize("&7Your party invite from &f" + p.getName() + " &7has expired"));
                }
            }, 20*10);
            p.sendMessage(ChatUtil.colorize("&7You have invited &f" + invitedPlayer.getName() + " &7to your party."));
            ChatUtil.msg(invitedPlayer,"&7You have been invited to &f" + p.getName() + "&7's party. Type '/party accept' to accept.");
            return true;
        } else if(args[0].equalsIgnoreCase("accept")) {
            if(isLeader || hasParty) {
                p.sendMessage(ChatUtil.colorize("&7You are currently in a party."));
                return false;
            }
            if(hasInvite.containsKey(p)) {
                Player leader = hasInvite.get(p);
                partyHandler.addPartyMember(leader, p);
                for(Player member:  partyHandler.getPartyMembers(leader)) {
                    member.sendMessage(ChatUtil.colorize("&f" + p.getName() + " &7has joined the party."));
                }
                hasInvite.remove(p);
                return true;
            }
            p.sendMessage(ChatUtil.colorize("&7You don't have any party invites."));
            return false;
        } else if(args[0].equalsIgnoreCase("kick")) {
            if(!checkHasParty(p, hasParty)) return false;
            if(!checkHasPermission(p, isLeader)) return false;
            if(args.length < 2) {
                p.sendMessage(ChatUtil.colorize("&7Please enter a player's name."));
                return false;
            }
            if(!checkIfPlayer(p, args[1])) return false;
            Player kickedPlayer = Bukkit.getPlayer(args[1]);

            if (!partyHandler.getLeader(kickedPlayer).equals(p)) {
                p.sendMessage(ChatUtil.colorize("&7That player is not in your party."));
                return false;
            }
            partyHandler.removePartyMember(kickedPlayer);
            ChatUtil.msg(kickedPlayer,"&7You have been kicked from &f" + p.getName() + "&7's party." );
            partyHandler.sendMessageToParty(p, "&f" + kickedPlayer.getName() + " &7has been kicked from the party.", true);

            return true;
        } else if(args[0].equalsIgnoreCase("disband")) {
            if(!checkHasParty(p, hasParty)) return false;
            if(!checkHasPermission(p, isLeader)) return false;
            partyHandler.sendMessageToParty(p, "&f" + p.getName() + " &7has disbanded your party.", true);
            partyHandler.disbandParty(p);
            return true;
        }
        if(!checkHasParty(p, hasParty)) return false;
        mainPartyGUI(p);
        return true;
    }

    private boolean checkIfPlayer(Player p, String name) {
        Player invitedPlayer = Bukkit.getPlayer(name);
        if(invitedPlayer == null) {
            p.sendMessage(ChatUtil.colorize("&7Please try again, we could not find that player."));
            return false;
        } else if(invitedPlayer.equals(p)) {
            p.sendMessage(ChatUtil.colorize("&7Let's not be stupid."));
            return false;
        }
        return true;
    }

    private boolean checkHasParty(Player p, boolean hasParty) {
        if(!hasParty) {
            p.sendMessage(ChatUtil.colorize("&7Please create a party to continue."));
            return false;
        }
        return true;
    }

    private boolean checkHasPermission(Player p, boolean isLeader) {
        if (!isLeader) {
            p.sendMessage(ChatUtil.colorize("&7You are not the leader, you can't kick."));
            return false;
        }
        return true;
    }

    private void mainPartyGUI(Player p) {
        Gui partyMenu = Gui.gui()
                .title(Component.text(ChatUtil.colorize("&0Party Menu")))
                .rows(6)
                .create();

        partyMenu.setItem(1, 1, ItemBuilder.skull()
                .name(Component.text(ChatUtil.colorize("&aRecruit Members")))
                .texture(TextureValues.recruitHead)
                .asGuiItem(event -> {
                    onlineMembersGUI(p);
                }));
        partyMenu.setItem(1, 2, ItemBuilder.skull()
                .name(Component.text(ChatUtil.colorize("&aParty Members")))
                .owner(p)
                .asGuiItem(event -> {
                    onlinePartyGUI(p);
                }));

        partyMenu.setDefaultClickAction(event -> event.setCancelled(true));
        partyMenu.open(p);
    }

    private void onlinePartyGUI(Player p) {
        Gui onlineParty = Gui.gui()
                .title(Component.text(ChatUtil.colorize("&0Party Members")))
                .rows(6)
                .create();
        onlineParty.setDefaultClickAction(event -> event.setCancelled(true));
        Player findLeader = p;
        if(!partyHandler.isLeader(p)) {
            findLeader = partyHandler.getLeader(p);
        }
        for(Player partyMem : partyHandler.getPartyMembers(findLeader)) {
            if(partyMem.equals(findLeader)) {
                onlineParty.addItem(ItemBuilder.skull()
                        .name(Component.text(ChatUtil.colorize("&a" + partyMem.getName())))
                        .enchant(Enchantment.DAMAGE_ALL)
                        .owner(partyMem)
                        .lore(createDataLore(partyMem))
                        .asGuiItem()
                );
                continue;
            }
            onlineParty.addItem(ItemBuilder.skull()
                    .name(Component.text(ChatUtil.colorize("&a" + partyMem.getName())))
                    .owner(partyMem)
                    .lore(createDataLore(partyMem))
                    .asGuiItem()
            );

        }
        onlineParty.open(p);
    }

    private void onlineMembersGUI(Player p) {
        PaginatedGui onlineMembers = Gui.paginated()
                .title(Component.text(ChatUtil.colorize("&0Recruit Members")))
                .rows(6)
                .pageSize(45)
                .create();

        onlineMembers.setDefaultClickAction(event -> event.setCancelled(true));
        if(Bukkit.getOnlinePlayers().size() > 45) {
            onlineMembers.setItem(6, 3, ItemBuilder.from(Material.PAPER).name(Component.text(ChatUtil.colorize("&cPrevious"))).asGuiItem(event -> onlineMembers.previous()));
            onlineMembers.setItem(6, 7, ItemBuilder.from(Material.PAPER).name(Component.text(ChatUtil.colorize("&cNext"))).asGuiItem(event -> onlineMembers.next()));
        }

        for(Player pl : Bukkit.getOnlinePlayers()) {
            if(p.equals(pl)) continue;
            if(partyHandler.hasParty(pl) || partyHandler.isLeader(pl)) continue;
            onlineMembers.addItem(ItemBuilder.skull()
                    .name(Component.text(ChatUtil.colorize("&a" + pl.getName())))
                    .owner(pl)
                    .lore(createDataLore(pl))
                    .asGuiItem(event -> {
                        p.performCommand("party invite " + pl.getName());
                    }));
        }
        onlineMembers.open(p);
    }

    private List<Component> createDataLore(Player p) {
        List<Component> lore = new ArrayList<>();
        PlayerData pData = playerHandler.getOnlinePlayerSaves().get(p);
        lore.add(Component.text(ChatUtil.colorize("&7Level:&f " + pData.getLevel())));
        lore.add(Component.text(ChatUtil.colorize("&7Exp:&f " + pData.getExp())));
        lore.add(Component.text(ChatUtil.colorize("&7Strength:&f " + pData.getStr())));
        lore.add(Component.text(ChatUtil.colorize("&7Agility:&f" + pData.getAgi())));
        lore.add(Component.text(ChatUtil.colorize("&7Intelligence:&f" + pData.getInt())));
        return lore;
    }
}
