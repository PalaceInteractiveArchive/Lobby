package network.palace.lobby.listeners;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import network.palace.core.dashboard.packets.PacketID;
import network.palace.core.dashboard.packets.dashboard.PacketLobbyData;
import network.palace.core.events.IncomingPacketEvent;
import network.palace.lobby.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PacketListener implements Listener {

    @EventHandler
    public void onIncomingPacket(IncomingPacketEvent event) {
        int id = event.getId();
        String packet = event.getPacket();
        if (id != PacketID.Dashboard.LOBBYDATA.getID()) return;
        JsonObject object = (JsonObject) new JsonParser().parse(packet);
        PacketLobbyData lobbyData = new PacketLobbyData().fromJSON(object);
        Lobby.getInstance().getInventoryNav().updateCounts(lobbyData.getParks(), lobbyData.getCreative(), lobbyData.getArcade());
        Lobby.getInstance().getHubSelector().updateCounts(lobbyData.getHubs());
    }
}
