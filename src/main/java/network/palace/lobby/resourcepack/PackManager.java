package network.palace.lobby.resourcepack;

import network.palace.core.Core;
import network.palace.core.events.CurrentPackReceivedEvent;
import network.palace.core.player.CPlayer;
import network.palace.core.resource.ResourceStatusEvent;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PackManager implements Listener {

    @EventHandler
    public void onCurrentPackReceived(CurrentPackReceivedEvent event) {
        if (!Lobby.getPlugin(Lobby.class).getConfig().getBoolean("packEnabled")) return;
        String current = event.getPack();
        if (!current.equals("Palace")) {
            Core.getResourceManager().sendPack(event.getPlayer(), Core.getResourceManager().getPack("Palace"));
        }
    }

    @EventHandler
    public void onResourceStatus(ResourceStatusEvent event) {
        CPlayer player = event.getPlayer();
        switch (event.getStatus()) {
            case ACCEPTED:
                player.sendMessage(ChatColor.GREEN + "Resource Pack accepted! Downloading now...");
                break;
            case LOADED:
                player.sendMessage(ChatColor.GREEN + "Resource Pack loaded!");
                break;
            case FAILED:
                player.sendMessage(ChatColor.RED + "Download failed! Please report this to a Staff Member. (Error Code 101)");
                break;
            case DECLINED:
                for (int i = 0; i < 5; i++) {
                    player.sendMessage(" ");
                }
                player.sendMessage(ChatColor.RED + "You have declined the Resource Pack!");
                player.sendMessage(ChatColor.YELLOW + "For help with this, visit: " + ChatColor.AQUA +
                        "https://palace.network/rphelp");
                break;
            default:
                player.sendMessage(ChatColor.RED + "Download failed! Please report this to a Staff Member. (Error Code 101)");
        }
    }
}