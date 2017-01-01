package network.palace.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Innectic
 * @since 1/1/2017
 */
public class PlayerJoinLeave implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        e.setQuitMessage("");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage("");
    }
}
