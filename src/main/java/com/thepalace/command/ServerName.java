package com.thepalace.command;

import com.thepalace.Lobby;
import com.thepalace.core.command.CommandException;
import com.thepalace.core.command.CommandMeta;
import com.thepalace.core.command.CommandPermission;
import com.thepalace.core.command.CoreCommand;
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
