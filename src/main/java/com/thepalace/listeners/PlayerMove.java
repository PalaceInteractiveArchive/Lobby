package com.thepalace.listeners;

import com.thepalace.Lobby;
import com.thepalace.core.Core;
import com.thepalace.core.player.CPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Innectic on 11/18/2016.
 */
public class PlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        CPlayer player = Core.getPlayerManager().getPlayer(e.getPlayer());

        if (player.getLocation().getY() <= 0) {
            player.teleport(new Location(Bukkit.getWorld(Lobby.instance.getConfig().getString("world")),
                    Lobby.instance.getConfig().getInt("x"),
                    Lobby.instance.getConfig().getInt("y"),
                    Lobby.instance.getConfig().getInt("z")));
        }
    }

}
