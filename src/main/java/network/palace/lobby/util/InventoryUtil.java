package network.palace.lobby.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import network.palace.core.Core;
import network.palace.core.menu.Menu;
import network.palace.core.menu.MenuButton;
import network.palace.core.player.CPlayer;
import network.palace.core.utils.ItemUtil;
import network.palace.lobby.Lobby;
import org.bukkit.*;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class InventoryUtil {
    private final List<UUID> joinList = new ArrayList<>();
    @Getter private final ItemStack lootBag;
    @Getter private final ItemStack compass;
    @Getter private final ItemStack adventureLog;
    private final List<CPlayer> popList = new ArrayList<>();

    public InventoryUtil() {
        Core.runTaskTimer(Lobby.getInstance(), () -> {
            if (joinList.isEmpty()) return;
            List<UUID> list = new ArrayList<>(joinList);
            joinList.clear();
            list.forEach(uuid -> {
                CPlayer player = Core.getPlayerManager().getPlayer(uuid);
                if (player == null) return;
                setInventory(player);
            });
        }, 0L, 10L);
        Core.runTaskTimer(Lobby.getInstance(), () -> {
            for (CPlayer tp : popList) {
                tp.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, tp.getLocation().add(0, 1.5, 0), 10, 0.05, 0.4, 0.05, 0.1);
                tp.getWorld().playSound(tp.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 1f);
            }
            popList.clear();
        }, 0L, 1L);

        compass = ItemUtil.create(Material.COMPASS, ChatColor.GREEN + "Navigation Compass",
                Arrays.asList("", ChatColor.GRAY + "Find your way around " + ChatColor.LIGHT_PURPLE + "Ravelia", ""));
        adventureLog = ItemUtil.create(Material.BOOK, ChatColor.GREEN + "Adventure Log",
                Arrays.asList("", ChatColor.GRAY + "Keep a log of all of your adventures", ""));
        lootBag = ItemUtil.create(Material.ENDER_CHEST, ChatColor.GREEN + "Loot Bag",
                Arrays.asList("", ChatColor.GRAY + "Store the items you find on your adventures", ""));
    }

    /**
     * Handle inventory setting when a player joins
     *
     * @param player the player
     */
    public void handleJoin(CPlayer player) {
        joinList.add(player.getUniqueId());
    }

    /**
     * Set the player's inventory with the proper items
     *
     * @param player the player
     */
    public void setInventory(CPlayer player) {
        player.getInventory().setContents(new ItemStack[]{null, null, null, null, null, null, lootBag, adventureLog, compass});
    }

    public void openCompassMenu(CPlayer player) {
        List<MenuButton> buttons = Arrays.asList(
                new MenuButton(10, getMenuItem(player, Material.BOAT_SPRUCE, ChatColor.AQUA + "Creative Island Port", "hub.compass.creative"), ImmutableMap.of(ClickType.LEFT, p -> {
                    p.sendMessage(ChatColor.GREEN + "Traveling to the Creative Island Port...");
                    p.teleport(new Location(p.getWorld(), 11.5, 64, 84.5, -10, 5));
                    popList.add(player);
                })),
                new MenuButton(12, getMenuItem(player, Material.MINECART, ChatColor.LIGHT_PURPLE + "Theme Park Portals", "hub.compass.parks"), ImmutableMap.of(ClickType.LEFT, p -> {
                    p.sendMessage(ChatColor.GREEN + "Traveling to the Theme Park portals...");
                    p.teleport(new Location(p.getWorld(), -23.5, 68, 3.5, 85, -5));
                    popList.add(player);
                })),
                new MenuButton(14, getMenuItem(player, Material.JACK_O_LANTERN, ChatColor.YELLOW + "Seasonal Festival Path", "hub.compass.seasonal"), ImmutableMap.of(ClickType.LEFT, p -> {
                    p.sendMessage(ChatColor.GREEN + "Traveling to the Seasonal Festival path...");
                    p.teleport(new Location(p.getWorld(), -18.5, 68, 24.5, 35, -5));
                    popList.add(player);
                })),
                new MenuButton(16, getMenuItem(player, Material.NETHER_STAR, ChatColor.GOLD + "Mini Games", "hub.compass.games"), ImmutableMap.of(ClickType.LEFT, p -> p.sendMessage(ChatColor.GREEN + "Traveling to Camp Alto..."))),
                new MenuButton(28, getMenuItem(player, Material.WATCH, ChatColor.DARK_AQUA + "Treetop Parkour", "hub.compass.parkour"), ImmutableMap.of(ClickType.LEFT, p -> p.sendMessage(ChatColor.GREEN + "Traveling to the Treetop Parkour..."))),
                new MenuButton(31, getMenuItem(player, Material.EMERALD, ChatColor.GREEN + "Central Marketplace", "hub.compass.marketplace"), ImmutableMap.of(ClickType.LEFT, p -> {
                    p.sendMessage(ChatColor.GREEN + "Traveling to the Central Marketplace...");
                    p.teleport(new Location(p.getWorld(), 54, 61, 60, -92, -10));
                    popList.add(player);
                })),
                new MenuButton(34, ItemUtil.create(Material.BOOK, ChatColor.GREEN + "Alternate Hubs"), ImmutableMap.of(ClickType.LEFT, p -> p.sendMessage(ChatColor.GREEN + "Open hub menu")))
        );
        Menu menu = new Menu(45, ChatColor.BLUE + "Navigation Compass", player, buttons);
        menu.open();
    }

    private ItemStack getMenuItem(CPlayer player, Material type, String name, String languageCode) {
        String description = Core.getLanguageFormatter().getFormat(player, languageCode);
        List<String> descList = size(description, ChatColor.GRAY);
        descList.add(0, " ");
        return ItemUtil.create(type, name, descList);
    }

    private List<String> size(String message, ChatColor color) {
        List<String> list = new ArrayList<>();
        String[] words = message.split(" ");
        StringBuilder current = new StringBuilder();
        for (String s : words) {
            if (s.contains("\n")) {
                String[] newLine = s.split("\n");
                String s1 = newLine[0];
                StringBuilder s2 = new StringBuilder();
                for (int i = 1; i < newLine.length; i++) {
                    s2.append(newLine[i]);
                }
                if (current.length() > 0) {
                    if ((noCodes(current).length() + noCodes(s1).length() + 1) <= 35) {
                        current.append(" ").append(s1);
                        list.add(color + finalize(current.toString()));
                        current = new StringBuilder();
                    } else {
                        list.add(color + finalize(current.toString()));
                        current = new StringBuilder();
                        current.append(s1);
                    }
                } else {
                    current.append(s1);
                }
                current.append(" ").append(s2);
                continue;
            }
            if (noCodes(current).length() >= 35 || (noCodes(current).length() + noCodes(s).length() + 1) >= 35) {
                list.add(color + finalize(current.toString()));
                current = new StringBuilder();
            }
            current.append(" ").append(s);
        }
        if (current.length() > 0) {
            list.add(color + finalize(current.toString()));
        }
        return list;
    }

    private String noCodes(String s) {
        return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', s));
    }

    private String noCodes(StringBuilder s) {
        return noCodes(s.toString());
    }

    private String finalize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s.trim());
    }

    public static void sendInventoryUpdate(CPlayer player, int windowId, int slot, ItemStack item) throws InvocationTargetException {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SET_SLOT);
        StructureModifier<Integer> mod = packet.getIntegers();
        mod.write(0, windowId);
        mod.write(1, slot);
        packet.getItemModifier().write(0, item);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player.getBukkitPlayer(), packet);
    }
}
