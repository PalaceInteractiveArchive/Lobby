package network.palace.lobby.listeners;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.cosmetics.Cosmetics;
import network.palace.lobby.Lobby;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        CPlayer player = Core.getPlayerManager().getPlayer(e.getPlayer());
        if (e.getMaterial().equals(Material.NETHER_STAR)) {
            e.setCancelled(true);
            Lobby.getInstance().getInventoryNav().openInventory(player);
        } else if (e.getMaterial().equals(Material.BOOK)) {
            e.setCancelled(true);
            Lobby.getInstance().getHubSelector().openInventory(player);
        } else if (e.getMaterial().equals(Material.ENDER_CHEST)) {
            e.setCancelled(true);
            player.openInventory(Cosmetics.getInventoryUtil().getMainInventory(player));
        }
    }
}
