package network.palace.lobby;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
public class ServerInfo {
    @Getter private String name;
    @Getter private String location;
    @Getter private int position;
    @Getter private Material item;
    @Getter private String description;

    /**
     * Create new ServerInfo data for a server
     *
     * @param name        the name of the server
     * @param position    the position in the nav menu
     * @param item        the item in the nav menu
     * @param description the language node for the description
     */
    public ServerInfo(String name, int position, Material item, String description) {
        this(name, name, position, item, description);
    }
}
