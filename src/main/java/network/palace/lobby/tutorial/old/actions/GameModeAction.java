package network.palace.lobby.tutorial.old.actions;

import network.palace.core.player.CPlayer;
import org.bukkit.GameMode;

public class GameModeAction extends TutorialAction {
    private final GameMode mode;

    public GameModeAction(long time, GameMode mode) {
        super(time);
        this.mode = mode;
    }

    @Override
    public boolean run(CPlayer player) {
        player.setGamemode(mode);
        return true;
    }
}