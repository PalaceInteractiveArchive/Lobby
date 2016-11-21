package com.thepalace.command;

import com.thepalace.Lobby;
import com.thepalace.core.Core;
import com.thepalace.core.command.CommandException;
import com.thepalace.core.command.CommandMeta;
import com.thepalace.core.command.CommandPermission;
import com.thepalace.core.command.CoreCommand;
import com.thepalace.core.player.CPlayer;
import org.bukkit.ChatColor;

@CommandMeta(description="Set the world spawn for player login")
@CommandPermission("lobby.setspawn")
public class Spawn extends CoreCommand {

    public Spawn() {
        super("setspawn");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        Lobby lobby = Lobby.getPlugin(Lobby.class);
        lobby.getConfig().set("world", player.getLocation().getWorld().getName());
        lobby.getConfig().set("x", player.getLocation().getBlockX());
        lobby.getConfig().set("y", player.getLocation().getBlockY());
        lobby.getConfig().set("z", player.getLocation().getBlockZ());
        lobby.getConfig().set("pitch", player.getLocation().getPitch());
        lobby.getConfig().set("yaw", player.getLocation().getYaw());
        lobby.saveConfig();
        player.sendMessage(ChatColor.GREEN + "Spawn set to your position");
    }
}
