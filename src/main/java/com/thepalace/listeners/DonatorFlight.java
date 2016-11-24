package com.thepalace.listeners;

import com.palacemc.palacecore.PalaceCore;
import com.palacemc.palacecore.permissions.Rank;
import com.palacemc.palacecore.player.User;
import com.thepalace.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class DonatorFlight implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        if (!Lobby.getPlugin(Lobby.class).getConfig().getBoolean("flightForDonorsEnabled")) return;
        User user = PalaceCore.getUser(e.getPlayer().getUniqueId());
        if (user.getRank().getRankId() >= Rank.DWELLER.getRankId()) {
            e.getPlayer().setAllowFlight(true);
            e.getPlayer().setFlying(true);
        }
    }
}
