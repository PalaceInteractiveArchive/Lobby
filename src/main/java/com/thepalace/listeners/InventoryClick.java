package com.thepalace.listeners;

import com.thepalace.core.Core;
import com.thepalace.core.player.CPlayer;
import com.thepalace.util.InventoryNav;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        CPlayer player = Core.getPlayerManager().getPlayer((Player) e.getWhoClicked());
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().hasItemMeta()) {
            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(InventoryNav.NAV_NAME)) {
                e.setCancelled(true);
            }
        }
        if (e.getClickedInventory() == null) return;
        String inventoryName = e.getClickedInventory().getName();
        if (inventoryName == null || inventoryName.trim().isEmpty()) return;
        if (!inventoryName.equals(InventoryNav.NAV_NAME)) return;
        e.setCancelled(true);
        InventoryNav.sendToServer(player, e.getCurrentItem());
    }
}
