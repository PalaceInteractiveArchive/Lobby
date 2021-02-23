package network.palace.lobby.listeners;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class PlayerMove implements Listener {
    private static final RegionManager regionManager = WorldGuardPlugin.inst().getRegionManager(Bukkit.getWorlds().get(0));

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        CPlayer player = Core.getPlayerManager().getPlayer(e.getPlayer());
        if (player == null) return;
        if (player.getLocation().getY() <= 0) {
            player.teleport(Lobby.getConfigUtil().getSpawn());
            return;
        }
        Location from = e.getFrom();
        Location to = e.getTo();
        if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
//            if (player.getRegistry().hasEntry("tutorial_start")) {
//                ApplicableRegionSet set = regionManager.getApplicableRegions(to);
//                if (player.getRegistry().hasEntry("tutorial_first_scene")) {
//                    boolean leftScene = true;
//                    for (ProtectedRegion r : set.getRegions()) {
//                        if (r.getId().equals("tutorial_first_scene")) {
//                            leftScene = false;
//                            break;
//                        }
//                    }
//                    if (leftScene) {
//                        e.setCancelled(true);
//                        if (!player.getRegistry().hasEntry("tutorial_first_scene_leave_message_delay") || (System.currentTimeMillis() - ((long) player.getRegistry().getEntry("tutorial_first_scene_leave_message_delay")) >= 0)) {
//                            player.getRegistry().addEntry("tutorial_first_scene_leave_message_delay", System.currentTimeMillis() + 5000);
//                            player.sendMessage(ChatColor.GRAY + "Man: " + ChatColor.WHITE + "Hold on! We should talk first before you head out there.");
//                        }
//                    }
//                }
//            } else {
            Block b = to.getBlock().getRelative(BlockFace.DOWN);
            if (b.getType().equals(Material.EMERALD_BLOCK)) {
                player.setVelocity(player.getLocation().getDirection().multiply(3).setY(1));
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.4f, 0.75f);
            }
//            }
        }
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        CPlayer player = Core.getPlayerManager().getPlayer(event.getPlayer());
        if (player == null) return;
        if (player.getRegistry().hasEntry("tutorial_start")) event.setCancelled(true);
    }
}
