package network.palace.lobby.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import network.palace.core.dashboard.packets.PacketID;
import network.palace.core.dashboard.packets.dashboard.PacketLobbyData;
import network.palace.core.events.IncomingPacketEvent;
import network.palace.lobby.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;

public class PacketListener implements Listener {

    public PacketListener() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Lobby.getInstance(), PacketType.Play.Server.WORLD_EVENT) {
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
