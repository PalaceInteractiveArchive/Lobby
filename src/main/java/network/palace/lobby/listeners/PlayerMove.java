package network.palace.lobby.listeners;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
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
        if (player != null && player.getLocation().getY() <= 0) {
            player.teleport(Lobby.getConfigUtil().getSpawn());
            return;
        }
        Location from = e.getFrom();
        Location to = e.getTo();
        if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
            Block b = to.getBlock().getRelative(BlockFace.DOWN);
            if (b.getType().equals(Material.EMERALD_BLOCK)) {
                player.setVelocity(player.getLocation().getDirection().multiply(3).setY(1));
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT,0.4f,0.75f);
            }
        }
    }
}
