package network.palace.listeners;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.util.InventoryNav;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        CPlayer player = Core.getPlayerManager().getPlayer(e.getPlayer());
        if (e.getMaterial().equals(Material.NETHER_STAR)) {
            InventoryNav.openInventory(player);
        }
    }
}