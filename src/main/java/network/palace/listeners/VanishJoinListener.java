package network.palace.listeners;

import network.palace.core.Core;
import network.palace.core.player.Rank;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.kitteh.vanish.VanishManager;
import org.kitteh.vanish.VanishPlugin;

public class VanishJoinListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        VanishManager vanishManager = VanishPlugin.getPlugin(VanishPlugin.class).getManager();
        if (!vanishManager.isVanished(e.getPlayer())) {
            if (Core.getPlayerManager().getPlayer(e.getPlayer().getUniqueId()).getRank().getRankId() >= Rank.CHARACTER.getRankId()) {
                vanishManager.toggleVanishQuiet(e.getPlayer().getPlayer(), false);
            }
        }
    }
}
