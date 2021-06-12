package network.palace.lobby.parkour;

import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class ParkourCountingTask extends BukkitRunnable {

    private long time = -1;
    private final CPlayer p;


    public ParkourCountingTask(CPlayer p) {
        this.p = p;
    }

    @Override
    public void run() {
        if (!Lobby.getParkourUtil().checkInParkour(p)) {
            this.cancel();
        } else {
            time++;
            p.getActionBar().show(ChatColor.AQUA + String.format("%02d:%02d:%02d", time / 3600, (time / 60) % 60, time % 60));
        }
    }

}
