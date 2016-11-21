package com.thepalace.listeners;

import com.palacemc.palacecore.PalaceCore;
import com.palacemc.palacecore.permissions.Rank;
import com.thepalace.Lobby;
import com.thepalace.core.Core;
import com.thepalace.core.events.CorePlayerJoinDelayedEvent;
import com.thepalace.core.player.CPlayer;
import com.thepalace.util.Inventory;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Innectic on 11/18/2016.
 */
public class PlayerLogin implements Listener {

    @EventHandler
    public void onLogin(CorePlayerJoinDelayedEvent e) {
        e.setJoinMessage("");

        if (Lobby.instance.vanishManager.isVanished(e.getPlayer().getBukkitPlayer())) {
            if (PalaceCore.getUser(e.getPlayer().getUuid()).getRank().getRankId() >= Rank.CHARACTER.getRankId()) {
                Lobby.instance.vanishManager.vanish(e.getPlayer().getBukkitPlayer(), true, false);
            }
        }

        CPlayer player = e.getPlayer();

        player.teleport(new Location(Bukkit.getWorld(Lobby.instance.getConfig().getString("world")),
                Lobby.instance.getConfig().getInt("x"),
                Lobby.instance.getConfig().getInt("y"),
                Lobby.instance.getConfig().getInt("z"),
                Core.getPluginInstance(Lobby.class).getConfig().getInt("yaw"),
                Core.getPluginInstance(Lobby.class).getConfig().getInt("pitch")));

        player.getHeaderFooter().setHeader(ChatColor.GOLD + "Palace Network - A Family of Servers");
        player.getHeaderFooter().setFooter(ChatColor.LIGHT_PURPLE +  "You're at the " + ChatColor.GOLD + " Lobby");

        player.getInventory().clear();
        player.getInventory().setItem(4, Inventory.nameItem(new ItemStack(Material.NETHER_STAR), "Navigation"));
        player.setGamemode(GameMode.ADVENTURE);
        player.getTitle().show("",
                ChatColor.LIGHT_PURPLE + "Use your Nether Star to navigate!", 10, 60, 10);
    }
}
