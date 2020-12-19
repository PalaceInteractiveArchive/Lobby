package network.palace.lobby.tutorial.actions;

import network.palace.core.player.CPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class MoveEntityAction extends TutorialAction {
    // Permanent variables
    private final Entity entity;
    private final Location to;
    private final int ticks;

    // Initial variables
    private boolean first = true;
    private double dx, dy, dz;
    private float dyaw, dpitch;

    // Changing variables
    private int i = 0;

    public MoveEntityAction(long time, Entity entity, Location to, int ticks) {
        super(time);
        this.entity = entity;
        this.to = to;
        this.ticks = ticks;
    }

    @Override
    public boolean run(CPlayer player) {
        if (entity == null || entity.isDead()) {
            return true;
        }
        Location current = null;

        if (first) {
            // Initialize variables
            Location from = entity.getLocation();
            dx = (to.getX() - from.getX()) / ticks;
            dy = (to.getY() - from.getY()) / ticks;
            dz = (to.getZ() - from.getZ()) / ticks;
            dyaw = (to.getYaw() - from.getYaw()) / ticks;
            dpitch = (to.getPitch() - from.getPitch()) / ticks;
            current = from;
            first = false;
        }
        if (current == null) current = entity.getLocation();

        // Move by dx,dy,dz,dyaw,dpitch values
        current.add(dx, dy, dz);
        current.setYaw(current.getYaw() + dyaw);
        current.setPitch(current.getPitch() + dpitch);
        entity.teleport(current);

        // Finished if number of ticks have passed, otherwise conitnuing
        return ++i >= ticks;
    }
}
