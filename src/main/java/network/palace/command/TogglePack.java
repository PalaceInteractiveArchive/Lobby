package network.palace.command;

import network.palace.Lobby;
import network.palace.core.command.CommandException;
import network.palace.core.command.CommandMeta;
import network.palace.core.command.CommandPermission;
import network.palace.core.command.CoreCommand;
import network.palace.core.player.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(description="Toggles the resource pack on and off")
@CommandPermission(rank = Rank.WIZARD)
public class TogglePack extends CoreCommand {

    public TogglePack() {
        super("togglepack");
    }

    @Override
    protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) return;
        Lobby lobby = Lobby.getPlugin(Lobby.class);
        lobby.getConfig().set("packEnabled", !lobby.getConfig().getBoolean("packEnabled"));
        lobby.saveConfig();
        sender.sendMessage(ChatColor.GREEN + "Pack loading status is " + lobby.getConfig().getBoolean("packEnabled"));
    }
}
