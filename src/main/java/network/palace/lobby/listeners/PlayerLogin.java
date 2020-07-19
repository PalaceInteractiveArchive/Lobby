package network.palace.lobby.listeners;

import network.palace.core.events.CorePlayerJoinedEvent;
import network.palace.core.player.CPlayer;
import network.palace.core.player.CPlayerScoreboardManager;
import network.palace.core.player.Rank;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class PlayerLogin implements Listener {
    private static final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 200000, 0, true);
    private static List<String> needTutorial = Arrays.asList("Legobuilder0813", "a");

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
            if (Lobby.getConfigUtil().getSpawn() != null) player.teleport(Lobby.getConfigUtil().getSpawn());
            Lobby.getInventoryUtil().handleJoin(player);
            player.addPotionEffect(speed);
            if (player.getRank().getRankId() >= Rank.SPECIALGUEST.getRankId()) player.setAllowFlight(true);
            Lobby.getTutorialManager().hideForTutorialPlayers(player);
            CPlayerScoreboardManager manager = player.getScoreboard();
            manager.title(ChatColor.GOLD + "" + ChatColor.BOLD + "Palace " + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Network");
            int total = 9;
            String dash = ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "                        ." + ChatColor.RESET;
//        manager.set(total--, (player.getRank().getChatColor().equals(ChatColor.WHITE) ? player.getRank().getTagColor() : player.getRank().getChatColor()) + "" + ChatColor.ITALIC + player.getName());
//        manager.set(total--, dash);
            manager.setBlank(total--);
            manager.set(total--, ChatColor.GREEN + "Rank: " + player.getRank().getFormattedName());
            manager.set(total--, ChatColor.GREEN + "Level: " + ChatColor.AQUA + player.getLevel());
            manager.set(total--, ChatColor.GREEN + "Achievements: " + ChatColor.AQUA + "1,250");
            manager.setBlank(total--);
//        manager.set(total--, dash + " ");
//        manager.set(total--, dash + "  ");
            manager.set(total--, ChatColor.GREEN + "Hub: " + ChatColor.AQUA + "#1");
            manager.set(total--, ChatColor.GREEN + "Players: " + ChatColor.AQUA + "152");
//        manager.setBlank(total--);
            manager.setBlank(total--);
//        manager.set(total--, dash + "   ");
            manager.set(total, ChatColor.YELLOW + "play.palace.network");
            /*
            |
            |Rank: Director
            |Level: 35
            |Achievements: 1250
            |
            |Players: 152
            |
            |play.palace.network
             */
        }
    }
}
