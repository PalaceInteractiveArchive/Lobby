package network.palace.lobby.command;

import network.palace.core.command.CommandException;
import network.palace.core.command.CommandMeta;
import network.palace.core.command.CoreCommand;
import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;

@CommandMeta(description = "Tutorial command")
public class TutorialCommand extends CoreCommand {

    public TutorialCommand() {
        super("tutorial");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        if (args.length < 1) return;
        switch (args[0].toLowerCase()) {
            case "test": {
                if (player.getRegistry().hasEntry("tutorial_start")) {
                    player.sendMessage(ChatColor.RED + "You're still in the tutorial!");
                    return;
                }
                Lobby.getTutorialManager().start(player);
                break;
            }
            case "reset": {
                String[] entries = new String[]{"tutorial_start", "tutorial_actions", "tutorial_leave_bed", "tutorial_first_scene_leave_message_delay"};
                for (String entry : entries) {
                    player.getRegistry().removeEntry(entry);
                }
                break;
            }
            case "audio": {
                break;
            }
        }
    }
}
