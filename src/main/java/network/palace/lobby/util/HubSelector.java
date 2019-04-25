package network.palace.lobby.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import lombok.Getter;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.core.utils.ItemUtil;
import network.palace.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author Innectic
 * @since 4/11/2017
 */
public class HubSelector {

    public static final Material NAV_MATERIAL = Material.BOOK;
    public static final String NAV_NAME = ChatColor.BLUE + "Hub Selector";
    public static final int NAV_SIZE = 27;
    private HashMap<UUID, Inventory> openInventories = new HashMap<>();
    @Getter private HashMap<String, Integer> hubs = new HashMap<>();
    private boolean green = true;

    public HubSelector() {
        hubs.put(Core.getInstanceName(), 0);
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
                            if (meta == null || meta.getDisplayName() == null) continue;
                            List<String> lore = meta.getLore();
                            if (lore == null || lore.isEmpty()) continue;
                            if (hubs == null || hubs.isEmpty()) continue;
                            int count = hubs.get(ChatColor.stripColor(meta.getDisplayName()));
                            List<String> newLore = new ArrayList<>();
                            for (int i2 = 0; i2 < lore.size(); i2++) {
                                String s = lore.get(i2);
                                if (i2 >= (lore.size() - 1)) {
                                    newLore.add(ChatColor.GRAY + "" + count + " players");
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

    private void sendToHub(CPlayer player, String server) {
        if (server == null || player == null) return;

        player.sendToServer(server);
        player.sendMessage(ChatColor.RED + "Sending you to " + ChatColor.BLUE + server);
    }

    public void openInventory(CPlayer player) {
        Inventory inv = Bukkit.createInventory(player.getBukkitPlayer(), NAV_SIZE, NAV_NAME);
        int pos = 10;
        for (Map.Entry<String, Integer> entry : hubs.entrySet()) {
            try {
                List<String> lore = Arrays.asList(" ",
                        (green ? ChatColor.GREEN : ChatColor.BLACK) + "➤ " + ChatColor.GREEN + "Switch to " + entry.getKey(),
                        ChatColor.GRAY + "" + entry.getValue() + " players");
                ItemStack hub = ItemUtil.create(Material.CHISELED_QUARTZ_BLOCK, ChatColor.GREEN + entry.getKey(), lore);
                inv.setItem(pos, hub);

                int windowId = player.getWindowId();
                PacketContainer packet = new PacketContainer(PacketType.Play.Server.SET_SLOT);
                StructureModifier<Integer> mod = packet.getIntegers();
                mod.write(0, windowId);
                mod.write(1, pos);
                packet.getItemModifier().write(0, hub);
                ProtocolLibrary.getProtocolManager().sendServerPacket(player.getBukkitPlayer(), packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
            pos += 1;
            if ((pos + 2) % 9 == 0) {
                pos += 3;
            }
        }
        player.openInventory(inv);
        openInventories.put(player.getUniqueId(), inv);
    }

    public void giveNav(CPlayer player) {
        Lobby plugin = Lobby.getPlugin(Lobby.class);
        if (plugin.getConfig().getBoolean("canSelectLobbies", false)) {
            ItemStack itemNav = ItemUtil.create(NAV_MATERIAL, NAV_NAME);
            player.getInventory().setItem(8, ItemUtil.makeUnableToMove(itemNav));
        }
    }

    public void closeInventory(UUID uuid) {
        openInventories.remove(uuid);
    }

    public void updateCounts(HashMap<String, Integer> hubs) {
        this.hubs = hubs;
    }
}
