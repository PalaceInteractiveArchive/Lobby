package network.palace.lobby.tutorial.old.actions;

import network.palace.core.player.CPlayer;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class TeleportAction extends TutorialAction {
    private final Location loc;

    public TeleportAction(long time, Location loc) {
        super(time);
        this.loc = loc;
    }

    @Override
    public boolean run(CPlayer player) {
        Vector v = player.getVelocity();
        player.teleport(loc);
        player.setVelocity(v);
        return true;
    }
}
