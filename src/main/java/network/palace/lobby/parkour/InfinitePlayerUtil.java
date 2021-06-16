package network.palace.lobby.parkour;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.UUID;

public class InfinitePlayerUtil {
    @Getter private final UUID userId;
    @Getter
    @Setter
    private Location blockOne;
    @Getter
    @Setter
    private Location blockTwo;
    @Getter
    @Setter
    private Location blockThree;
    @Getter int score;
    @Getter
    @Setter
    private int colour;

    public InfinitePlayerUtil(UUID userId) {
        this.userId = userId;
    }

    public void increaseScore() {
        this.score++;
    }

}
