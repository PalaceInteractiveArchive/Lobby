package network.palace.lobby.listeners;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        CPlayer player = Core.getPlayerManager().getPlayer(e.getPlayer());
        if (player != null && player.getLocation().getY() <= 0) player.teleport(Lobby.getConfigUtil().getSpawn());
    }
}
