package network.palace.lobby.command;

import network.palace.core.command.CommandException;
import network.palace.core.command.CommandMeta;
import network.palace.core.command.CommandPermission;
import network.palace.core.command.CoreCommand;
import network.palace.core.player.Rank;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(description = "Set the server name for tab list")
@CommandPermission(rank = Rank.DEVELOPER)
public class SetServerName extends CoreCommand {

    public SetServerName() {
        super("setservername");
    }

    @Override
    protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) return;
        Lobby lobby = Lobby.getPlugin(Lobby.class);
        lobby.getConfig().set("serverName", args[0]);
        lobby.saveConfig();
        sender.sendMessage(ChatColor.GREEN + "Server name set to " + args[0]);
    }
}
