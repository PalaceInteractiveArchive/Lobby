package network.palace.lobby.command.lobby;

import network.palace.core.command.CommandException;
import network.palace.core.command.CommandMeta;
import network.palace.core.command.CoreCommand;
import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

@CommandMeta(description = "Set the world spawn for player login")
public class SetSpawnCommand extends CoreCommand {

    public SetSpawnCommand() {
        super("setspawn");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        FileConfiguration config = Lobby.getInstance().getConfig();

        Location loc = player.getLocation();

        config.set("spawn.world", loc.getWorld().getName());
        config.set("spawn.x", loc.getX());
        config.set("spawn.y", loc.getY());
        config.set("spawn.z", loc.getZ());
        config.set("spawn.yaw", loc.getYaw());
        config.set("spawn.pitch", loc.getPitch());

        Lobby.getInstance().saveConfig();

        player.sendMessage(ChatColor.GREEN + "Spawn set to your position");
    }
}
