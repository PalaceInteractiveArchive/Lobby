package network.palace.lobby.listeners;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.cosmetics.Cosmetics;
import network.palace.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        CPlayer player = Core.getPlayerManager().getPlayer(event.getPlayer());
        if (player == null) return;
        if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.EMERALD_BLOCK)) {
            Location loc = event.getClickedBlock().getLocation();
            Bukkit.broadcastMessage("new Location(w, " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")");
            return;
        }
        switch (event.getMaterial()) {
            case COMPASS:
                Lobby.getInventoryUtil().openCompassMenu(player);
//                Lobby.getInventoryNav().openInventory(player);
                break;
            case BOOK:
//                Lobby.getHubSelector().openInventory(player);
                break;
            case ENDER_CHEST:
                try {
                    player.openInventory(Cosmetics.getInventoryUtil().getMainInventory(player));
                } catch (NoClassDefFoundError e) {
                    player.sendMessage(ChatColor.RED + "There's an error loading Cosmetics right now, sorry!");
                }
                break;
            default:
                /*PlayerInventory inv = player.getInventory();
                if (inv.getHeldItemSlot() == 2 && inv.getItem(2).getType() != null) {
                    try {
                        new ToyUseEvent(player, false).call();
                    } catch (NoClassDefFoundError ignored) {
                    }
                }*/
        }
    }
}
