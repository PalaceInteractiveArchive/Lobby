package com.thepalace.listeners;

import com.palacemc.palacecore.PalaceCore;
import com.palacemc.palacecore.permissions.Rank;
import com.thepalace.Lobby;
import com.thepalace.core.Core;
import com.thepalace.core.events.CorePlayerJoinDelayedEvent;
import com.thepalace.core.player.CPlayer;
import com.thepalace.util.InventoryNav;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.kitteh.vanish.VanishManager;
import org.kitteh.vanish.VanishPlugin;

public class PlayerLogin implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onLogin(CorePlayerJoinDelayedEvent e) {
        e.setJoinMessage("");

        VanishManager vanishManager = VanishPlugin.getPlugin(VanishPlugin.class).getManager();
        if (!vanishManager.isVanished(e.getPlayer().getBukkitPlayer())) {
            if (PalaceCore.getUser(e.getPlayer().getUuid()).getRank().getRankId() >= Rank.CHARACTER.getRankId()) {
                vanishManager.toggleVanishQuiet(e.getPlayer().getBukkitPlayer(), false);
            }
        }

        CPlayer player = e.getPlayer();

        Lobby lobby = Lobby.getPlugin(Lobby.class);
        player.teleport(new Location(Bukkit.getWorld(lobby.getConfig().getString("world")),
                lobby.getConfig().getInt("x"),
                lobby.getConfig().getInt("y"),
                lobby.getConfig().getInt("z"),
                lobby.getConfig().getInt("yaw"),
                lobby.getConfig().getInt("pitch")));

        player.getHeaderFooter().setHeader(ChatColor.GOLD + "Palace Network - A Family of Servers");
        player.getHeaderFooter().setFooter(ChatColor.LIGHT_PURPLE +  "You're at the " + ChatColor.GOLD + " Lobby");

        player.getInventory().clear();
        player.getInventory().setItem(4, InventoryNav.nameItem(new ItemStack(Material.NETHER_STAR), "Navigation"));
        player.setGamemode(GameMode.ADVENTURE);
        player.getTitle().show("",
                ChatColor.LIGHT_PURPLE + "Use your Nether Star to navigate!", 10, 60, 10);
    }
}
