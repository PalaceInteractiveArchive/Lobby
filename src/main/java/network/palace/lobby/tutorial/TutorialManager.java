package network.palace.lobby.tutorial;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.core.player.Rank;
import network.palace.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class TutorialManager {
    private final World world = Bukkit.getWorld("hub");
    private final Location spawn = new Location(world, 22.5, 64.0, 3.5, -90, 0);
    private final List<CPlayer> inTutorial = new ArrayList<>();

    public TutorialManager() {
        Core.runTaskTimer(Lobby.getInstance(), () -> {
            for (CPlayer tp : new ArrayList<>(inTutorial)) {
                if (tp == null || !tp.getRegistry().hasEntry("tutorial_start")) continue;
                long start = (long) tp.getRegistry().getEntry("tutorial_start");
                int secondsIn = (int) ((System.currentTimeMillis() - start) / 1000);
                switch (secondsIn) {
                    case 2: {
                        tp.sendMessage("Welcome!");
                        break;
                    }
                    case 10: {
                        tp.sendMessage("Done!");
                        done(tp);
                        break;
                    }
                }
            }
        }, 20L, 20L);
    }

    public void start(CPlayer player) {
        inTutorial.add(player);
        player.getRegistry().addEntry("tutorial_start", System.currentTimeMillis());
        player.setGamemode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setFlySpeed(0);
        player.setWalkSpeed(0);
        player.getInventory().clear();
        for (CPlayer tp : Core.getPlayerManager().getOnlinePlayers()) {
            // Hide every player from this player
            player.hidePlayer(Lobby.getInstance(), tp);
            // Hide this player from every player
            tp.hidePlayer(Lobby.getInstance(), player);
        }
        player.removePotionEffect(PotionEffectType.SPEED);
        player.teleport(spawn);
    }

    public void done(CPlayer player) {
        inTutorial.remove(player);
        player.getRegistry().removeEntry("tutorial_start");
        player.setFlySpeed(0.1f);
        player.setWalkSpeed(0.2f);
        player.setFlying(false);
        player.setAllowFlight(player.getRank().getRankId() >= Rank.SPECIALGUEST.getRankId());
        Lobby.getScoreboardManager().handleLogin(player);
        Lobby.getInventoryUtil().handleJoin(player);
        for (CPlayer tp : Core.getPlayerManager().getOnlinePlayers()) {
            if (tp.getRegistry().hasEntry("tutorial_start")) continue;
            player.showPlayer(Lobby.getInstance(), tp);
            tp.showPlayer(Lobby.getInstance(), player);
        }
    }

    public void hideForTutorialPlayers(CPlayer player) {
        for (CPlayer tp : inTutorial) {
            tp.hidePlayer(Lobby.getInstance(), player);
            player.hidePlayer(Lobby.getInstance(), tp);
        }
    }
}
