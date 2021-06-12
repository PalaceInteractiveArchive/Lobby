package network.palace.lobby.parkour;

import lombok.Getter;
import org.bukkit.Location;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class PlayerParkourUtil {
    @Getter private final UUID userId;
    @Getter private final LocalDateTime startTime;
    @Getter private LocalDateTime endTime;
    @Getter private Location lastCheckpoint;

    public PlayerParkourUtil(UUID userId) {
        this.userId = userId;
        this.startTime = LocalDateTime.now();
        this.endTime = null;
    }

    public long markEndTime() {
        endTime = LocalDateTime.now();
        return ChronoUnit.SECONDS.between(startTime, endTime);
    }

    public void updateCheckpoint(Location l) {
        lastCheckpoint = l;
    }

}
