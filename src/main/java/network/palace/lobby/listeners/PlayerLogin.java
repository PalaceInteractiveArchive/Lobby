package network.palace.lobby.listeners;

import network.palace.core.events.CorePlayerJoinedEvent;
import network.palace.core.player.CPlayer;
import network.palace.core.player.Rank;
import network.palace.lobby.Lobby;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLogin implements Listener {

    @EventHandler
    public void onLogin(CorePlayerJoinedEvent event) {
        CPlayer player = event.getPlayer();

        player.resetPlayer();
        if (Lobby.getConfigUtil().getSpawn() != null) player.teleport(Lobby.getConfigUtil().getSpawn());
        player.setGamemode(GameMode.ADVENTURE);
        Lobby.getInventoryUtil().handleJoin(player);

        if (player.getRank().getRankId() >= Rank.SHAREHOLDER.getRankId()) player.setAllowFlight(true);
    }
}
