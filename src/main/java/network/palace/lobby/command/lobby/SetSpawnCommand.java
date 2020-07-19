package network.palace.lobby.command.lobby;

import network.palace.core.command.CommandException;
import network.palace.core.command.CommandMeta;
import network.palace.core.command.CoreCommand;
import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;

@CommandMeta(description = "Set spawn locations")
public class SetSpawnCommand extends CoreCommand {

    public SetSpawnCommand() {
        super("setspawn");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        if (args.length == 0) {
            player.sendMessage(ChatColor.AQUA + "Which spawn do you want to set?");
            player.sendMessage(ChatColor.GREEN + "- /lobby setspawn main - the main spawn location");
            player.sendMessage(ChatColor.GREEN + "- /lobby setspawn tutorial - the tutorial spawn location");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "main":
                Lobby.getConfigUtil().setSpawn(player.getLocation());
                player.sendMessage(ChatColor.GREEN + "The main spawn location has been updated!");
                break;
            case "tutorial":
                Lobby.getConfigUtil().setTutorialSpawn(player.getLocation());
                player.sendMessage(ChatColor.GREEN + "The tutorial spawn location has been updated!");
                break;
        }
    }
}
