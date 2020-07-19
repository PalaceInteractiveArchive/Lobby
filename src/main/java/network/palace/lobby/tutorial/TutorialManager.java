package network.palace.lobby.tutorial;

import net.md_5.bungee.api.ChatColor;
import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.core.player.impl.managers.CorePlayerManager;
import network.palace.lobby.Lobby;
import network.palace.lobby.tutorial.actions.GameModeAction;
import network.palace.lobby.tutorial.actions.MessageAction;
import network.palace.lobby.tutorial.actions.TeleportAction;
import network.palace.lobby.tutorial.actions.TutorialAction;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TutorialManager {
    private static final PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 21 * 20, 0, true);
    private static final PotionEffect invisible = new PotionEffect(PotionEffectType.INVISIBILITY, 10000 * 20, 0, true);
    private final World world = Bukkit.getWorld("hub");
    private final Location spawn = new Location(world, -14.5, 20, -105.5, -90, 0);
    private final List<CPlayer> inTutorial = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public TutorialManager() {
        Core.runTaskTimer(Lobby.getInstance(), () -> {
            List<CPlayer> tutorialDone = new ArrayList<>();
            inTutorial.forEach(tp -> {
                if (!tp.getRegistry().hasEntry("tutorial_actions")) return;
                List<TutorialAction> actions = (List<TutorialAction>) tp.getRegistry().getEntry("tutorial_actions");
                if (actions.isEmpty()) return;
                long startTime = (long) tp.getRegistry().getEntry("tutorial_start");
                List<TutorialAction> toRemove = new ArrayList<>();
                actions.forEach(action -> {
                    if (System.currentTimeMillis() - startTime >= action.getTime()) {
                        action.run(tp);
                        toRemove.add(action);
                    }
                });
                actions.removeAll(toRemove);
                if (actions.isEmpty()) tutorialDone.add(tp);
            });
            inTutorial.removeAll(tutorialDone);
        }, 0L, 1L);
    }

    public void start(CPlayer player) {
        inTutorial.add(player);
        player.teleport(spawn);
        player.addPotionEffect(blindness);
        ((CorePlayerManager) Core.getPlayerManager()).getDefaultScoreboard().disableDefaultScoreboard(player.getUniqueId());
        player.getScoreboard().clear();
        Core.getPlayerManager().displayRank(player);
        for (CPlayer tp : Core.getPlayerManager().getOnlinePlayers()) {
            player.hidePlayer(Lobby.getInstance(), tp);
            tp.hidePlayer(Lobby.getInstance(), player);
        }
//        ArmorStand s = spawn.getWorld().spawn(new Location(spawn.getWorld(), -15.2, 52.5, -105.5, -90, -90), ArmorStand.class);
//        s.setGravity(false);
//        s.setVisible(false);
        player.setGamemode(GameMode.SPECTATOR);
//        player.getBukkitPlayer().setSpectatorTarget(s);
        List<TutorialAction> actions = new ArrayList<>(Arrays.asList(
                new MessageAction(0, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"),
                new MessageAction(2000, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.BLACK + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(2250, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.DARK_GRAY + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(2500, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.GRAY + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(2750, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(6000, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(6500, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I w" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(6750, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I wa" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(7000, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I wan" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(7250, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(7750, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want t" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(8000, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(8500, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to g" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(8750, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(9250, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go o" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(9500, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go on" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(10000, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go on a" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(10250, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go on an" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(10750, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go on an a" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(11000, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go on an ad" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(11250, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go on an adv" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(11500, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go on an adve" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(11750, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go on an adven" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(12000, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go on an advent" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(12250, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go on an adventu" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(12500, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go on an adventur" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(12750, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go on an adventure" +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(13000, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go on an adventure." +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(13300, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go on an adventure.." +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(13600, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + "" + ChatColor.ITALIC + "I want to go on an adventure..." +
                        ChatColor.BLACK + "\n\n"
                ),
                new MessageAction(15000, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.WHITE + player.getName() + ":\n" +
                        ChatColor.DARK_GRAY + "   " + ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "I want to go on an " + ChatColor.GOLD + ChatColor.ITALIC + "adventure..." +
                        ChatColor.BLACK + "\n\n"
                ),
                new TeleportAction(19500, new Location(spawn.getWorld(), -14.5, 62.2, -105.5, -90, 0)),
                new GameModeAction(19500, GameMode.SURVIVAL),
                new MessageAction(19750, ChatColor.RESET + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")
        ));
        player.getRegistry().addEntry("tutorial_start", System.currentTimeMillis());
        player.getRegistry().addEntry("tutorial_actions", actions);
    }

    public void hideForTutorialPlayers(CPlayer player) {
        for (CPlayer tp : inTutorial) {
            tp.hidePlayer(Lobby.getInstance(), player);
            player.hidePlayer(Lobby.getInstance(), tp);
        }
    }
}
