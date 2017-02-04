package network.palace.lobby.listeners;

import network.palace.lobby.Lobby;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    private Lobby lobby = Lobby.getPlugin(Lobby.class);

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        CPlayer player = Core.getPlayerManager().getPlayer(e.getPlayer());
        if (player.getLocation().getY() <= 0) {
            player.teleport(lobby.getSpawn());
        }
    }
}
