package network.palace.lobby.tutorial;

import network.palace.core.Core;
import network.palace.core.player.CPlayer;
import network.palace.core.player.impl.managers.CorePlayerManager;
import network.palace.lobby.Lobby;
import network.palace.lobby.tutorial.actions.MessageAction;
import network.palace.lobby.tutorial.actions.TutorialAction;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TutorialManager {
    private static final PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 100, 0, true);
    private static final PotionEffect invisible = new PotionEffect(PotionEffectType.INVISIBILITY, 10000 * 20, 0, true);
    private final World world = Bukkit.getWorld("hub");
    private final Location spawn = new Location(world, 58.5, 71, 22.5, -90, -90);
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
                        if (action.run(tp)) toRemove.add(action);
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
        player.getInventory().clear();
        player.setGamemode(GameMode.ADVENTURE);
        ((CorePlayerManager) Core.getPlayerManager()).getDefaultScoreboard().disableDefaultScoreboard(player.getUniqueId());
        player.getScoreboard().clear();
        Core.getPlayerManager().displayRank(player);
        for (CPlayer tp : Core.getPlayerManager().getOnlinePlayers()) {
            player.hidePlayer(Lobby.getInstance(), tp);
            tp.hidePlayer(Lobby.getInstance(), player);
        }
        player.removePotionEffect(PotionEffectType.SPEED);
        player.addPotionEffect(blindness);

        ArmorStand s = spawn.getWorld().spawn(new Location(spawn.getWorld(), 58.5, 71.1, 22.5, -90, -90), ArmorStand.class);
        s.setGravity(false);
        s.setVisible(false);

        List<TutorialAction> actions = new ArrayList<>(Arrays.asList(
                new TutorialAction(0) {
                    @Override
                    public boolean run(CPlayer player) {
                        Location loc = player.getLocation();
                        loc.setYaw(-90);
                        loc.setPitch(-90);
                        player.teleport(loc);
                        player.getRegistry().addEntry("tutorial_first_scene", true);
                        return true;
                    }
                },
                new TutorialAction(50) {
                    @Override
                    public boolean run(CPlayer player) {
                        s.addPassenger(player.getBukkitPlayer());
                        return true;
                    }
                },
                new TutorialAction(2450) {
                    @Override
                    public boolean run(CPlayer player) {
//                        s.remove();
//                        player.setGamemode(GameMode.SPECTATOR);
                        return true;
                    }
                },
                new TutorialAction(2500) {
                    @Override
                    public boolean run(CPlayer player) {
//                        player.teleport(s2.getLocation());
//                        player.getBukkitPlayer().setSpectatorTarget(s2);
                        return true;
                    }
                },
//                new MoveEntityAction(4000, s2, to, 40),
                new MessageAction(7000, ChatColor.GRAY + "Man: " + ChatColor.WHITE + "Oh, hello there. You're awake!"),
                new MessageAction(10500, ChatColor.GRAY + "Man: " + ChatColor.WHITE + "I’m sure you’re wondering where you are. When you’re ready, come sit and we’ll talk."),
                new TutorialAction(15500) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.getRegistry().addEntry("tutorial_leave_bed", true);
                        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Leave the bed by holding " + ChatColor.AQUA + "Left-Shift");
                        return true;
                    }
                }
