package network.palace.lobby.listeners;

import network.palace.core.events.CorePlayerJoinedEvent;
import network.palace.core.player.CPlayer;
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

        CPlayer player = e.getPlayer();

        player.resetPlayer();
        player.teleport(lobby.getSpawn());

        Lobby.getInstance().setPlayerHeader(player);

        player.getInventory().clear();

        lobby.getInventoryNav().giveNav(player);
        lobby.getHubSelector().giveNav(player);
        player.getInventory().setItem(0, Lobby.getCosmeticsItem());

        player.setGamemode(GameMode.ADVENTURE);
        if (player.getRank().getRankId() >= Rank.SPECIALGUEST.getRankId()) {
            player.setAllowFlight(true);
            player.setFlying(true);
        } else if (Lobby.getPlugin(Lobby.class).getConfig().getBoolean("flightForDonorsEnabled") && player.getRank().getRankId() >= Rank.DWELLER.getRankId()) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
        if (lobby.getConfig().getBoolean("titleEnabled")) {
            player.getActionBar().show(ChatColor.LIGHT_PURPLE + "Use your Nether Star to navigate!");
        }
    }
}
