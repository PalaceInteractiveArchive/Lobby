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
import java.util.Random;

public class TutorialManager {
    private static final PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 100, 0, true);
    private static final PotionEffect invisible = new PotionEffect(PotionEffectType.INVISIBILITY, 10000 * 20, 0, true);
    private final World world = Bukkit.getWorld("hub");
    private final Location spawn = new Location(world, 58.5, 71, 22.5, -90, -90);
    private final List<CPlayer> inTutorial = new ArrayList<>();
    private final Random random;

    @SuppressWarnings("unchecked")
    public TutorialManager() {
        random = new Random();
        Location chairParticles = new Location(world, 63.5, 73.1, 19.5);
        Location compassParticles = new Location(world, 63.5, 73.3, 12.5);
        Location bookParticles = new Location(world, 62.5, 75.6, 25.5);
        Location bagParticles = new Location(world, 57.5, 72.4, 17.5);
        Core.runTaskTimer(Lobby.getInstance(), new Runnable() {
            int i = 1;

            @Override
            public void run() {
                List<CPlayer> tutorialDone = new ArrayList<>();
                inTutorial.forEach(tp -> {
                    if (i % 10 == 0 && tp.getRegistry().hasEntry("tutorial_particle")) {
                        switch ((String) tp.getRegistry().getEntry("tutorial_particle")) {
                            case "chair": {
                                tp.getParticles().send(chairParticles, Particle.VILLAGER_HAPPY, 2, 0.35f, 0.15f, 0.35f, 0.1f);
                                break;
                            }
                            case "items": {
                                if (tp.getRegistry().hasEntry("tutorial_compass"))
                                    tp.getParticles().send(compassParticles, Particle.VILLAGER_HAPPY, 2, 0.15f, 0.15f, 0.15f, 0.1f);
                                if (tp.getRegistry().hasEntry("tutorial_book"))
                                    tp.getParticles().send(bookParticles, Particle.VILLAGER_HAPPY, 2, 0.15f, 0.15f, 0.15f, 0.1f);
                                if (tp.getRegistry().hasEntry("tutorial_bag"))
                                    tp.getParticles().send(bagParticles, Particle.VILLAGER_HAPPY, 2, 0.15f, 0.15f, 0.15f, 0.1f);
                                if (!tp.getRegistry().hasEntry("tutorial_compass") && !tp.getRegistry().hasEntry("tutorial_book") && !tp.getRegistry().hasEntry("tutorial_bag"))
                                    tp.getParticles().send(chairParticles, Particle.VILLAGER_HAPPY, 2, 0.35f, 0.15f, 0.35f, 0.1f);
                                break;
                            }
                        }
                    }
                    if (!tp.getRegistry().hasEntry("tutorial_start")) {
                        tutorialDone.add(tp);
                        return;
                    }
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
                });
                inTutorial.removeAll(tutorialDone);
                if (++i > 20) i = 0;
            }
        }, 0L, 1L);
    }

    public void start(CPlayer player) {
        inTutorial.add(player);
        player.teleport(spawn);
        player.getInventory().clear();
        player.setGamemode(GameMode.ADVENTURE);
        ((CorePlayerManager) Core.getPlayerManager()).getDefaultScoreboard().disableDefaultScoreboard(player.getUniqueId());
        player.getScoreboard().clear();
//        Core.getPlayerManager().displayRank(player);
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
                new MessageAction(7000, ChatColor.GRAY + "Man: " + ChatColor.WHITE + "Oh, hello there. You're awake!"),
                new MessageAction(10500, ChatColor.GRAY + "Man: " + ChatColor.WHITE + "I’m sure you’re wondering where you are. When you’re ready, come sit and we’ll talk."),
                new TutorialAction(15500) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.getRegistry().addEntry("tutorial_leave_bed", true);
                        player.getRegistry().addEntry("tutorial_particle", "chair");
                        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Leave the bed by holding " + ChatColor.AQUA + "Left-Shift");
                        return true;
                    }
                }
        ));
        player.getRegistry().addEntry("tutorial_start", System.currentTimeMillis());
        player.getRegistry().addEntry("tutorial_actions", actions);
    }

    public void startTableDiscussion(CPlayer player) {
        player.getRegistry().removeEntry("tutorial_particle");
        List<TutorialAction> actions = new ArrayList<>(Arrays.asList(
                new MessageAction(2000, ChatColor.GRAY + "Man: " + ChatColor.WHITE + "Hi there, my name is Elred. What's your name?"),
                new TutorialAction(7000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "You: " + ChatColor.WHITE + player.getName());
                        return true;
                    }
                },
                new TutorialAction(10000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "Nice to meet you, " + player.getName() + ".");
                        return true;
                    }
                },
                new MessageAction(13000, ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "Welcome to the " + ChatColor.LIGHT_PURPLE + "Kingdom of Ravelia!"),
                new MessageAction(17000, ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "Right now, you’re on " + ChatColor.GREEN + "Lotus Island." +
                        ChatColor.WHITE + " It’s the central island of the kingdom."),
                new MessageAction(22000, ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "Because of that, we usually call it " + ChatColor.GREEN + "Hub Island."),
                new MessageAction(28000, ChatColor.GRAY + "You: " + ChatColor.WHITE + "How did I get here?"),
                new MessageAction(31000, ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "Do you remember where you were before?"),
                new MessageAction(34000, ChatColor.GRAY + "You: " + ChatColor.WHITE + "..."),
                new MessageAction(36000, ChatColor.GRAY + "You: " + ChatColor.WHITE + "No."),
                new MessageAction(38000, ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "That's alright. Lots of new settlers have started to appear recently. They don't remember their past either."),
                new MessageAction(44000, ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "We're excited that our kingdom is growing! We're just not sure where everyone's coming from."),
                new TutorialAction(50500) {
                    @Override
                    public boolean run(CPlayer player) {
                        List<String> options = Arrays.asList("on the shore near the island's port.",
                                "in the forest to the west of the town center.",
                                "near the castle walls at the east edge of the island.");
                        int rand = random.nextInt(options.size());
                        player.sendMessage(ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "You were found " + options.get(rand));
                        return true;
                    }
                },
                new MessageAction(55000, ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "Some of the settlers here are trying to get to the bottom of it."),
                new MessageAction(61000, ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "But never mind that for now!"),
                new MessageAction(64000, ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "Let's get you out of here and into town."),
                new MessageAction(68000, ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "First, I have some supplies here to help you in your adventures!"),
                new MessageAction(75000, ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "Look around the room for a " + ChatColor.GREEN + "Navigation Compass" +
                        ChatColor.WHITE + ", an " + ChatColor.GREEN + "Adventure Log" + ChatColor.WHITE + ", and a " + ChatColor.GREEN + "Loot Bag" + ChatColor.WHITE + "."),
                new MessageAction(81000, ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "Come sit down again when you have everything you need."),
                new TutorialAction(85000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.getRegistry().addEntry("tutorial_leave_table", true);
                        player.getRegistry().addEntry("tutorial_particle", "items");
                        player.getRegistry().addEntry("tutorial_items", true);
                        player.getRegistry().addEntry("tutorial_compass", true);
                        player.getRegistry().addEntry("tutorial_book", true);
                        player.getRegistry().addEntry("tutorial_bag", true);
                        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Leave the chair by holding " + ChatColor.AQUA + "Left-Shift");
                        return true;
                    }
                }
        ));
        player.getRegistry().addEntry("tutorial_start", System.currentTimeMillis());
        player.getRegistry().addEntry("tutorial_actions", actions);
    }

    public void startLastTableDiscussion(CPlayer player) {
        player.getRegistry().removeEntry("tutorial_items");
        player.getRegistry().removeEntry("tutorial_particle");
        List<TutorialAction> actions = new ArrayList<>(Arrays.asList(
                new MessageAction(500, ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "Great! Looks like you have everything you need."),
                new MessageAction(2500, ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "There are so many fun things to do here! I'm sure you'll find something you enjoy."),
                new MessageAction(8500, ChatColor.GRAY + "Elred: " + ChatColor.WHITE + "And, people in the kingdom are always looking for new ways to have fun!"),
                new TutorialAction(10000) {
                    @Override
                    public boolean run(CPlayer player) {
                        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This concludes the draft tutorial.");
                        String[] entries = new String[]{"tutorial_start", "tutorial_actions", "tutorial_sit_chair", "tutorial_leave_table",
                                "tutorial_leave_bed", "tutorial_first_scene_leave_message_delay", "tutorial_particle", "tutorial_first_scene",
                                "tutorial_compass", "tutorial_book", "tutorial_bag", "tutorial_items"};
                        for (String entry : entries) {
                            player.getRegistry().removeEntry(entry);
                        }
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