//        new MessageAction(2000, ChatColor.AQUA + "Would you like to connect to the Audio Server to hear music and sounds?"),
//                new TutorialAction(4000) {
//                    @Override
//                    public boolean run(CPlayer player) {
//                        FormattedMessage msg = new FormattedMessage("Yes").color(ChatColor.GREEN).command("tutorial audio yes").tooltip(ChatColor.AQUA + "Click to connect to the Audio Server")
//                                .then("\nClick Yes or No\n").color(ChatColor.GRAY).style(ChatColor.ITALIC)
//                                .then("No").color(ChatColor.RED).command("tutorial audio no").tooltip(ChatColor.AQUA + "Click to skip using the Audio Server (you can connect later!)");
//                        msg.send(player);
//                    }
//                }
        ));
        player.getRegistry().addEntry("tutorial_start", System.currentTimeMillis());
        player.getRegistry().addEntry("tutorial_actions", actions);
    }

    public void oldTutorial(CPlayer player) {
        inTutorial.add(player);
        Location spawn = new Location(world, -34.5, 56.0, -109.5, -135, 0);
        player.teleport(spawn);
        player.addPotionEffect(blindness);
        ((CorePlayerManager) Core.getPlayerManager()).getDefaultScoreboard().disableDefaultScoreboard(player.getUniqueId());
//        player.getScoreboard().clear();
        Core.getPlayerManager().displayRank(player);
        for (CPlayer tp : Core.getPlayerManager().getOnlinePlayers()) {
            player.hidePlayer(Lobby.getInstance(), tp);
            tp.hidePlayer(Lobby.getInstance(), player);
        }
        ArmorStand s = spawn.getWorld().spawn(new Location(spawn.getWorld(), -34.2, 47.5, -109.5, -135, -15), ArmorStand.class);
        s.setGravity(false);
        s.setVisible(false);
        List<TutorialAction> actions = new ArrayList<>(Arrays.asList(
                new TutorialAction(0) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.teleport(spawn);
                        return true;
                    }
                },
                new TutorialAction(250) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.setFlying(false);
                        player.setAllowFlight(false);
                        player.teleport(spawn);
                        return true;
                    }
                },
                new TutorialAction(1000) {
                    @Override
                    public boolean run(CPlayer player) {
                        Location loc = player.getLocation();
                        loc.setYaw(-135);
                        loc.setPitch(-36);
                        player.getBukkitPlayer().teleport(loc);
                        player.setGamemode(GameMode.ADVENTURE);
                        s.addPassenger(player.getBukkitPlayer());
                        return true;
                    }
                },
                new TutorialAction(4300) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.WHITE + "" + ChatColor.ITALIC + "Another one came through!");
                        return true;
                    }
                },
                new TutorialAction(6500) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.WHITE + "" + ChatColor.ITALIC + "Over here!");
                        return true;
                    }
                },
                new TutorialAction(9750) {
                    @Override
                    public boolean run(CPlayer player) {
                        s.removePassenger(player.getBukkitPlayer());
                        s.teleport(new Location(spawn.getWorld(), -34.2, 48.5, -109.5, -135, -15));
                        return true;
                    }
                },
                new TutorialAction(10000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.setGamemode(GameMode.SPECTATOR);
                        player.getBukkitPlayer().setSpectatorTarget(s);
                        return true;
                    }
                },
                new TutorialAction(11200) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "Man: " + ChatColor.WHITE + "There you are! You took quite a fall, there.");
                        return true;
                    }
                },
                new TutorialAction(14600) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "Man: " + ChatColor.WHITE + "Here, let's get you sitting up.");
                        return true;
                    }
                },
                new TutorialAction(18000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.setGamemode(GameMode.ADVENTURE);
                        s.addPassenger(player.getBukkitPlayer());
                        return true;
                    }
                },
                new TutorialAction(20500) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "Man: " + ChatColor.WHITE + "A lot of people have come through recently. So, we made the landing a little softer, ha ha!");
                        return true;
                    }
                },
                new TutorialAction(26800) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "You: " + ChatColor.WHITE + "Through?");
                        return true;
                    }
                },
                new TutorialAction(27900) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "You: " + ChatColor.WHITE + "Through what?");
                        return true;
                    }
                },
                new TutorialAction(30000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "Man: " + ChatColor.WHITE + "That portal up there! Ya see?");
                        return true;
                    }
                },
                new TutorialAction(38000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "Man: " + ChatColor.WHITE + "What's your name?");
                        return true;
                    }
                },
                new TutorialAction(41000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "You: " + ChatColor.WHITE + player.getName());
                        return true;
                    }
                },
                new TutorialAction(43700) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "Man: " + ChatColor.WHITE + "Nice to meet you, " + player.getName() + ". I'm " + ChatColor.AQUA + "Elred" + ChatColor.WHITE + ". I've been keeping an eye out for people such as yourself that come through that portal.");
                        return true;
                    }
                },
                new TutorialAction(51700) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "Some hikers came across the portal recently. No one knows where it came from.");
                        return true;
                    }
                },
                new TutorialAction(57000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "Then, people started coming through!");
                        return true;
                    }
                },
                new TutorialAction(60000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "The only thing people seem to remember is their name. You don't remember what led you here, do you?");
                        return true;
                    }
                },
                new TutorialAction(66000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + player.getName() + ": " + ChatColor.WHITE + "No...");
                        return true;
                    }
                },
                new TutorialAction(69000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "It's certainly strange... Some of the settlers here are trying to get to the bottom of it.");
                        return true;
                    }
                },
                new TutorialAction(75500) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + player.getName() + ": " + ChatColor.WHITE + "Where am I?");
                        return true;
                    }
                },
                new TutorialAction(77500) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "Oh, right! How silly of me.");
                        return true;
                    }
                },
                new TutorialAction(80250) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "You're in the " + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Kingdom of Ravelia!");
                        return true;
                    }
                },
                new TutorialAction(84000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "Right now, you're on " + ChatColor.AQUA + "Lotus Island" + ChatColor.WHITE + ". It's the central island of the kingdom.");
                        return true;
                    }
                },
                new TutorialAction(89000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "Let's get you out of here and into town.");
                        return true;
                    }
                },
                new TutorialAction(93000) {
                    @Override
                    public boolean run(CPlayer player) {
                        s.removePassenger(player.getBukkitPlayer());
                        return true;
                    }
                },
                new TutorialAction(97000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "Hold on, I almost forgot! Just a moment...");
                        return true;
                    }
                },
                new TutorialAction(101000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.LIGHT_PURPLE + "" + ChatColor.MAGIC + "Abrakadbra");
                        return true;
                    }
                },
                new TutorialAction(103000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + "Alakazam");
                        return true;
                    }
                },
                new TutorialAction(106000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.getParticles().send(new Location(player.getWorld(), -27.5, 52.5, -112.1), Particle.FIREWORKS_SPARK, 25, 0.7f, 0.3f, 0.1f, 0.1f);
                        return true;
                    }
                },
                new TutorialAction(111000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.AQUA + "Elred: " + ChatColor.WHITE + "What numbers do you see on the wall, there? In order, please.");
                        return true;
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
