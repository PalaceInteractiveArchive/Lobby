package com.thepalace.listeners;

import com.palacemc.palacecore.PalaceCore;
import com.palacemc.palacecore.permissions.Rank;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.kitteh.vanish.VanishManager;
import org.kitteh.vanish.VanishPlugin;

public class VanishJoinListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage("");
        VanishManager vanishManager = VanishPlugin.getPlugin(VanishPlugin.class).getManager();
        if (!vanishManager.isVanished(e.getPlayer())) {
            if (PalaceCore.getUser(e.getPlayer().getUniqueId()).getRank().getRankId() >= Rank.CHARACTER.getRankId()) {
                vanishManager.toggleVanishQuiet(e.getPlayer().getPlayer(), false);
            }
        }
    }
}
