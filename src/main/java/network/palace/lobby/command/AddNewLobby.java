package network.palace.lobby.command;

import network.palace.core.command.CommandException;
import network.palace.core.command.CommandMeta;
import network.palace.core.command.CommandPermission;
import network.palace.core.command.CoreCommand;
import network.palace.core.player.CPlayer;
import network.palace.core.player.Rank;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;

/**
 * @author Innectic
 * @since 4/11/2017
 */
@CommandMeta(description = "Add a new lobby")
@CommandPermission(rank = Rank.DEVELOPER)
public class AddNewLobby extends CoreCommand {

    public AddNewLobby() {
        super("addnewlobby");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        Lobby plugin = Lobby.getPlugin(Lobby.class);

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Please supply the serverName, and display name!");
            return;
        }

        String name = args[0];
        String server = args[1];

        plugin.getConfig().set("hubs." + name + ".name", name);
        plugin.getConfig().set("hubs." + name + ".name", server);

        plugin.saveConfig();

        player.sendMessage(ChatColor.GREEN + "Added another lobby!");
    }
}
