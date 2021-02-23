package network.palace.lobby.tutorial.old.actions;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import network.palace.core.packets.server.chat.WrapperPlayServerChat;
import network.palace.core.player.CPlayer;

public class MessageAction extends TutorialAction {
    private final String text;

    public MessageAction(long time, String text) {
        super(time);
        this.text = text;
    }

    @Override
    public boolean run(CPlayer player) {
        WrapperPlayServerChat packet = new WrapperPlayServerChat();
        packet.setMessage(WrappedChatComponent.fromText(text));
        packet.setPosition(EnumWrappers.ChatType.CHAT);
        player.sendPacket(packet);
        return true;
    }
}
