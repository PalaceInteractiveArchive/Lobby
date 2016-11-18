package com.thepalace.listeners;

import com.thepalace.ServerPortStar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDrop implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (ServerPortStar.ifItemIsPortStar(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }
}
