package com.thepalace.command;

import com.palacemc.core.command.CommandException;
import com.palacemc.core.command.CommandMeta;
import com.palacemc.core.command.CommandPermission;
import com.palacemc.core.command.CoreCommand;
import com.thepalace.Lobby;
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
