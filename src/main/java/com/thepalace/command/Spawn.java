package com.thepalace.command;

import com.palacemc.core.command.CommandException;
import com.palacemc.core.command.CommandMeta;
import com.palacemc.core.command.CommandPermission;
import com.palacemc.core.command.CoreCommand;
import com.palacemc.core.player.CPlayer;
import com.thepalace.Lobby;
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
        lobby.getConfig().set("yaw", player.getLocation().getYaw());
        lobby.getConfig().set("pitch", player.getLocation().getPitch());
        lobby.saveConfig();
        player.sendMessage(ChatColor.GREEN + "Spawn set to your position");
    }
}
