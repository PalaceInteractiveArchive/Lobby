package com.thepalace.listeners;

import com.thepalace.Lobby;
import com.thepalace.core.events.CorePlayerJoinDelayedEvent;
import com.thepalace.core.player.CPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by Innectic on 11/18/2016.
 */
public class PlayerLogin implements Listener {

    @EventHandler
    public void onLogin(CorePlayerJoinDelayedEvent e) {
        CPlayer player = e.getPlayer();

        player.getHeaderFooter().setHeader(ChatColor.GOLD + "Palace Network - A Family of Servers");
        player.getHeaderFooter().setFooter(ChatColor.LIGHT_PURPLE +  "You're at the " + ChatColor.GOLD + " Lobby");

        player.teleport(new Location(Bukkit.getWorld(Lobby.instance.getConfig().getString("world")),
                Lobby.instance.getConfig().getInt("x"),
                Lobby.instance.getConfig().getInt("y"),
                Lobby.instance.getConfig().getInt("z")));
        player.getLocation().setYaw(Lobby.instance.getConfig().getInt("yaw"));
        player.getLocation().setPitch(Lobby.instance.getConfig().getInt("pitch"));
    }
}
