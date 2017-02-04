package network.palace.lobby.command;

import network.palace.core.command.CommandException;
import network.palace.core.command.CommandMeta;
import network.palace.core.command.CommandPermission;
import network.palace.core.command.CoreCommand;
import network.palace.core.player.Rank;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(description = "Toggles the title message on join")
@CommandPermission(rank = Rank.WIZARD)
public class ToggleTitle extends CoreCommand {

    public ToggleTitle() {
        super("toggletitle");
    }

    @Override
    protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {
        Lobby lobby = Lobby.getPlugin(Lobby.class);
        lobby.getConfig().set("titleEnabled", !lobby.getConfig().getBoolean("titleEnabled"));
        lobby.saveConfig();
        sender.sendMessage(ChatColor.GREEN + "Showing title on join is " + lobby.getConfig().getBoolean("titleEnabled"));
    }
}
