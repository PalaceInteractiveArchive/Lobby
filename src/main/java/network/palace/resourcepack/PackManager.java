package network.palace.resourcepack;

import network.palace.Lobby;
import network.palace.core.Core;
import network.palace.core.events.CurrentPackReceivedEvent;
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
}