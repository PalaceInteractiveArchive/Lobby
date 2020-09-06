package network.palace.lobby.npc;

import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
import network.palace.lobby.npc.traits.TutorialNPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class NPCListener implements Listener {

    @EventHandler
    public void onNPCSpawn(NPCSpawnEvent event) {
        NPC npc = event.getNPC();
        if (!npc.hasTrait(TutorialNPC.class)) return;
        TutorialNPC tutorialNPC = npc.getTrait(TutorialNPC.class);
        UUID targetPlayer = tutorialNPC.getTargetPlayer();
        for (CPlayer tp : Core.getPlayerManager().getOnlinePlayers()) {
            if (tp.getUniqueId().equals(targetPlayer)) continue;
            tp.getBukkitPlayer().hidePlayer(Lobby.getInstance(), (Player) npc.getEntity());
        }
    }
}
