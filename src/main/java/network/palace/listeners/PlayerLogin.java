package network.palace.listeners;

import network.palace.Lobby;
import network.palace.core.events.CorePlayerJoinDelayedEvent;
import network.palace.core.player.CPlayer;
import network.palace.util.InventoryNav;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLogin implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onLogin(CorePlayerJoinDelayedEvent e) {
        e.setJoinMessage("");
        CPlayer player = e.getPlayer();

        Lobby lobby = Lobby.getPlugin(Lobby.class);
        player.teleport(new Location(Bukkit.getWorld(lobby.getConfig().getString("world")),
                lobby.getConfig().getInt("x"),
                lobby.getConfig().getInt("y"),
                lobby.getConfig().getInt("z"),
                lobby.getConfig().getInt("yaw"),
                lobby.getConfig().getInt("pitch")));

        player.getHeaderFooter().setHeader(ChatColor.GOLD + "Palace Network - A Family of Servers");
        player.getHeaderFooter().setFooter(ChatColor.LIGHT_PURPLE +  "You're at the " + ChatColor.GOLD + lobby.getConfig().getString("serverName"));

        player.getInventory().clear();
        InventoryNav.giveNav(player);
        player.setGamemode(GameMode.ADVENTURE);
        if (lobby.getConfig().getBoolean("titleEnabled")) {
            player.getTitle().show("", ChatColor.LIGHT_PURPLE + "Use your Nether Star to navigate!", 10, 60, 10);
        }
    }
}
