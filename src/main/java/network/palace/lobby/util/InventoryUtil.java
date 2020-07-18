package network.palace.lobby.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.core.utils.ItemUtil;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class InventoryUtil {
    private List<UUID> joinList = new ArrayList<>();
    private ItemStack cosmetics;
    private ItemStack nav;
    private ItemStack hub;

    public InventoryUtil() {
        Core.runTaskTimer(Lobby.getInstance(), () -> {
            if (joinList.isEmpty()) return;
            List<UUID> list = new ArrayList<>(joinList);
            joinList.clear();
            list.forEach(uuid -> {
                CPlayer player = Core.getPlayerManager().getPlayer(uuid);
                if (player == null) return;
                setInventory(player);
            });
        }, 0L, 10L);

        cosmetics = ItemUtil.create(Material.ENDER_CHEST, ChatColor.GREEN + "Cosmetics",
                Collections.singletonList(ChatColor.GRAY + "Open Cosmetics Menu"));
        nav = ItemUtil.create(Material.NETHER_STAR, ChatColor.BLUE + "Navigation");
        hub = ItemUtil.create(Material.BOOK, ChatColor.BLUE + "Hub Selector");
    }

    /**
     * Handle inventory setting when a player joins
     *
     * @param player the player
     */
    public void handleJoin(CPlayer player) {
        joinList.add(player.getUniqueId());
    }

    /**
     * Set the player's inventory with the proper items
     *
     * @param player the player
     */
    public void setInventory(CPlayer player) {
        player.getInventory().setContents(new ItemStack[]{cosmetics, null, null, null, nav, null, null, null, hub});
    }

    public static void sendInventoryUpdate(CPlayer player, int windowId, int slot, ItemStack item) throws InvocationTargetException {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SET_SLOT);
        StructureModifier<Integer> mod = packet.getIntegers();
        mod.write(0, windowId);
        mod.write(1, slot);
        packet.getItemModifier().write(0, item);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player.getBukkitPlayer(), packet);
    }
}
