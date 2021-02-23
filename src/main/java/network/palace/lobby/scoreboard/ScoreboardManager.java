package network.palace.lobby.scoreboard;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.core.player.CPlayerScoreboardManager;
import network.palace.core.player.RankTag;
import network.palace.lobby.Lobby;
import org.bukkit.ChatColor;

import java.util.List;

public class ScoreboardManager {

    public void handleLogin(CPlayer player) {
        CPlayerScoreboardManager manager = player.getScoreboard();
        manager.title(ChatColor.GOLD + "" + ChatColor.BOLD + "Palace " + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Network");
        List<RankTag> tags = player.getTags();
        int total = 7 + (tags.isEmpty() ? 0 : (tags.size() + 1));
        String dash = ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "                        ." + ChatColor.RESET;
//        manager.set(total--, (player.getRank().getChatColor().equals(ChatColor.WHITE) ? player.getRank().getTagColor() : player.getRank().getChatColor()) + "" + ChatColor.ITALIC + player.getName());
//        manager.set(total--, dash);
        manager.setBlank(total--);
        manager.set(total--, ChatColor.GREEN + "Rank: " + player.getRank().getFormattedName());
//        manager.set(total--, ChatColor.GREEN + "Level: " + ChatColor.AQUA + player.getLevel());
//        manager.set(total--, ChatColor.GREEN + "Achievements: " + ChatColor.AQUA + "1,250");
        if (!tags.isEmpty()) {
            manager.setBlank(total--);
            for (int i = tags.size(); i > 0; i--) {
                RankTag tag = tags.get(tags.size() - i);
                manager.set(total--, tag.getColor() + "" + ChatColor.ITALIC + "" + tag.getName());
            }
        }
        manager.setBlank(total--);
//        manager.set(total--, dash + " ");
//        manager.set(total--, dash + "  ");
        manager.set(total--, ChatColor.GREEN + "Hub: " + ChatColor.AQUA + "#1");
        manager.set(total--, ChatColor.GREEN + "Players: " + ChatColor.AQUA + Lobby.getConfigUtil().getLastOnline());
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

    public void updateCounts(int online) {
        for (CPlayer tp : Core.getPlayerManager().getOnlinePlayers()) {
            CPlayerScoreboardManager manager = tp.getScoreboard();
            manager.remove(3);
            manager.set(3, ChatColor.GREEN + "Players: " + ChatColor.AQUA + online);
        }
    }
}
