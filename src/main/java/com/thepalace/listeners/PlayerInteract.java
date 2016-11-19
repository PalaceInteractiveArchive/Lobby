package com.thepalace.listeners;

import com.thepalace.ServerPortStar;
import com.thepalace.core.Core;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.STONE_PLATE)) return;
        ServerPortStar.openPortStar(Core.getPlayerManager().getPlayer(event.getPlayer()));
    }
}
