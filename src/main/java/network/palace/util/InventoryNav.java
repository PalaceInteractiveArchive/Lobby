package network.palace.util;

import network.palace.core.inventory.ClickAction;
import network.palace.core.inventory.CoreInventory;
import network.palace.core.inventory.CoreInventoryClick;
import network.palace.core.inventory.InventoryButton;
import network.palace.core.utils.ItemUtil;
import network.palace.core.player.CPlayer;
import network.palace.ServerInfo;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryNav {

    private final Material NAV_MATERIAL = Material.NETHER_STAR;
    private final String NAV_NAME = ChatColor.DARK_AQUA + "Navigation";
    private final int NAV_SIZE = 9;

    private final ServerInfo[] SERVERS = {
            new ServerInfo("Parks", "TTC1", 2, Material.STICK),
            new ServerInfo("Creative", 3, Material.DIAMOND_PICKAXE),
            new ServerInfo("Arcade", 5, Material.BOW),
            new ServerInfo("Hub", "Hub1", 6, Material.WOOL)
    };

    private CoreInventory inv;

    public InventoryNav() {
        inv = new CoreInventory(NAV_SIZE, NAV_NAME);
        for (ServerInfo server : SERVERS) {
            ItemStack item = nameItem(new ItemStack(server.getItem()), server.getName());
            CoreInventoryClick click = (player, clickAction) -> sendToServer(player, server);
            inv.addButton(new InventoryButton(item, click), server.getPosition());
        }
    }

    public void giveNav(CPlayer player) {
        ItemStack itemNav = nameItem(new ItemStack(NAV_MATERIAL), NAV_NAME);
        player.getInventory().setItem(4, ItemUtil.makeUnableToMove(itemNav));
    }

    public void openInventory(CPlayer player) {
        inv.open(player);
    }

    private void sendToServer(CPlayer player, ServerInfo server) {
        if (server == null) return;
        player.sendMessage(ChatColor.GREEN + "Sending you to " + server.getName() + "...");
        player.sendToServer(server.getLocation());
    }

    private ItemStack nameItem(ItemStack itemStack, String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
