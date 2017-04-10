package network.palace.lobby.listeners;

import network.palace.core.events.CorePlayerJoinedEvent;
import network.palace.core.player.Rank;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLogin implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onLogin(CorePlayerJoinedEvent e) {
        Lobby lobby = Lobby.getPlugin(Lobby.class);

        e.getPlayer().resetPlayer();
        e.getPlayer().teleport(lobby.getSpawn());

        e.getPlayer().getHeaderFooter().setHeader(ChatColor.GOLD + "Palace Network - A Family of Servers");
        e.getPlayer().getHeaderFooter().setFooter(ChatColor.LIGHT_PURPLE + "You're at the " + ChatColor.GOLD + lobby.getConfig().getString("serverName"));

        e.getPlayer().getInventory().clear();
        lobby.getInventoryNav().giveNav(e.getPlayer());
        e.getPlayer().setGamemode(GameMode.ADVENTURE);
        if (e.getPlayer().getRank().getRankId() >= Rank.SPECIALGUEST.getRankId()) {
            e.getPlayer().setAllowFlight(true);
            e.getPlayer().setFlying(true);
        } else if (Lobby.getPlugin(Lobby.class).getConfig().getBoolean("flightForDonorsEnabled") && e.getPlayer().getRank().getRankId() >= Rank.DWELLER.getRankId()) {
            e.getPlayer().setAllowFlight(true);
            e.getPlayer().setFlying(true);
        }
        if (lobby.getConfig().getBoolean("titleEnabled")) {
            e.getPlayer().getActionBar().show(ChatColor.LIGHT_PURPLE + "Use your Nether Star to navigate!");
        }
    }
}
