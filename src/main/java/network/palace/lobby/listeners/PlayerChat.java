package network.palace.lobby.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.core.player.Rank;
import network.palace.core.player.RankTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        CPlayer player = Core.getPlayerManager().getPlayer(event.getPlayer());
        if (player == null || player.getRegistry().hasEntry("tutorial_start")) return;
        Rank rank = player.getRank();
        String msg;
        if (rank.getRankId() >= Rank.TRAINEE.getRankId()) {
            msg = ChatColor.translateAlternateColorCodes('&', event.getMessage());
        } else {
            msg = event.getMessage();
        }
        BaseComponent[] messageToSend = new ComponentBuilder(RankTag.formatChat(player.getTags())).event(getPlayerHover(player, Core.getInstanceName()))
                .append(rank.getFormattedName() + " ")
                .append(player.getName() + ": ").color(ChatColor.GRAY)
                .append(msg, ComponentBuilder.FormatRetention.NONE).color(ChatColor.getByChar(rank.getChatColor().getChar())).create();
        for (CPlayer tp : Core.getPlayerManager().getOnlinePlayers()) {
            tp.getBukkitPlayer().spigot().sendMessage(messageToSend);
        }

    }

    private HoverEvent getPlayerHover(CPlayer player, String server) {
        ComponentBuilder builder = new ComponentBuilder(player.getRank().getFormattedName())
                .append(" " + player.getName() + "\n").color(ChatColor.GRAY);
        for (RankTag tag : player.getTags()) {
            builder.append(tag.getName() + "\n").color(ChatColor.getByChar(tag.getColor().getChar())).italic(true);
        }
        builder.append("Server: ", ComponentBuilder.FormatRetention.NONE).color(ChatColor.AQUA).append(server).color(ChatColor.GREEN);
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, builder.create());
    }
}
