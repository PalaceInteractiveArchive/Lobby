package network.palace.lobby.util;

import network.palace.core.Core;
import network.palace.lobby.Lobby;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WorldUtil {
    private boolean streetLights = false, shootingStars = false;
    private final List<Location> streetLightLocations = new ArrayList<>();
    private final List<Location> launchPadLocations = new ArrayList<>();
    private final Location portal;

    public WorldUtil() {
        World w = Bukkit.getWorlds().get(0);
        streetLightLocations.addAll(Arrays.asList(
                new Location(w, 18, 73, -23),
                new Location(w, 5, 73, -33),
                new Location(w, -11, 77, -8),
                new Location(w, -2, 77, 20),
                new Location(w, 22, 75, 22),
                new Location(w, 37, 73, -6),
                new Location(w, -12, 75, -48),
                new Location(w, -30, 75, -65),
                new Location(w, -45, 77, -45),
                new Location(w, -75, 75, -52),
                new Location(w, -89, 75, -40),
                new Location(w, 12, 76, 47),
                new Location(w, 52, 73, 53),
                new Location(w, 10, 76, 70),
                new Location(w, 24, 75, 86),
                new Location(w, 6, 68, 103),
                new Location(w, 106, 72, 84),
                new Location(w, 107, 72, 51),
                new Location(w, 92, 73, 31),
                new Location(w, 81, 73, 19),
                new Location(w, 95, 73, 12),
                new Location(w, 82, 74, -4),
                new Location(w, 59, 73, 6),
                new Location(w, 61, 73, 81)
        ));
        launchPadLocations.addAll(Arrays.asList(
                new Location(w, -25, 62, -51),
                new Location(w, 8, 60, -21),
                new Location(w, 30, 60, -78),
                new Location(w, -4, 63, 9),
                new Location(w, 11, 62, 21),
                new Location(w, 36, 60, 60)
        ));
        portal = new Location(w, -34.5, 54.6, -109.5);

//        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, ChatColor.GREEN + "Elred");
//
//        Equipment eq = npc.getTrait(Equipment.class);
//        eq.set(Equipment.EquipmentSlot.BOOTS, new ItemStack(Material.LEATHER_BOOTS));
//        eq.set(Equipment.EquipmentSlot.HELMET, new ItemStack(Material.LEATHER_HELMET));
//
//        Waypoints.registerWaypointProvider(PathProvider.class, "lobbyPaths");
//
//        npc.addTrait(new TutorialNPC(Core.getPlayerManager().getOnlinePlayers().get(0).getUniqueId()));
//
//        npc.hasTrait(TutorialNPC.class);
//
//        npc.spawn(portal);

        Core.runTaskTimer(Lobby.getInstance(), new Runnable() {
            int step = 0;
            int step2 = 10;

            @Override
            public void run() {
                double strands = 4, curveRatio = 5, radius = 1.3;
                int particles = 30;

//                for (int i = 1; i <= strands; i++) {
//                    for (int j = 1; j <= particles; j++) {
//                        float ratio = (float) j / (particles * 2);
//                        double angle = curveRatio * ratio * 2 * Math.PI / strands + (2 * Math.PI * i / strands) + (Math.PI / 4) - (step / 40.0);
//
//                        double x = Math.cos(angle) * ratio * radius;
//                        double z = Math.sin(angle) * ratio * radius;
//                        portal.getWorld().spawnParticle(Particle.PORTAL, x + portal.getX(), portal.getY(), z + portal.getZ(), 1, 0.01, 0, 0.01, 0);
//                    }
//                }

                for (int i = 1; i <= strands; i++) {
                    for (int j = 1; j <= particles; j++) {
                        float ratio = (float) j / particles;
                        double angle = curveRatio * ratio * 2 * Math.PI / strands + (2 * Math.PI * i / strands) + (Math.PI / 4) - (step2 / 40.0);

                        double x = Math.cos(angle) * ratio * radius;
                        double z = Math.sin(angle) * ratio * radius;
                        portal.getWorld().spawnParticle(Particle.REDSTONE, x + portal.getX(), portal.getY() + (0.5 * (1 - ratio)), z + portal.getZ(),
                                0, 0.01, 0, 0, 1);
                    }
                }

                if (step % 20 == 0) {
                    for (int i = 0; i < 30; i++) {
                        double rads = Math.toRadians(i * 12);

                        double x = Math.cos(rads) * radius;
                        double z = Math.sin(rads) * radius;
                        portal.getWorld().spawnParticle(Particle.DRAGON_BREATH, x + portal.getX(), portal.getY(), z + portal.getZ(), 1, 0.1, 0, 0.1, 0);
//                            0, 1, 0, 1, 1);
                    }
                }

                portal.getWorld().spawnParticle(Particle.PORTAL, portal.getX(), portal.getY(), portal.getZ(), 8, 0.1, 0.1, 0.1, 0.6);

                step += 1;
                step %= 40;
                step2 += 1;
                step2 %= 40;
            }
        }, 0L, 1L);
        Core.runTaskTimer(Lobby.getInstance(), () -> launchPadLocations.forEach(loc -> {
            Block b = loc.getBlock();
            if (!b.getType().equals(Material.EMERALD_BLOCK)) return;
            b.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc.clone().add(0, 1.1, 0),
                    2, 0.45, 0.05, 0.45, 0);
        }), 0L, 5L);
        Core.runTaskTimer(Lobby.getInstance(), () -> {
            /*
            Schedule:
            13000 - street lights on
            14000 - shooting stars (every five nights, or first night when server starts up)
            23000 - street lights off
             */
            World spawnWorld = Bukkit.getWorlds().get(0);
            long time = spawnWorld.getTime();
            if (time >= 13000 && time < 23000) {
                //if lights should be on
                if (!streetLights) {
                    //turn them on
                    Bukkit.getLogger().info("Street lights on!");
                    Collections.shuffle(streetLightLocations);
                    int i = 0;
                    boolean stopped = false;
                    for (Location loc : streetLightLocations) {
                        Block b = loc.getBlock();
                        if (b.getType().equals(Material.REDSTONE_LAMP_OFF)) {
                            if (i++ >= 3) {
                                stopped = true;
                                break;
                            }
                            Material material = b.getType();
                            Handler handler = new Handler();
                            handler.init(loc.getWorld());

                            handler.setStatic(true);
                            b.setType(Material.REDSTONE_LAMP_ON);
                            handler.setStatic(false);
                            loc.getWorld().playSound(loc, Sound.BLOCK_LEVER_CLICK, 1.7f, 0.6f);
                        }
                    }
                    if (!stopped) streetLights = true;
                } else {
                    //do cool particle effects
                    streetLightLocations.forEach(loc -> {
                        Block b = loc.getBlock();
                        if (!b.getType().equals(Material.REDSTONE_LAMP_ON)) return;
                        Location l = loc.clone().add(0.5, 0.5, 0.5);
                        b.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, l, 5, 0.6, 0.3, 0.6, 0.05);
                    });
                }
            }
            if ((time >= 13000 && time < 23000) && !streetLights) {
                Bukkit.getLogger().info("Street lights on!");
                Collections.shuffle(streetLightLocations);
                int i = 0;
                boolean stopped = false;
                for (Location loc : streetLightLocations) {
                    Block b = loc.getBlock();
                    if (b.getType().equals(Material.REDSTONE_LAMP_OFF)) {
                        if (i++ >= 3) {
                            stopped = true;
                            break;
                        }
                        Material material = b.getType();
                        Handler handler = new Handler();
                        handler.init(loc.getWorld());

                        handler.setStatic(true);
                        b.setType(Material.REDSTONE_LAMP_ON);
                        handler.setStatic(false);
                        loc.getWorld().playSound(loc, Sound.BLOCK_LEVER_CLICK, 1.7f, 0.6f);
                    }
                }
                if (!stopped) streetLights = true;
            } else if ((time >= 23000 || time < 13000) && streetLights) {
                Bukkit.getLogger().info("Street lights off!");
                Collections.shuffle(streetLightLocations);
                int i = 0;
                boolean stopped = false;
                for (Location loc : streetLightLocations) {
                    Block b = loc.getBlock();
                    if (b.getType().equals(Material.REDSTONE_LAMP_ON)) {
                        if (i++ >= 3) {
                            stopped = true;
                            break;
                        }
                        b.setType(Material.REDSTONE_LAMP_OFF);
                        loc.getWorld().playSound(loc, Sound.BLOCK_LEVER_CLICK, 2f, 0.5f);
                    }
                }
                if (!stopped) streetLights = false;
            }
            if ((time >= 14000 && time < 18000) && !shootingStars) {
                shootingStars = true;
                Bukkit.getLogger().info("Shooting stars starting!");
            } else if ((time >= 18000 || time < 14000) && shootingStars) {
                shootingStars = false;
                Bukkit.getLogger().info("Shooting stars reset!");
            }
        }, 0L, 20L);
    }

    public void shutdown() {
    }

    private static class Handler {
        private Object craftWorld;
        private Field clientSide;

        public void init(World world) {
            try {
                craftWorld = getMinecraftWorld(world);
                clientSide = craftWorld.getClass().getField("isClientSide");
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                try {
                    clientSide = craftWorld.getClass().getField("isStatic");
                } catch (NoSuchFieldException e1) {
                    e1.printStackTrace();
                }
            }

            clientSide.setAccessible(true);
        }

        public void setStatic(boolean value) {
            try {
                clientSide.set(craftWorld, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        private Object getMinecraftWorld(Object world) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            String nmsVersion = Core.getMinecraftVersion();
            Object craftWorld = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".CraftWorld", false, Lobby.class.getClassLoader()).cast(world);
            Object worldServerInstance = craftWorld.getClass().getDeclaredMethod("getHandle").invoke(craftWorld);

            return Class.forName("net.minecraft.server." + nmsVersion + ".World", false, Lobby.class.getClassLoader()).cast(worldServerInstance);
        }
    }
}
