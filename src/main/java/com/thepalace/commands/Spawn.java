package com.thepalace.commands;

import com.thepalace.Lobby;
import com.thepalace.core.command.CommandException;
import com.thepalace.core.command.CommandMeta;
import com.thepalace.core.command.CoreCommand;
import com.thepalace.core.player.CPlayer;
import org.bukkit.ChatColor;

@CommandMeta(description = "Set the spawn")
public class Spawn extends CoreCommand {

    public Spawn() {
        super("spawn");
    }

    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        try {
            Lobby.instance.getConfig().set("world", player.getLocation().getWorld().getName());
            Lobby.instance.getConfig().set("x", player.getLocation().getBlockX());
            Lobby.instance.getConfig().set("y", player.getLocation().getBlockY());
            Lobby.instance.getConfig().set("z", player.getLocation().getBlockZ());
            Lobby.instance.getConfig().set("pitch", player.getLocation().getPitch());
            Lobby.instance.getConfig().set("yaw", player.getLocation().getYaw());
            Lobby.instance.saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "There was an error saving the config.");
        }
    }
}
