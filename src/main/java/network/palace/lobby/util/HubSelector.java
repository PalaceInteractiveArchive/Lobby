package network.palace.lobby.util;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import network.palace.core.Core;
import network.palace.core.menu.Menu;
import network.palace.core.menu.MenuButton;
import network.palace.core.player.CPlayer;
import network.palace.core.utils.ItemUtil;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author Innectic
 * @since 4/11/2017
 */
public class HubSelector {
    private HashMap<UUID, Menu> openInventories = new HashMap<>();
    @Getter private HashMap<String, Integer> hubs = new HashMap<>();
    private boolean green = true;

    public HubSelector() {
        hubs.put(Core.getInstanceName(), 0);
        Core.runTaskTimer(Lobby.getInstance(), () -> {
            try {
                green = !green;
                for (Map.Entry<UUID, Menu> entry : openInventories.entrySet()) {
                    UUID uuid = entry.getKey();
                    CPlayer player = Core.getPlayerManager().getPlayer(uuid);
                    if (player != null && player.getOpenInventory().isPresent()) {
                        Menu menu = entry.getValue();
                        for (int i = 0; i < menu.getSize(); i++) {
                            Optional<MenuButton> opt = menu.getButton(i);
                            if (!opt.isPresent()) continue;
                            MenuButton button = opt.get();
                            ItemStack item = button.getItemStack();
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
                            menu.setButton(new MenuButton(i, item, button.getActions()));
                            InventoryUtil.sendInventoryUpdate(player, player.getWindowId(), i, item);
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
        Menu menu = new Menu(27, ChatColor.BLUE + "Hub Selector", player, new ArrayList<>());
        int pos = 10;
        for (Map.Entry<String, Integer> entry : hubs.entrySet()) {
            try {
                List<String> lore = Arrays.asList(" ",
                        (green ? ChatColor.GREEN : ChatColor.BLACK) + "➤ " + ChatColor.GREEN + "Switch to " + entry.getKey(),
                        ChatColor.GRAY + "" + entry.getValue() + " players");
                ItemStack hub = ItemUtil.create(Material.CHISELED_QUARTZ_BLOCK, ChatColor.GREEN + entry.getKey(), lore);
                menu.setButton(new MenuButton(pos, hub, ImmutableMap.of(ClickType.LEFT, p -> {
                    p.sendMessage(ChatColor.GREEN + "Sending you to " + entry.getKey() + "...");
                    p.sendToServer(entry.getKey());
                    p.closeInventory();
                })));
            } catch (Exception e) {
                e.printStackTrace();
            }
            pos += 1;
            if ((pos + 2) % 9 == 0) {
                pos += 3;
            }
        }
        menu.open();
        openInventories.put(player.getUniqueId(), menu);
    }

    public void closeInventory(UUID uuid) {
        openInventories.remove(uuid);
    }

    public void updateCounts(HashMap<String, Integer> hubs) {
        this.hubs = hubs;
    }
}
