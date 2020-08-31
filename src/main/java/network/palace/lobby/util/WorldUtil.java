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
    private boolean streetLights = false,
            shootingStars = false;
    private final List<Location> streetLightLocations = new ArrayList<>();

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
                new Location(w, -89, 75, -40)
        ));
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
                    Bukkit.broadcastMessage("Street lights on!");
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
                Bukkit.broadcastMessage("Street lights on!");
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
                Bukkit.broadcastMessage("Street lights off!");
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
                Bukkit.broadcastMessage("Shooting stars starting!");
            } else if ((time >= 18000 || time < 14000) && shootingStars) {
                shootingStars = false;
                Bukkit.broadcastMessage("Shooting stars reset!");
            }
        }, 0L, 20L);
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
