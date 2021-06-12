package network.palace.lobby.listeners;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.cosmetics.Cosmetics;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        CPlayer player = Core.getPlayerManager().getPlayer(event.getPlayer());
        if (player == null) return;
        switch (event.getMaterial()) {
            case NETHER_STAR:
                Lobby.getInventoryNav().openInventory(player);
                break;
            case BOOK:
                Lobby.getHubSelector().openInventory(player);
                break;
            case ENDER_CHEST:
                try {
                    player.openInventory(Cosmetics.getInventoryUtil().getMainInventory(player));
                } catch (NoClassDefFoundError e) {
                    player.sendMessage(ChatColor.RED + "There's an error loading Cosmetics right now, sorry!");
                }
                break;
            case IRON_PLATE:
                if (Lobby.getParkourUtil().checkInParkour(player)) {
                    Location userTp = Lobby.getParkourUtil().getUserCheckpoint(player);
                    player.teleport(userTp);
                    event.setUseInteractedBlock(Event.Result.DENY);
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
