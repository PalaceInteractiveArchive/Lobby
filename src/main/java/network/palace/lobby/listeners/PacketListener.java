package network.palace.lobby.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import network.palace.core.Core;
import network.palace.core.dashboard.packets.PacketID;
import network.palace.core.dashboard.packets.dashboard.PacketLobbyData;
import network.palace.core.events.IncomingPacketEvent;
import network.palace.core.player.CPlayer;
import network.palace.lobby.Lobby;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;

public class PacketListener implements Listener {

    public PacketListener() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        // Disable ender portal sounds
        manager.addPacketListener(new PacketAdapter(Lobby.getInstance(), PacketType.Play.Server.WORLD_EVENT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                StructureModifier<Integer> ints = packet.getIntegers();
                try {
                    Field f = ints.getField(0);
                    f.setAccessible(true);
                    if ((int) f.get(packet.getHandle()) == 1032) event.setCancelled(true);
                } catch (Exception ignored) {
                }
            }
        });

        // Prevent ejecting from vechile
        manager.addPacketListener(new PacketAdapter(Lobby.getInstance(), ListenerPriority.LOWEST, PacketType.Play.Client.STEER_VEHICLE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                CPlayer player = Core.getPlayerManager().getPlayer(event.getPlayer());
                if (player == null) return;
                PacketContainer packet = event.getPacket();
                try {
                    Field f = packet.getBooleans().getFields().get(1);
                    f.setAccessible(true);
                    if (!f.getBoolean(packet.getHandle())) {
                        return;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                }
                if (player.getRegistry().hasEntry("tutorial_start")) {
                    // They're in the tutorial, only allow ejecting when leaving table
                    if (player.getRegistry().hasEntry("tutorial_leave_bed")) {
                        event.setCancelled(false);
                        Entity e = player.getBukkitPlayer().getVehicle();
                        Core.runTask(Lobby.getInstance(), () -> {
                            player.getRegistry().removeEntry("tutorial_leave_bed");
                            player.getRegistry().addEntry("tutorial_sit_chair", true);
                            player.teleport(new Location(player.getWorld(), 58.5, 72.1, 21.5, -90, 0));
                            if (e != null && !e.isDead()) e.remove();
                        });
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        });

    }

    @EventHandler
    public void onIncomingPacket(IncomingPacketEvent event) {
        String packet = event.getPacket();
        if (event.getId() != PacketID.Dashboard.LOBBYDATA.getID()) return;
        JsonObject object = (JsonObject) new JsonParser().parse(packet);
        PacketLobbyData lobbyData = new PacketLobbyData().fromJSON(object);
        Lobby.getInventoryNav().updateCounts(lobbyData.getParks(), lobbyData.getCreative(), lobbyData.getArcade());
        Lobby.getHubSelector().updateCounts(lobbyData.getHubs());
    }
}
