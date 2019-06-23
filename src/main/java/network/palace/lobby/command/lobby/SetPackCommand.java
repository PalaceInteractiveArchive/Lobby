package network.palace.lobby.command.lobby;

import network.palace.core.command.CommandException;
import network.palace.core.command.CommandMeta;
import network.palace.core.command.CoreCommand;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(description = "Manage resource pack settings")
public class SetPackCommand extends CoreCommand {

    public SetPackCommand() {
        super("setpack");
    }

    @Override
    protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            helpMenu(sender);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "name": {
                String name = args[1];
                Lobby.getConfigUtil().setServerPack(name);
                sender.sendMessage(ChatColor.GREEN + "Resource pack name set to " + ChatColor.AQUA + name);
                return;
            }
            case "disable": {
                Lobby.getConfigUtil().setServerPack(null);
                sender.sendMessage(ChatColor.RED + "Players will no longer be sent a resource pack on join!");
                return;
            }
        }
        helpMenu(sender);
    }

    private void helpMenu(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Pack Commands:");
        sender.sendMessage(ChatColor.GREEN + "/lobby setpack name [name] " + ChatColor.AQUA + "- Set the name of the pack players are sent");
        sender.sendMessage(ChatColor.GREEN + "/lobby setpack disable " + ChatColor.AQUA + "- Disable the server pack");
    }
}
