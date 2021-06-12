package network.palace.lobby.listeners;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
import network.palace.lobby.parkour.ParkourCountingTask;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

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
            Block b = to.getBlock().getRelative(BlockFace.DOWN);
            Block above = to.getBlock().getRelative(BlockFace.SELF);
            if (b.getType().equals(Material.EMERALD_BLOCK)) {
                player.setVelocity(player.getLocation().getDirection().multiply(3).setY(1));
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.4f, 0.75f);
            }
            if (b.getType().equals(Material.PISTON_BASE) && above.getType().equals(Material.GOLD_PLATE)) {
                if (!Lobby.getParkourUtil().checkInParkour(player)) {
                    Lobby.getParkourUtil().addToParkour(player);
                    player.sendMessage(ChatColor.AQUA + "Started the parkour! Aim for the quickest time!");
                    Lobby.getParkourUtil().setUserCheckpoint(player, b.getLocation());
                    player.getBukkitPlayer().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 0);
                    new ParkourCountingTask(player).runTaskTimerAsynchronously(Lobby.getInstance(), 0, 20);
                }
            }
            if (b.getType().equals(Material.PISTON_BASE) && above.getType().equals(Material.IRON_PLATE)) {
                if (Lobby.getParkourUtil().checkInParkour(player)) {
                    Lobby.getParkourUtil().setUserCheckpoint(player, player.getLocation());
                    player.getBukkitPlayer().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 0);
                    player.sendMessage(ChatColor.AQUA + "You hit a checkpoint!");
                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.ITALIC + "Pushing right click on the metal pressure plate in your inventory will teleport you back!");
                } else {
                    player.sendMessage(ChatColor.AQUA + "This is a parkour checkpoint! Head to the start to begin the parkour.");
                }
            }
            if (b.getType().equals(Material.PISTON_STICKY_BASE) && above.getType().equals(Material.GOLD_PLATE)) {
                if (Lobby.getParkourUtil().checkInParkour(player)) {
                    Lobby.getParkourUtil().removeFromParkour(player, true);
                    player.getBukkitPlayer().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 0);
                } else {
                    player.sendMessage(ChatColor.AQUA + "This is the parkour end! Head to the start to begin the parkour.");
                }
            }
        }
        if (player.isFlying() && Lobby.getParkourUtil().checkInParkour(player)) {
            if (Lobby.getParkourUtil().checkInParkour(player)) {
                Lobby.getParkourUtil().removeFromParkour(player, false);
                player.sendMessage(ChatColor.RED + "You flew so were removed from the parkour!");
                player.getBukkitPlayer().playSound(player.getLocation(), Sound.ENTITY_PLAYER_DEATH, 0.5f, 0);
            }
        }
    }
}
