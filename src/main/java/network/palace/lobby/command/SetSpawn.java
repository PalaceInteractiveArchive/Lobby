package network.palace.lobby.command;

import network.palace.lobby.Lobby;
import network.palace.core.command.CommandException;
import network.palace.core.command.CommandMeta;
import network.palace.core.command.CommandPermission;
import network.palace.core.command.CoreCommand;
import network.palace.core.player.CPlayer;
import network.palace.core.player.Rank;
import org.bukkit.ChatColor;

@CommandMeta(description="Set the world spawn for player login")
@CommandPermission(rank = Rank.WIZARD)
public class SetSpawn extends CoreCommand {

    public SetSpawn() {
        super("setspawn");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        Lobby lobby = Lobby.getPlugin(Lobby.class);

        lobby.getConfig().set("world", player.getLocation().getWorld().getName());
        lobby.getConfig().set("x", player.getLocation().getBlockX());
        lobby.getConfig().set("y", player.getLocation().getBlockY());
        lobby.getConfig().set("z", player.getLocation().getBlockZ());
        lobby.getConfig().set("yaw", player.getLocation().getYaw());
        lobby.getConfig().set("pitch", player.getLocation().getPitch());

        lobby.saveConfig();

        player.sendMessage(ChatColor.GREEN + "Spawn set to your position");
    }
}
