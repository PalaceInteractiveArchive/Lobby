package network.palace.lobby.npc.traits;

import lombok.Getter;
import net.citizensnpcs.api.trait.Trait;

import java.util.UUID;

public class TutorialNPC extends Trait {
    @Getter private final UUID targetPlayer;

    public TutorialNPC(UUID targetPlayer) {
        super("tutorialnpc");
        this.targetPlayer = targetPlayer;
    }
}
