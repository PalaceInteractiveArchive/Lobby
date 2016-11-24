package com.thepalace.resourcepack;

import com.palacemc.palacecore.PalaceCore;
import com.palacemc.palacecore.player.User;
import com.palacemc.palacecore.resource.CurrentPackReceivedEvent;
import com.palacemc.palacecore.resource.ResourcePack;
import com.thepalace.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PackManager implements Listener {

    @EventHandler
    public void onCurrentPackReceived(CurrentPackReceivedEvent event) {
        if (!Lobby.getPlugin(Lobby.class).getConfig().getBoolean("packEnabled")) return;
        User user = event.getUser();
        Player player = Bukkit.getPlayer(user.getUniqueId());
        String current = event.getPacks();
        if (!current.equals("Palace")) {
            ResourcePack pack = PalaceCore.resourceManager.getPack("Palace");
            PalaceCore.resourceManager.sendPack(player, pack.getName());
        }
    }
}