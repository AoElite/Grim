package ac.grim.grimac.checks.impl.chat;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.impl.misc.ClientBrand;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;

import java.util.concurrent.TimeUnit;

@CheckData(name = "BotA")
public class BotA extends Check implements PacketCheck {
    public BotA(GrimPlayer playerData) {
        super(playerData);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CHAT_MESSAGE) {
            // over 5s of ping or < 2s
            long nano = System.nanoTime() - player.getPlayerClockAtLeast();
            long join = System.currentTimeMillis() - player.timeJoinedInMs;
            boolean brand = player.checkManager.getPacketCheck(ClientBrand.class).hasBrand;
            if (nano > 5 * 1e9 || join < 2000 || player.getTransactionPing() > 5000) {
                WrapperPlayClientChatMessage msg = new WrapperPlayClientChatMessage(event);
                String message = limit(msg.getMessage(), 48)
                        .replace("{", "[other]")
                        .replace("}", "[other]")
                        .replace("/", "[slash]");
                event.setCancelled(true);
                player.onPacketCancel();
                flagAndAlert("clock=" + TimeUnit.NANOSECONDS.toMillis(nano) + "ms, " + "join=" + join + "ms, brand=" + brand + ", msg=" + message);
                if (player.bukkitPlayer != null) {
                    player.bukkitPlayer.sendMessage("You cannot talk at the moment. Please wait.");
                }
            }
            //
        }
    }

    public static String limit(String value, int length) {
        StringBuilder buf = new StringBuilder(value);
        if (buf.length() > length) {
            buf.setLength(length);
            buf.append("...");
        }

        return buf.toString();
    }

}
