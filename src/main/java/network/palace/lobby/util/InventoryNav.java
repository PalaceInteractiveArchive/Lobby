package network.palace.lobby.util;

import network.palace.core.inventory.InventoryClick;
import network.palace.core.inventory.impl.Inventory;
import network.palace.core.inventory.impl.InventoryButton;
import network.palace.core.player.CPlayer;
import network.palace.core.utils.ItemUtil;
import network.palace.lobby.ServerInfo;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class InventoryNav {

    private final Material NAV_MATERIAL = Material.NETHER_STAR;
    private final String NAV_NAME = ChatColor.DARK_AQUA + "Navigation";
    private final int NAV_SIZE = 9;

    private final ServerInfo[] SERVERS = {
            new ServerInfo("Parks", "TTC", 4, Material.STICK),
            new ServerInfo("Creative", 2, Material.DIAMOND_PICKAXE),
            new ServerInfo("Arcade", 6, Material.BOW)
    };

    private Inventory inv;

    public InventoryNav() {
        inv = new Inventory(NAV_SIZE, NAV_NAME);

        for (ServerInfo server : SERVERS) {
            ItemStack item = ItemUtil.create(server.getItem(), ChatColor.AQUA + server.getName());
            item = ItemUtil.hideAttributes(item);
            InventoryClick click = (player, clickAction) -> sendToServer(player, server);
            inv.addButton(new InventoryButton(item, click), server.getPosition());
        }
    }

    public void giveNav(CPlayer player) {
        ItemStack itemNav = ItemUtil.create(NAV_MATERIAL, NAV_NAME);
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
}
