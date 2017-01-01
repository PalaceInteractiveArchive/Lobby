package network.palace.command;

import network.palace.Lobby;
import network.palace.core.command.CommandException;
import network.palace.core.command.CommandMeta;
import network.palace.core.command.CommandPermission;
import network.palace.core.command.CoreCommand;
import network.palace.core.player.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(description="Toggles the flying for donators on and off")
@CommandPermission(rank = Rank.WIZARD)
public class ToggleDonatorFly extends CoreCommand {

    public ToggleDonatorFly() {
        super("toggledonatorfly");
    }

    @Override
    protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) return;
        Lobby lobby = Lobby.getPlugin(Lobby.class);
        lobby.getConfig().set("flightForDonorsEnabled", !lobby.getConfig().getBoolean("flightForDonorsEnabled"));
        lobby.saveConfig();
        sender.sendMessage(ChatColor.GREEN + "Flight for donators is " + lobby.getConfig().getBoolean("flightForDonorsEnabled"));
    }
}