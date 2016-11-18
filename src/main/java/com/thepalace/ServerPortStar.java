package com.thepalace;

import com.palacemc.palacecore.PalaceCore;
import com.palacemc.palacecore.dashboard.packets.dashboard.PacketSendToServer;
import com.thepalace.core.Core;
import com.thepalace.core.player.CPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ServerPortStar {

    private static final String INVENTORY_NAME = ChatColor.AQUA + "Server Selector";
    private static ItemStack portStar = new ItemStack(Material.NETHER_STAR);

    public static boolean ifItemIsPortStar(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (itemStack.getType() != Material.NETHER_STAR) return false;
        if (itemStack.getItemMeta() == null) return false;
        if (itemStack.getItemMeta().getDisplayName() == null) return false;
        return itemStack.getItemMeta().getDisplayName().equals(INVENTORY_NAME);
    }

    public static void givePortStar(CPlayer player) {
        player.getInventory().remove(Material.NETHER_STAR);
        ItemStack tempStack = portStar.clone();
        ItemMeta itemMeta = tempStack.getItemMeta();
        itemMeta.setDisplayName(INVENTORY_NAME);
        tempStack.setItemMeta(itemMeta);
        player.getInventory().addItem(tempStack);
    }

    public static void openPortStar(CPlayer player) {
        if (player.getBukkitPlayer().getItemInHand().getType() != portStar.getType()) return;
        if (!player.getBukkitPlayer().getItemInHand().getItemMeta().getDisplayName().equals(INVENTORY_NAME)) return;
        Inventory inventory = Bukkit.createInventory(null, 9, INVENTORY_NAME);

        ItemStack parksIs = new ItemStack(Material.NETHER_STAR);
        ItemMeta parksMeta = parksIs.getItemMeta();
        parksMeta.setDisplayName(ChatColor.AQUA + "Parks");
        parksIs.setItemMeta(parksMeta);

        ItemStack arcadeIs = new ItemStack(Material.EMERALD);
        ItemMeta arcadeMeta = arcadeIs.getItemMeta();
        arcadeMeta.setDisplayName(ChatColor.AQUA + "Arcade");
        arcadeIs.setItemMeta(arcadeMeta);

        ItemStack creativeIs = new ItemStack(Material.GRASS);
        ItemMeta creativeMeta = arcadeIs.getItemMeta();
        creativeMeta.setDisplayName(ChatColor.AQUA + "Creative");
        creativeIs.setItemMeta(creativeMeta);

        inventory.addItem(parksIs);
        inventory.addItem(arcadeIs);
        inventory.addItem(creativeIs);
        player.openInventory(inventory);
    }

    public static void checkPortStar(InventoryClickEvent event) {
        CPlayer player = Core.getPlayerManager().getPlayer((Player) event.getWhoClicked());
        if (player.getBukkitPlayer().getOpenInventory() != null) {
            if (ifItemIsPortStar(event.getCurrentItem())) {
                event.setCancelled(true);
            }
        }
        if (player.getBukkitPlayer().getOpenInventory() == null) return;
        if (player.getBukkitPlayer().getOpenInventory().getTitle() == null || !player.getBukkitPlayer().getOpenInventory().getTitle() .equals(INVENTORY_NAME)) return;
        event.setCancelled(true);
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Parks")) {
            PalaceCore.dashboardConnection.send(new PacketSendToServer(player.getUuid(), "TTC"));
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Arcade")) {
            PalaceCore.dashboardConnection.send(new PacketSendToServer(player.getUuid(), "Arcade"));
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Creative")) {
            PalaceCore.dashboardConnection.send(new PacketSendToServer(player.getUuid(), "Creative"));
        }
    }
}
