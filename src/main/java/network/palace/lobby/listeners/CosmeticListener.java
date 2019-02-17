package network.palace.lobby.listeners;

import network.palace.core.player.CPlayer;
import network.palace.cosmetics.events.ToyEquipEvent;
import network.palace.cosmetics.events.ToyUnequipEvent;
import network.palace.cosmetics.toys.Toy;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CosmeticListener implements Listener {

    @EventHandler
    public void onToyEquip(ToyEquipEvent event) {
        CPlayer player = event.getPlayer();
        Toy toy = event.getToy();
        PlayerInventory inv = player.getInventory();
        inv.setItem(2, toy.getMenuItem());
    }

    @EventHandler
    public void onToyUnequip(ToyUnequipEvent event) {
        CPlayer player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        inv.setItem(2, new ItemStack(Material.AIR));
    }
}
