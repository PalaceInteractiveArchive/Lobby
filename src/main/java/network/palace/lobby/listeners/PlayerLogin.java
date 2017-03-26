package network.palace.lobby.listeners;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.core.player.Rank;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerLogin implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onLogin(PlayerJoinEvent e) {
        Core.runTaskLater(() -> {
            CPlayer player = Core.getPlayerManager().getPlayer(e.getPlayer());
            player.resetPlayer();
            Lobby lobby = Lobby.getPlugin(Lobby.class);

            player.teleport(lobby.getSpawn());

            player.getHeaderFooter().setHeader(ChatColor.GOLD + "Palace Network - A Family of Servers");
            player.getHeaderFooter().setFooter(ChatColor.LIGHT_PURPLE + "You're at the " + ChatColor.GOLD + lobby.getConfig().getString("serverName"));

            player.getInventory().clear();
            lobby.getInventoryNav().giveNav(player);
            player.setGamemode(GameMode.ADVENTURE);
            if (player.getRank().getRankId() >= Rank.SPECIALGUEST.getRankId()) {
                player.setAllowFlight(true);
                player.setFlying(true);
            } else if (Lobby.getPlugin(Lobby.class).getConfig().getBoolean("flightForDonorsEnabled") &&
                    player.getRank().getRankId() >= Rank.DWELLER.getRankId()) {
                player.setAllowFlight(true);
                player.setFlying(true);
            }
            if (lobby.getConfig().getBoolean("titleEnabled")) {
                player.getActionBar().show(ChatColor.LIGHT_PURPLE + "Use your Nether Star to navigate!");
            }
        }, 10L);
    }
}
