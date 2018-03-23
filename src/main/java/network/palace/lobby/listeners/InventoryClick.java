package network.palace.lobby.listeners;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
import network.palace.lobby.ServerInfo;
import network.palace.lobby.util.InventoryNav;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        CPlayer player = Core.getPlayerManager().getPlayer(event.getWhoClicked().getUniqueId());
        if (player == null) return;
        Inventory inv = event.getClickedInventory();
        if (!inv.getTitle().startsWith(ChatColor.BLUE.toString())) return;
        String title = inv.getTitle();
        String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
        if (title.equalsIgnoreCase(InventoryNav.NAV_NAME)) {
            event.setCancelled(true);
            for (ServerInfo s : InventoryNav.SERVERS) {
                if (s.getName().equalsIgnoreCase(name)) {
                    player.sendToServer(s.getLocation());
                    player.sendMessage(ChatColor.GREEN + "Sending you to " + s.getName() + "...");
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        CPlayer player = Core.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        if(player==null)return;
        Lobby.getInstance().getInventoryNav().closeInventory(player.getUniqueId());
    }
}
