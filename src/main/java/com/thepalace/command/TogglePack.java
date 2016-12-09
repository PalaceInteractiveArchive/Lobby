package com.thepalace.command;

import com.palacemc.core.command.CommandException;
import com.palacemc.core.command.CommandMeta;
import com.palacemc.core.command.CommandPermission;
import com.palacemc.core.command.CoreCommand;
import com.thepalace.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(description="Toggles the resource pack on and off")
@CommandPermission("lobby.togglepack")
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
