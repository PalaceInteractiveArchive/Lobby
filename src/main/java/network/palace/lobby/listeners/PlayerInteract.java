package network.palace.lobby.listeners;

import network.palace.chairs.events.PlayerEnterChairEvent;
import network.palace.chairs.events.PlayerLeaveChairEvent;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.cosmetics.Cosmetics;
import network.palace.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerEnterBed(PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerEnterChair(PlayerEnterChairEvent event) {
        CPlayer player = event.getPlayer();
        Location loc = event.getChair();
        if (player.getRegistry().hasEntry("tutorial_start")) {
            if (player.getRegistry().hasEntry("tutorial_sit_chair") && loc.getBlockX() == 63 && loc.getBlockY() == 72 && loc.getBlockZ() == 19) {
                player.getRegistry().removeEntry("tutorial_sit_chair");
                event.setCancelled(false);
                if (player.getRegistry().hasEntry("tutorial_items")) {
                    // last discussion
                    Lobby.getTutorialManager().startLastTableDiscussion(player);
                } else {
                    // first discussion
                    Lobby.getTutorialManager().startTableDiscussion(player);
                }
            } else {
                if (player.getRegistry().hasEntry("tutorial_items")) {
                    player.sendMessage(ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "You don't have all three items yet. Keep looking!");
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerLeaveChair(PlayerLeaveChairEvent event) {
        CPlayer player = event.getPlayer();
        if (player.getRegistry().hasEntry("tutorial_start")) {
            if (player.getRegistry().hasEntry("tutorial_leave_table")) {
                player.getRegistry().removeEntry("tutorial_leave_table");
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        CPlayer player = Core.getPlayerManager().getPlayer(event.getPlayer());
        if (player == null) return;

        if (player.getRegistry().hasEntry("tutorial_start")) {
            Block b = event.getClickedBlock();
            if (b != null && !b.getType().equals(Material.AIR) && player.getRegistry().hasEntry("tutorial_items")) {
                Location loc = b.getLocation();
                if (player.getRegistry().hasEntry("tutorial_compass") && loc.getBlockX() == 63 && loc.getBlockY() == 73 && loc.getBlockZ() == 12) {
                    player.getRegistry().removeEntry("tutorial_compass");
                    player.sendMessage(ChatColor.GREEN + "You picked up a Navigation Compass!");
                    player.getInventory().setItem(8, Lobby.getInventoryUtil().getCompass());
                } else if (player.getRegistry().hasEntry("tutorial_book") && loc.getBlockX() == 62 && loc.getBlockY() == 75 && loc.getBlockZ() == 25) {
                    player.getRegistry().removeEntry("tutorial_book");
                    player.sendMessage(ChatColor.GREEN + "You picked up an Adventure Log!");
                    player.getInventory().setItem(7, Lobby.getInventoryUtil().getAdventureLog());
                } else if (player.getRegistry().hasEntry("tutorial_bag") && loc.getBlockX() == 57 && loc.getBlockY() == 72 && loc.getBlockZ() == 17) {
                    player.getRegistry().removeEntry("tutorial_bag");
                    player.sendMessage(ChatColor.GREEN + "You picked up a Loot Bag!");
                    player.getInventory().setItem(6, Lobby.getInventoryUtil().getLootBag());
                }
                if (!player.getRegistry().hasEntry("tutorial_compass") && !player.getRegistry().hasEntry("tutorial_book") &&
                        !player.getRegistry().hasEntry("tutorial_bag") && !player.getRegistry().hasEntry("tutorial_sit_chair")) {
                    player.sendMessage(ChatColor.GREEN + "You have everything! Go finish talking with Elred.");
                    player.getRegistry().addEntry("tutorial_sit_chair", true);
                }
            }
            event.setCancelled(true);
            return;
        }

        if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.EMERALD_BLOCK)) {
            Location loc = event.getClickedBlock().getLocation();
            Bukkit.getLogger().info("new Location(w, " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")");
            return;
        }
        switch (event.getMaterial()) {
            case COMPASS:
                Lobby.getInventoryUtil().openCompassMenu(player);
//                Lobby.getInventoryNav().openInventory(player);
                break;
            case BOOK:
//                Lobby.getHubSelector().openInventory(player);
                break;
            case ENDER_CHEST:
                try {
                    player.openInventory(Cosmetics.getInventoryUtil().getMainInventory(player));
                } catch (NoClassDefFoundError e) {
                    player.sendMessage(ChatColor.RED + "There's an error loading Cosmetics right now, sorry!");
                }
                break;
            default:
                /*PlayerInventory inv = player.getInventory();
                if (inv.getHeldItemSlot() == 2 && inv.getItem(2).getType() != null) {
                    try {
                        new ToyUseEvent(player, false).call();
                    } catch (NoClassDefFoundError ignored) {
                    }
                }*/
        }
    }
}
