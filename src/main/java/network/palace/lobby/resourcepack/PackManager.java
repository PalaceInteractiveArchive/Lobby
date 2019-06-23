package network.palace.lobby.resourcepack;

import network.palace.core.Core;
import network.palace.core.events.CurrentPackReceivedEvent;
import network.palace.core.message.FormattedMessage;
import network.palace.core.player.CPlayer;
import network.palace.core.resource.ResourceStatusEvent;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PackManager implements Listener {

    @EventHandler
    public void onCurrentPackReceived(CurrentPackReceivedEvent event) {
        if (Lobby.getConfigUtil().getServerPack() != null)
            Core.getResourceManager().sendPack(event.getPlayer(), Lobby.getConfigUtil().getServerPack());
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
            case DECLINED:
                player.sendMessage(ChatColor.RED + "You have declined the Resource Pack!");
                break;
            default: {
                if (player.getRegistry().hasEntry("packDownloadURL")) {
                    String url = (String) player.getRegistry().getEntry("packDownloadURL");
                    new FormattedMessage("Download failed! ").color(ChatColor.RED)
                            .then("You can download the pack manually by clicking ").color(ChatColor.AQUA)
                            .then("here").color(ChatColor.YELLOW).style(ChatColor.UNDERLINE).link(url).send(player);
                } else {
                    player.sendMessage(ChatColor.RED + "Download failed!");
                    player.sendMessage(ChatColor.YELLOW + "For help with this, visit: " + ChatColor.AQUA +
                            "https://palnet.us/rphelp");
                }
            }
        }
    }
}