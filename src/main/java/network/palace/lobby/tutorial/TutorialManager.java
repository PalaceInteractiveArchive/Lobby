package network.palace.lobby.tutorial;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.core.player.impl.managers.CorePlayerManager;
import network.palace.lobby.Lobby;
import network.palace.lobby.tutorial.actions.TutorialAction;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TutorialManager {
    private static final PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 200, 0, true);
    private static final PotionEffect invisible = new PotionEffect(PotionEffectType.INVISIBILITY, 10000 * 20, 0, true);
    private final World world = Bukkit.getWorld("hub");
    private final Location spawn = new Location(world, -34.5, 56.0, -109.5, -135, 0);
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
        ArmorStand s = spawn.getWorld().spawn(new Location(spawn.getWorld(), -34.2, 47.5, -109.5, -135, -36), ArmorStand.class);
        s.setGravity(false);
        s.setVisible(false);
        List<TutorialAction> actions = new ArrayList<>(Arrays.asList(
                new TutorialAction(0) {
                    @Override
                    public void run(CPlayer player) {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.teleport(spawn);
                    }
                },
                new TutorialAction(250) {
                    @Override
                    public void run(CPlayer player) {
                        player.setFlying(false);
                        player.setAllowFlight(false);
                        player.teleport(spawn);
                        player.getBukkitPlayer().getEyeHeight();
                    }
                },
                new TutorialAction(1000) {
                    @Override
                    public void run(CPlayer player) {
                        Location loc = player.getLocation();
                        loc.setYaw(-135);
                        loc.setPitch(-36);
                        player.getBukkitPlayer().teleport(loc);
                        player.setGamemode(GameMode.ADVENTURE);
                        s.addPassenger(player.getBukkitPlayer());
                    }
                },
                new TutorialAction(4300) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.WHITE + "" + ChatColor.ITALIC + "Another one came through!");
                    }
                },
                new TutorialAction(6500) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.WHITE + "" + ChatColor.ITALIC + "Over here!");
                    }
                },
                new TutorialAction(9750) {
                    @Override
                    public void run(CPlayer player) {
                        s.removePassenger(player.getBukkitPlayer());
                        s.teleport(new Location(spawn.getWorld(), -34.2, 48.5, -109.5, -135, -36));
                    }
                },
                new TutorialAction(10000) {
                    @Override
                    public void run(CPlayer player) {
                        player.setGamemode(GameMode.SPECTATOR);
                        player.getBukkitPlayer().setSpectatorTarget(s);
                    }
                },
                new TutorialAction(11200) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "Man: " + ChatColor.WHITE + "There you are! You took quite a fall, there.");
                    }
                },
                new TutorialAction(14600) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "Man: " + ChatColor.WHITE + "Here, let's get you sitting up.");
                    }
                },
                new TutorialAction(18000) {
                    @Override
                    public void run(CPlayer player) {
                        player.setGamemode(GameMode.ADVENTURE);
                        s.addPassenger(player.getBukkitPlayer());
                    }
                },
                new TutorialAction(20500) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "Man: " + ChatColor.WHITE + "A lot of people have come through recently. So, we made the landing a little softer, ha ha!");
                    }
                },
                new TutorialAction(26800) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "You: " + ChatColor.WHITE + "Through?");
                    }
                },
                new TutorialAction(27900) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "You: " + ChatColor.WHITE + "Through what?");
                    }
                },
                new TutorialAction(30000) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "Man: " + ChatColor.WHITE + "That portal up there! Ya see?");
                    }
                },
                new TutorialAction(38000) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "Man: " + ChatColor.WHITE + "What's your name?");
                    }
                },
                new TutorialAction(41000) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "You: " + ChatColor.WHITE + player.getName());
                    }
                },
                new TutorialAction(43700) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "Man: " + ChatColor.WHITE + "Nice to meet you, " + player.getName() + ". I'm " + ChatColor.AQUA + "Elred" + ChatColor.WHITE + ". I've been keeping an eye out for people such as yourself that come through that portal.");
                    }
                },
                new TutorialAction(51700) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "Some hikers came across the portal recently. No one knows where it came from.");
                    }
                },
                new TutorialAction(57000) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "Then, people started coming through!");
                    }
                },
                new TutorialAction(60000) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "The only thing people seem to remember is their name. You don't remember what led you here, do you?");
                    }
                },
                new TutorialAction(66000) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + player.getName() + ": " + ChatColor.WHITE + "No...");
                    }
                },
                new TutorialAction(69000) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "It's certainly strange... Some of the settlers here are trying to get to the bottom of it.");
                    }
                },
                new TutorialAction(75500) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + player.getName() + ": " + ChatColor.WHITE + "Where am I?");
                    }
                },
                new TutorialAction(77500) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "Oh, right! How silly of me.");
                    }
                },
                new TutorialAction(80250) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "You're in the " + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Kingdom of Ravelia!");
                    }
                },
                new TutorialAction(84000) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "Right now, you're on " + ChatColor.AQUA + "Lotus Island" + ChatColor.WHITE + ". It's the central island of the kingdom.");
                    }
                },
                new TutorialAction(89000) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "Let's get you out of here and into town.");
                    }
                },
                new TutorialAction(93000) {
                    @Override
                    public void run(CPlayer player) {
                        s.removePassenger(player.getBukkitPlayer());
                    }
                },
                new TutorialAction(97000) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "Hold on, I almost forgot! Just a moment...");
                    }
                },
                new TutorialAction(101000) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.LIGHT_PURPLE + "" + ChatColor.MAGIC + "Abrakadbra");
                    }
                },
                new TutorialAction(103000) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + "Alakazam");
                    }
                },
                new TutorialAction(106000) {
                    @Override
                    public void run(CPlayer player) {
                        player.getParticles().send(new Location(player.getWorld(), -27.5, 52.5, -112.1), Particle.FIREWORKS_SPARK, 25, 0.7f, 0.3f, 0.1f, 0.1f);
                    }
                },
                new TutorialAction(111000) {
                    @Override
                    public void run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "What numbers do you see on the wall, there? In order, please.");
                    }
                }
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
