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
@CommandMeta(description = "Toggle the lobby selection")
@CommandPermission(rank = Rank.WIZARD)
public class ToggleSelectLobbies extends CoreCommand {

    public ToggleSelectLobbies() {
        super("toggleselectlobbies");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        Lobby.getPlugin(Lobby.class).getConfig().set("canSelectLobbies", !Lobby.getPlugin(Lobby.class).isHubSelectorEnabled());
        Lobby.getPlugin(Lobby.class).setHubSelectorEnabled(!Lobby.getPlugin(Lobby.class).isHubSelectorEnabled());

        player.sendMessage(ChatColor.GREEN + "Toggled select lobbies to " + Lobby.getPlugin(Lobby.class).isHubSelectorEnabled());
    }
}
