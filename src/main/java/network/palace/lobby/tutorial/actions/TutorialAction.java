package network.palace.lobby.tutorial.actions;

import lombok.Getter;
import network.palace.core.player.CPlayer;

public abstract class TutorialAction {
    @Getter private final long time;

    public TutorialAction(long time) {
        this.time = time;
    }

    public abstract boolean run(CPlayer player);
}
