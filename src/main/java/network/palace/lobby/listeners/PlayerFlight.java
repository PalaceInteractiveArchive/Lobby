package network.palace.lobby.listeners;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.core.player.Rank;
import network.palace.lobby.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerFlight implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        CPlayer player = Core.getPlayerManager().getPlayer(e.getPlayer());
        if (player.getRank().getRankId() >= Rank.SPECIALGUEST.getRankId()) {
            e.getPlayer().setAllowFlight(true);
            e.getPlayer().setFlying(true);
        }
        if (!Lobby.getPlugin(Lobby.class).getConfig().getBoolean("flightForDonorsEnabled")) return;
        if (player.getRank().getRankId() >= Rank.DWELLER.getRankId()) {
            e.getPlayer().setAllowFlight(true);
            e.getPlayer().setFlying(true);
        }
    }
}
