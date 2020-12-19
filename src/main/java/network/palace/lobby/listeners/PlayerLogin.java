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

import java.util.Arrays;
import java.util.List;

public class PlayerLogin implements Listener {
    private static final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 200000, 0, true);
    private static final List<String> needTutorial = Arrays.asList("Legobuilder0813f", "a");

    @EventHandler
    public void onLogin(CorePlayerJoinedEvent event) {
        handleJoin(event.getPlayer());
    }

    public static void handleJoin(CPlayer player) {
        player.resetPlayer();
        player.setGamemode(GameMode.ADVENTURE);
        if (needTutorial.contains(player.getName())) {
            // Run tutorial for player
            Lobby.getTutorialManager().start(player);
        } else {
            // Player has run tutorial already
//            if (Lobby.getConfigUtil().getSpawn() != null) player.teleport(Lobby.getConfigUtil().getSpawn());
            Lobby.getScoreboardManager().handleLogin(player);
            Lobby.getInventoryUtil().handleJoin(player);
            player.addPotionEffect(speed);
            Lobby.getTutorialManager().hideForTutorialPlayers(player);
            if (player.getRank().getRankId() >= Rank.SPECIALGUEST.getRankId()) player.setAllowFlight(true);
        }
    }
}
