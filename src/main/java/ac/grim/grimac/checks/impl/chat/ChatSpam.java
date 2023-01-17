package ac.grim.grimac.checks.impl.chat;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;

@CheckData(name = "ChatSpam")
public class ChatSpam extends Check implements PacketCheck {

    public ChatSpam(GrimPlayer player) {
        super(player);
    }

    private String lastMessage = null;
    private int sameMessage = 0;
    private long lastMessageTime = System.currentTimeMillis();

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CHAT_MESSAGE && preventSpam) {
        /*    WrapperPlayClientChatMessage wrapper = new WrapperPlayClientChatMessage(event);
            final String message = wrapper.getMessage();*/
            final long time = System.currentTimeMillis();

         /*   if (message.equalsIgnoreCase(lastMessage) && sameMessage++ > maxDuplicates) {
                event.setCancelled(true);
                player.onPacketCancel();
                flagAndAlert("duplicates=" + sameMessage);
            } else {
                sameMessage = 0;
            }
*/
            long delay = time - lastMessageTime;
            if (!event.isCancelled() && delay < (lagCompensate ? Math.max(minDelay, minDelay + player.getPlayerClockDelayInMs()) : minDelay)) {
                event.setCancelled(true);
                player.onPacketCancel();
                flagAndAlert("delay=" + delay);
            }
            //
            lastMessageTime = time;
            //lastMessage = message;
        }
    }

    private boolean preventSpam = false;
    private int maxDuplicates = 3;
    private long minDelay = 250;
    private boolean lagCompensate = false;

    @Override
    public void reload() {
        preventSpam = GrimAPI.INSTANCE.getConfigManager().getConfig().getBooleanElse("ChatSpam.prevent-spam", false);
        maxDuplicates = GrimAPI.INSTANCE.getConfigManager().getConfig().getIntElse("ChatSpam.max-duplicates", 5);
        minDelay = GrimAPI.INSTANCE.getConfigManager().getConfig().getLongElse("ChatSpam.min-delay", 250);
        lagCompensate = GrimAPI.INSTANCE.getConfigManager().getConfig().getBooleanElse("ChatSpam.lag-compensate", false);
    }
}
