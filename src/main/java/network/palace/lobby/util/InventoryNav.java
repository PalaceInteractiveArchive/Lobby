package network.palace.lobby.util;

import com.google.common.collect.ImmutableMap;
import network.palace.core.Core;
import network.palace.core.menu.Menu;
import network.palace.core.menu.MenuButton;
import network.palace.core.player.CPlayer;
import network.palace.core.utils.ItemUtil;
import network.palace.core.utils.TextUtil;
import network.palace.lobby.Lobby;
import network.palace.lobby.ServerInfo;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class InventoryNav {
    public static final ServerInfo[] SERVERS = {
            new ServerInfo("Creative", 10, Material.DIAMOND_PICKAXE, "lobby.nav.creative"),
            new ServerInfo("Theme Parks", "WDW", 13, Material.STICK, "lobby.nav.parks"),
            new ServerInfo("Arcade", 16, Material.BOW, "lobby.nav.arcade")
    };
    private int parks = 0;
    private int creative = 0;
    private int arcade = 0;
    private HashMap<UUID, Menu> openInventories = new HashMap<>();
    private boolean green = true;

    public InventoryNav() {
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
                                    newLore.add(ChatColor.GRAY + "" + count + " player" + TextUtil.pluralize(count));
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

    public void openInventory(CPlayer player) {
        Menu menu = new Menu(27, ChatColor.BLUE + "Navigation", player, new ArrayList<>());
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
            if (count >= 0) descList.add(ChatColor.GRAY + "" + count + " player" + TextUtil.pluralize(count));

            ItemStack item = ItemUtil.create(info.getItem(), ChatColor.GREEN + info.getName(), descList);
            item = ItemUtil.hideAttributes(item);
            menu.setButton(new MenuButton(info.getPosition(), item, ImmutableMap.of(ClickType.LEFT, p -> {
                player.sendMessage(ChatColor.GREEN + "Sending you to " + info.getName() + "...");
                player.performCommand("warp wdw");
            })));
        }
        menu.open();
        openInventories.put(player.getUniqueId(), menu);
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
