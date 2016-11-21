package com.thepalace.listeners;

import com.thepalace.core.Core;
import com.thepalace.core.player.CPlayer;
import com.thepalace.util.Inventory;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Innectic on 11/18/2016.
 */
public class PlayerInteract implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        CPlayer player = Core.getPlayerManager().getPlayer(e.getPlayer());

        if (e.getMaterial().equals(Material.NETHER_STAR)) {
            Inventory.openInventory(player);
        }
    }
}
