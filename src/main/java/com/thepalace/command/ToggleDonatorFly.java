package com.thepalace.command;

import com.thepalace.Lobby;
import com.thepalace.core.command.CommandException;
import com.thepalace.core.command.CommandMeta;
import com.thepalace.core.command.CommandPermission;
import com.thepalace.core.command.CoreCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(description="Toggles the flying for donators on and off")
@CommandPermission("lobby.toggledonatorfly")
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
