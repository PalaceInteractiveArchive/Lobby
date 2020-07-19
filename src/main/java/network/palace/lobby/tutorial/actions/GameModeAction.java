package network.palace.lobby.tutorial.actions;

import network.palace.core.player.CPlayer;
import org.bukkit.GameMode;

public class GameModeAction extends TutorialAction {
    private final GameMode mode;

    public GameModeAction(long time, GameMode mode) {
        super(time);
        this.mode = mode;
    }

    @Override
    public void run(CPlayer player) {
        player.setGamemode(mode);
    }
}
