package network.palace.lobby.listeners;

import network.palace.core.events.CorePlayerJoinedEvent;
import network.palace.core.player.CPlayer;
import network.palace.core.player.Rank;
import network.palace.lobby.Lobby;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerLogin implements Listener {
    private static final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 200000, 0, true);

    @EventHandler
    public void onLogin(CorePlayerJoinedEvent event) {
        handleJoin(event.getPlayer());
    }

    public static void handleJoin(CPlayer player) {
        player.resetPlayer();
        if (Lobby.getConfigUtil().getSpawn() != null) player.teleport(Lobby.getConfigUtil().getSpawn());
        player.setGamemode(GameMode.ADVENTURE);
        Lobby.getInventoryUtil().handleJoin(player);
        player.addPotionEffect(speed);
        if (player.getRank().getRankId() >= Rank.SPECIALGUEST.getRankId()) player.setAllowFlight(true);
    }
}
