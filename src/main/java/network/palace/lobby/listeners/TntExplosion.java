package network.palace.lobby.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * @author Innectic
 * @since 12/31/2016
 */
public class TntExplosion implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTntExplosionEvent(BlockExplodeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityExplosionEvent(EntityExplodeEvent e) {
        e.blockList().clear();
    }
}
