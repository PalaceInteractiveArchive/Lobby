package network.palace.lobby.util;

import network.palace.core.Core;
import network.palace.core.inventory.InventoryClick;
import network.palace.core.inventory.impl.Inventory;
import network.palace.core.inventory.impl.InventoryButton;
import network.palace.core.player.CPlayer;
import network.palace.core.utils.ItemUtil;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

/**
 * @author Innectic
 * @since 4/11/2017
 */
public class HubSelector {

    private final Material NAV_MATERIAL = Material.BOOK;
    private final String NAV_NAME = ChatColor.BLUE + "Choose a Server";
    private final int NAV_SIZE = 9;

    private Inventory inventory;

    public HubSelector() {
        inventory = new Inventory(NAV_SIZE, NAV_NAME);

        Lobby plugin = Lobby.getPlugin(Lobby.class);

        Set<String> hubs = plugin.getConfig().getConfigurationSection("hubs").getKeys(false);

        if (hubs.size() <= 0) return;

        int location = 0;

        for (String hub : hubs) {
            String name = plugin.getConfig().getString("hubs." + hub + ".name");
            ItemStack item = ItemUtil.create(Material.WOOL, ChatColor.YELLOW + name);
            String server = plugin.getConfig().getString("hubs." + hub + ".server");

            if (Core.getInstanceName().equals(server)) {
                item = ItemUtil.addGlow(item);
            }

            InventoryClick click = (player, clickAction) -> sendToHub(player, server);
            inventory.addButton(new InventoryButton(item, click), location);
            location++;
        }
    }

    private void sendToHub(CPlayer player, String server) {
        if (server == null) return;
        if (player == null) return;

        player.sendToServer(server);
        player.sendMessage(ChatColor.RED + "Sending you to " + ChatColor.BLUE + server);
    }

    public void openInventory(CPlayer player) {
        inventory.open(player);
    }

    public void giveNav(CPlayer player) {
        Lobby plugin = Lobby.getPlugin(Lobby.class);
        if (plugin.getConfig().getBoolean("canSelectLobbies", false)) {
            ItemStack itemNav = ItemUtil.create(NAV_MATERIAL, NAV_NAME);
            player.getInventory().setItem(8, ItemUtil.makeUnableToMove(itemNav));
        }
    }
}
