package com.thepalace.listeners;

import com.thepalace.ServerPortStar;
import com.thepalace.core.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.PHYSICAL)) return;
        ServerPortStar.openPortStar(Core.getPlayerManager().getPlayer(event.getPlayer()));
    }
}
