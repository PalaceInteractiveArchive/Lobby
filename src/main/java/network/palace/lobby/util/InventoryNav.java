package network.palace.lobby.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.core.utils.ItemUtil;
import network.palace.lobby.ServerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class InventoryNav {
    public static final Material NAV_MATERIAL = Material.NETHER_STAR;
    public static final String NAV_NAME = ChatColor.BLUE + "Navigation";
    public static final int NAV_SIZE = 27;
    public static final ServerInfo[] SERVERS = {
            new ServerInfo("Creative", 11, Material.DIAMOND_PICKAXE, "lobby.nav.creative"),
            new ServerInfo("Theme Parks", "TTC", 13, Material.STICK, "lobby.nav.parks"),
            new ServerInfo("Arcade", 15, Material.BOW, "lobby.nav.arcade")
    };
    private int parks = 0;
    private int creative = 0;
    private int arcade = 0;
    private HashMap<UUID, Inventory> openInventories = new HashMap<>();
    private boolean green = true;

    public InventoryNav() {
        Core.runTaskTimer(() -> {
            try {
                green = !green;
                for (Map.Entry<UUID, Inventory> entry : openInventories.entrySet()) {
                    UUID uuid = entry.getKey();
                    CPlayer player = Core.getPlayerManager().getPlayer(uuid);
                    if (player != null && player.getOpenInventory().isPresent()) {
                        Inventory inv = entry.getValue();
                        for (int i = 0; i < inv.getSize(); i++) {
                            ItemStack item = inv.getItem(i);
                            if (item == null) continue;
                            ItemMeta meta = item.getItemMeta();
                            if (meta == null) continue;
                            List<String> lore = meta.getLore();
                            if (lore == null || lore.isEmpty()) continue;
                            int count = 0;
                            switch (ChatColor.stripColor(meta.getDisplayName().toLowerCase())) {
                                case "theme parks": {
                                    count = parks;
                                    break;
                                }
                                case "creative": {
                                    count = creative;
                                    break;
                                }
                                case "arcade": {
                                    count = arcade;
                                    break;
                                }
                            }
                            List<String> newLore = new ArrayList<>();
                            for (int i2 = 0; i2 < lore.size(); i2++) {
                                String s = lore.get(i2);
                                if (i2 >= (lore.size() - 1)) {
                                    newLore.add(ChatColor.GRAY + "" + count + " currently online");
                                    continue;
                                }
                                String replaced = s.replaceAll((green ? ChatColor.BLACK : ChatColor.GREEN) + "➤",
                                        (green ? ChatColor.GREEN : ChatColor.BLACK) + "➤");
                                newLore.add(replaced);
                            }
                            meta.setLore(newLore);
                            item.setItemMeta(meta);
                            inv.setItem(i, item);
                            int windowId = player.getWindowId();

                            PacketContainer packet = new PacketContainer(PacketType.Play.Server.SET_SLOT);
                            StructureModifier<Integer> mod = packet.getIntegers();
                            mod.write(0, windowId);
                            mod.write(1, i);
                            packet.getItemModifier().write(0, item);
                            ProtocolLibrary.getProtocolManager().sendServerPacket(player.getBukkitPlayer(), packet);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0L, 10L);
    }

    public void giveNav(CPlayer player) {
        ItemStack itemNav = ItemUtil.create(NAV_MATERIAL, NAV_NAME);
        player.getInventory().setItem(4, ItemUtil.makeUnableToMove(itemNav));
    }

    public void openInventory(CPlayer player) {
        Inventory inv = Bukkit.createInventory(player.getBukkitPlayer(), NAV_SIZE, NAV_NAME);
        for (ServerInfo info : SERVERS) {
            int count = -1;
            switch (info.getName().toLowerCase()) {
                case "theme parks": {
                    count = parks;
                    break;
                }
                case "creative": {
                    count = creative;
                    break;
                }
                case "arcade": {
                    count = arcade;
                    break;
                }
            }
            String description = Core.getLanguageFormatter().getFormat(player, info.getDescription());
            List<String> descList = size(description, ChatColor.GRAY);
            descList.add(0, " ");
            descList.add("  ");
            descList.add((green ? ChatColor.GREEN : ChatColor.BLACK) + "➤ " + ChatColor.GREEN + "/join " + info.getLocation());
            if (count >= 0) {
                descList.add(ChatColor.GRAY + "" + count + " currently online");
            }
            ItemStack item = ItemUtil.create(info.getItem(), ChatColor.GREEN + info.getName(), descList);
            item = ItemUtil.hideAttributes(item);
            inv.setItem(info.getPosition(), item);
        }
        player.openInventory(inv);
        openInventories.put(player.getUniqueId(), inv);
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
                    if ((current.length() + s1.length() + 1) <= 35) {
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
            if (current.length() >= 35 || (current.length() + s.length() + 1) >= 35) {
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

    private String finalize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s.trim());
    }

    public void closeInventory(UUID uuid) {
        openInventories.remove(uuid);
    }

    public void updateCounts(int parks, int creative, int arcade) {
        this.parks = parks;
        this.creative = creative;
        this.arcade = arcade;
    }
}
