package com.thepalace.listeners;

import com.palacemc.palacecore.PalaceCore;
import com.palacemc.palacecore.dashboard.packets.dashboard.PacketSendToServer;
import com.thepalace.core.Core;
import com.thepalace.core.player.CPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by Innectic on 11/18/2016.
 */
public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        CPlayer player = Core.getPlayerManager().getPlayer((Player) e.getWhoClicked());

        if (e.getCurrentItem() != null) {
            if (e.getCursor() != null) {
                if (e.getInventory().getName() == "Navigation") {
                    e.setCancelled(true);
                    if (e.getSlot() == 2) {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.GREEN + "Sending you to Parks...");
                        PalaceCore.getInstance().dashboardConnection.send(new PacketSendToServer(player.getUuid(), "TTC1"));
                    } else if (e.getSlot() == 3) {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.GREEN + "Sending you to Creative...");
                        PalaceCore.getInstance().dashboardConnection.send(new PacketSendToServer(player.getUuid(), "Creative"));
                    } else if (e.getSlot() == 5) {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.GREEN + "Sending you to Arcade...");
                        PalaceCore.getInstance().dashboardConnection.send(new PacketSendToServer(player.getUuid(), "Arcade"));
                    } else if (e.getSlot() == 6) {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.GREEN + "Sending you to Hub...");
                        PalaceCore.getInstance().dashboardConnection.send(new PacketSendToServer(player.getUuid(), "Hub1"));
                    }
                }
            }
        }
    }
}
