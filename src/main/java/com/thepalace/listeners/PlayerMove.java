package com.thepalace.listeners;

import com.palacemc.core.Core;
import com.palacemc.core.player.CPlayer;
import com.thepalace.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        CPlayer player = Core.getPlayerManager().getPlayer(e.getPlayer());
        if (player.getLocation().getY() <= 0) {
            Lobby lobby = Lobby.getPlugin(Lobby.class);
            player.teleport(new Location(Bukkit.getWorld(lobby.getConfig().getString("world")),
                    lobby.getConfig().getInt("x"),
                    lobby.getConfig().getInt("y"),
                    lobby.getConfig().getInt("z"),
                    lobby.getConfig().getInt("yaw"),
                    lobby.getConfig().getInt("pitch")));
        }
    }
}
