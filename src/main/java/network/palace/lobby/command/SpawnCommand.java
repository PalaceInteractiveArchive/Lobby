package network.palace.lobby.command;

import network.palace.core.command.CommandException;
import network.palace.core.command.CommandMeta;
import network.palace.core.command.CoreCommand;
import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;

@CommandMeta(description = "Teleport to spawn")
public class SpawnCommand extends CoreCommand {

    public SpawnCommand() {
        super("spawn");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        player.teleport(Lobby.getConfigUtil().getSpawn());
        player.sendMessage(ChatColor.GRAY + "Teleported you to the spawn.");
    }
}
