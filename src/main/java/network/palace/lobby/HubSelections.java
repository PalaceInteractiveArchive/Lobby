package network.palace.lobby;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

/**
 * @author Innectic
 * @since 4/11/2017
 */
@AllArgsConstructor
public class HubSelections {
    @Getter private String hubName;
    @Getter private String serverName;
    @Getter private ItemStack displayItem;
}
