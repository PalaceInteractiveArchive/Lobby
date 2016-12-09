package com.thepalace.command;

import com.palacemc.core.command.CommandException;
import com.palacemc.core.command.CommandMeta;
import com.palacemc.core.command.CommandPermission;
import com.palacemc.core.command.CoreCommand;
import com.thepalace.Lobby;
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
