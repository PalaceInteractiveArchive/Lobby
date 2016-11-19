package com.thepalace.resourcepack;

import com.palacemc.palacecore.PalaceCore;
import com.palacemc.palacecore.player.User;
import com.palacemc.palacecore.resource.CurrentPackReceivedEvent;
import com.palacemc.palacecore.resource.ResourcePack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by Marc on 11/18/16
 */
public class PackManager implements Listener {

    @EventHandler
    public void onCurrentPackReceived(CurrentPackReceivedEvent event) {
        User user = event.getUser();
        final Player player = Bukkit.getPlayer(user.getUniqueId());
        String current = event.getPacks();
        String preferred = user.getPreferredPack();
        if (!current.equals("Palace")) {
            ResourcePack pack = PalaceCore.resourceManager.getPack("Palace");
            PalaceCore.resourceManager.sendPack(player, pack.getName());
        }
    }
}