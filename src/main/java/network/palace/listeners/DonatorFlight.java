package network.palace.listeners;

import network.palace.Lobby;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.core.player.Rank;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class DonatorFlight implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        if (!Lobby.getPlugin(Lobby.class).getConfig().getBoolean("flightForDonorsEnabled")) return;
        CPlayer player = Core.getPlayerManager().getPlayer(e.getPlayer());
        if (player.getRank().getRankId() >= Rank.DWELLER.getRankId()) {
            e.getPlayer().setAllowFlight(true);
            e.getPlayer().setFlying(true);
        }
    }
}
