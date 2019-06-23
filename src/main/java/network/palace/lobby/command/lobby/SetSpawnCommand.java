package network.palace.lobby.command.lobby;

import network.palace.core.command.CommandException;
import network.palace.core.command.CommandMeta;
import network.palace.core.command.CoreCommand;
import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;

@CommandMeta(description = "Set the world spawn for player login")
public class SetSpawnCommand extends CoreCommand {

    public SetSpawnCommand() {
        super("setspawn");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        Lobby.getConfigUtil().setSpawn(player.getLocation());
        player.sendMessage(ChatColor.GREEN + "Spawn set to your position");
    }
}
