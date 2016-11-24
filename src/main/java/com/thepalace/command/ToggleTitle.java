package com.thepalace.command;

import com.thepalace.Lobby;
import com.thepalace.core.command.CommandException;
import com.thepalace.core.command.CommandMeta;
import com.thepalace.core.command.CommandPermission;
import com.thepalace.core.command.CoreCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(description="Toggles the title message on join")
@CommandPermission("lobby.toggletitle")
public class ToggleTitle extends CoreCommand {

    public ToggleTitle() {
        super("toggletitle");
    }

    @Override
    protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) return;
        Lobby lobby = Lobby.getPlugin(Lobby.class);
        lobby.getConfig().set("titleEnabled", !lobby.getConfig().getBoolean("titleEnabled"));
        lobby.saveConfig();
        sender.sendMessage(ChatColor.GREEN + "Showing title on join is " + lobby.getConfig().getBoolean("titleEnabled"));
    }
}
