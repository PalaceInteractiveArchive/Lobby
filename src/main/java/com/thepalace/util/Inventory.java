package com.thepalace.util;

import com.thepalace.core.player.CPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Innectic on 11/18/2016.
 */
public class Inventory {

    public static void openInventory(CPlayer player) {
        org.bukkit.inventory.Inventory inventory = Bukkit.createInventory(null, 9, "Navigation");

        inventory.setItem(2, nameItem(new ItemStack(Material.STICK), "Parks"));
        inventory.setItem(3, nameItem(new ItemStack(Material.DIAMOND_PICKAXE), "Creative"));
        inventory.setItem(5, nameItem(new ItemStack(Material.BOW), "Arcade"));
        inventory.setItem(6, nameItem(new ItemStack(Material.WOOL), "Hub"));

        player.openInventory(inventory);
    }

    public static ItemStack nameItem(ItemStack itemStack, String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
