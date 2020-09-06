package network.palace.lobby.npc.paths;

import net.citizensnpcs.api.command.CommandContext;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.util.DataKey;
import net.citizensnpcs.trait.waypoint.WaypointEditor;
import net.citizensnpcs.trait.waypoint.WaypointProvider;
import org.bukkit.command.CommandSender;

public class PathProvider implements WaypointProvider {

    @Override
    public WaypointEditor createEditor(CommandSender commandSender, CommandContext commandContext) {
        return null;
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public void onRemove() {

    }

    @Override
    public void onSpawn(NPC npc) {

    }

    @Override
    public void setPaused(boolean b) {

    }

    @Override
    public void load(DataKey dataKey) {

    }

    @Override
    public void save(DataKey dataKey) {

    }
}
