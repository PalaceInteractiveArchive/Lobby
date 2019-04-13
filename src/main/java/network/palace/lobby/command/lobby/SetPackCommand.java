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
            case "enabled": {
                Boolean val = Boolean.valueOf(args[1]);
                Lobby.getInstance().setPackEnabled(val);
                Lobby.getInstance().getConfig().set("pack.send", val);
                Lobby.getInstance().saveConfig();
                if (val) {
                    sender.sendMessage(ChatColor.GREEN + "Players will now be sent a resource pack on join!");
                } else {
                    sender.sendMessage(ChatColor.RED + "Players will no longer be sent a resource pack on join!");
                }
                return;
            }
            case "name": {
                String name = args[1];
                Lobby.getInstance().setPackName(name);
                Lobby.getInstance().getConfig().set("pack.name", name);
                Lobby.getInstance().saveConfig();
                sender.sendMessage(ChatColor.GREEN + "Resource pack name set to " + ChatColor.AQUA + name);
                return;
            }
        }
        helpMenu(sender);
    }

    private void helpMenu(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Pack Commands:");
        sender.sendMessage(ChatColor.GREEN + "/lobby setpack enabled [true/false] " + ChatColor.AQUA + "- Set whether the player should be sent a pack on join");
        sender.sendMessage(ChatColor.GREEN + "/lobby setpack name [name] " + ChatColor.AQUA + "- Set the name of the pack players are sent");
    }
}
