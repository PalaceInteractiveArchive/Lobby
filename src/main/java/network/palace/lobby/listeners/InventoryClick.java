package network.palace.lobby.listeners;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClick implements Listener {

//    @EventHandler
//    public void onInventoryClick(InventoryClickEvent event) {
//        CPlayer player = Core.getPlayerManager().getPlayer(event.getWhoClicked().getUniqueId());
//        if (player == null) return;
//        Inventory inv = event.getClickedInventory();
//        if (!inv.getTitle().startsWith(ChatColor.BLUE.toString())) return;
//        String title = inv.getTitle();
//        String name;
//        try {
//            name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
//        } catch (Exception e) {
//            name = "";
//        }
//        if (title.equalsIgnoreCase(InventoryNav.NAV_NAME)) {
//            event.setCancelled(true);
//            for (ServerInfo s : InventoryNav.SERVERS) {
//                if (s.getName().equalsIgnoreCase(name)) {
//                    player.sendMessage(ChatColor.GREEN + "Sending you to " + s.getName() + "...");
//                    player.sendToServer(s.getLocation());
//                    player.closeInventory();
//                    return;
//                }
//            }
//        } else if (title.equalsIgnoreCase(HubSelector.NAV_NAME)) {
//            event.setCancelled(true);
//            for (String s : Lobby.getHubSelector().getHubs().keySet()) {
//                if (s.equalsIgnoreCase(name)) {
//                    player.sendMessage(ChatColor.GREEN + "Sending you to " + name + "...");
//                    player.sendToServer(name);
//                    player.closeInventory();
//                }
//            }
//        }
//    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        CPlayer player = Core.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        if (player == null) return;
        Lobby.getInventoryNav().closeInventory(player.getUniqueId());
        Lobby.getHubSelector().closeInventory(player.getUniqueId());
    }
}
