package network.palace.lobby.command;

import network.palace.core.command.CommandMeta;
import network.palace.core.command.CoreCommand;
import network.palace.core.player.Rank;
import network.palace.lobby.command.lobby.SetPackCommand;
import network.palace.lobby.command.lobby.SetSpawnCommand;

@CommandMeta(description = "Lobby command", rank = Rank.DEVELOPER)
public class LobbyCommand extends CoreCommand {

    public LobbyCommand() {
        super("lobby");
        registerSubCommand(new SetPackCommand());
        registerSubCommand(new SetSpawnCommand());
    }

    @Override
    protected boolean isUsingSubCommandsOnly() {
        return true;
    }
}
