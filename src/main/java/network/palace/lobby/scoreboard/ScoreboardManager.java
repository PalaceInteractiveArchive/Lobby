package network.palace.lobby.scoreboard;

import network.palace.core.player.CPlayer;
import network.palace.core.player.CPlayerScoreboardManager;
import org.bukkit.ChatColor;

public class ScoreboardManager {

    public void handleLogin(CPlayer player) {
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
