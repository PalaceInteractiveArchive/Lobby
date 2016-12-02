package com.thepalace.util;

import com.thepalace.ServerInfo;
import com.thepalace.core.ItemUtils;
import com.thepalace.core.player.CPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryNav {

    private static final Material NAV_MATERIAL = Material.NETHER_STAR;
    public static final String NAV_NAME = ChatColor.DARK_AQUA + "Navigation";
    private static final int NAV_SIZE = 9;

    private static ServerInfo[] servers = {
            new ServerInfo("Parks", "TTC1", 2, Material.STICK),
            new ServerInfo("Creative", 3, Material.DIAMOND_PICKAXE),
            new ServerInfo("Arcade", 5, Material.BOW),
            new ServerInfo("Hub", "Hub1", 6, Material.WOOL)
    };

    public static void giveNav(CPlayer player) {
        ItemStack itemNav = InventoryNav.nameItem(new ItemStack(NAV_MATERIAL), NAV_NAME);
        player.getInventory().setItem(4, ItemUtils.makeUnableToMove(itemNav));
    }

    public static void openInventory(CPlayer player) {
        Inventory inventory = Bukkit.createInventory(null, NAV_SIZE, NAV_NAME);
        for (ServerInfo server : servers) {
            ItemStack serverItem = nameItem(new ItemStack(server.getItem()), server.getName());
            inventory.setItem(server.getPosition(), ItemUtils.makeUnableToMove(serverItem));
        }
        player.openInventory(inventory);
    }

    public static void sendToServer(CPlayer player, ItemStack itemStack) {
        ServerInfo server = getServerFromStack(itemStack);
        if (server == null) return;
        player.sendMessage(ChatColor.GREEN + "Sending you to " + server.getName() + "...");
        player.sendToServer(server.getLocation());
    }

    private static ServerInfo getServerFromStack(ItemStack stack) {
        if (stack == null) return null;
        if (!stack.hasItemMeta()) return null;
        if (stack.getItemMeta().getDisplayName() == null || stack.getItemMeta().getDisplayName().trim().isEmpty()) return null;
        for (ServerInfo serverInfo : servers) {
            if (stack.getItemMeta().getDisplayName().equals(serverInfo.getName()) && stack.getType().equals(serverInfo.getItem())) {
                return serverInfo;
            }
        }
        return null;
    }

    private static ItemStack nameItem(ItemStack itemStack, String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
