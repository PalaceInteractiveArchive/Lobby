package com.thepalace.command;

import com.palacemc.core.command.CommandException;
import com.palacemc.core.command.CommandMeta;
import com.palacemc.core.command.CommandPermission;
import com.palacemc.core.command.CoreCommand;
import com.thepalace.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(description="Set the server name for tab list")
@CommandPermission("lobby.setservername")
public class ServerName extends CoreCommand {

    public ServerName() {
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
