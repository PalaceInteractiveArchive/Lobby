package com.thepalace.command;

import com.thepalace.Lobby;
import com.thepalace.core.Core;
import com.thepalace.core.command.CommandException;
import com.thepalace.core.command.CommandMeta;
import com.thepalace.core.command.CommandPermission;
import com.thepalace.core.command.CoreCommand;
import com.thepalace.core.player.CPlayer;

/**
 * Created by Innectic on 11/19/2016.
 */
@CommandMeta(description="Set the world spawn for player login")
@CommandPermission("lobby.setspawn")
public class Spawn extends CoreCommand {

    public Spawn() {
        super("setspawn");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        Core.getPluginInstance(Lobby.class).getConfig().set("x", player.getLocation().getBlockX());
        Core.getPluginInstance(Lobby.class).getConfig().set("y", player.getLocation().getBlockY());
        Core.getPluginInstance(Lobby.class).getConfig().set("z", player.getLocation().getBlockZ());
        Core.getPluginInstance(Lobby.class).getConfig().set("pitch", player.getLocation().getPitch());
        Core.getPluginInstance(Lobby.class).getConfig().set("yaw", player.getLocation().getYaw());

        try {
            Core.getPluginInstance(Lobby.class).saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
