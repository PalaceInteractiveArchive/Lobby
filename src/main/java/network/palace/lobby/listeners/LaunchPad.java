package network.palace.lobby.listeners;

import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public class LaunchPad implements Listener {

    private ArrayList<UUID> launchCooldown = new ArrayList<>();

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).trim().equalsIgnoreCase("[Launch]")) {
            event.setLine(0, ChatColor.GOLD + "[Launch]");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerMoveEvent event) {
        // Player
        if (event.getPlayer() == null) return;
        Player player = event.getPlayer();
        if (launchCooldown.contains(player.getUniqueId())) return;
        // Pressure plate
        if (player.getLocation().getBlock() == null) return;
        if (!isPlate(player.getLocation().getBlock().getType())) return;
        Block pressurePlate = player.getLocation().getBlock();
        // Block under
        if (pressurePlate.getRelative(BlockFace.DOWN) == null) return;
        Block blockUnder = pressurePlate.getRelative(BlockFace.DOWN);
        // Sign
        if (blockUnder.getRelative(BlockFace.DOWN) == null) return;
        if (!isSign(blockUnder.getRelative(BlockFace.DOWN))) return;
        // Get sign
        Sign sign = (Sign) blockUnder.getRelative(BlockFace.DOWN).getState();
        // Check if launch and valid
        if (sign.getLine(0) == null && sign.getLine(1) == null && sign.getLine(2) == null && sign.getLine(3) == null) return;
        if (!sign.getLine(0).equals(ChatColor.GOLD + "[Launch]")) return;
        if (!checkIfDouble(sign.getLine(1))) return;
        if (!checkIfDouble(sign.getLine(2))) return;
        if (!checkIfDouble(sign.getLine(3))) return;
        // Set vector and send them off
        launchCooldown.add(player.getUniqueId());
        Vector velocity = new Vector(getDouble(sign.getLine(1)),getDouble(sign.getLine(2)), getDouble(sign.getLine(3)));
        player.setVelocity(velocity);
        new BukkitRunnable() {
            @Override
            public void run() {
                launchCooldown.remove(player.getUniqueId());
            }
        }.runTaskLaterAsynchronously(Lobby.getPlugin(Lobby.class), 20);
    }

    private boolean isSign(Block block) {
        return block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST;
    }

    private boolean isPlate(Material material) {
        return material == Material.WOOD_PLATE || material == Material.STONE_PLATE || material == Material.IRON_PLATE || material == Material.GOLD_PLATE;
    }

    private double getDouble(String text) {
        return Double.parseDouble(text);
    }

    private boolean checkIfDouble(String toCheck) {
        try {
            Double.parseDouble(toCheck);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
