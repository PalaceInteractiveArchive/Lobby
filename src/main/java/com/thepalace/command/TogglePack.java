package com.thepalace.command;

import com.thepalace.Lobby;
import com.thepalace.core.command.CommandException;
import com.thepalace.core.command.CommandMeta;
import com.thepalace.core.command.CommandPermission;
import com.thepalace.core.command.CoreCommand;
import com.thepalace.core.player.CPlayer;
import org.bukkit.ChatColor;

@CommandMeta(description="Toggles the resource pack on and off")
@CommandPermission("lobby.togglepack")
public class TogglePack extends CoreCommand {

    public TogglePack() {
        super("togglepack");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        if (args.length <= 0) return;
        Lobby lobby = Lobby.getPlugin(Lobby.class);
        lobby.getConfig().set("packEnabled", !lobby.getConfig().getBoolean("packEnabled"));
        lobby.saveConfig();
        player.sendMessage(ChatColor.GREEN + "Pack loading status is " + lobby.getConfig().getBoolean("packEnabled"));
    }
}
