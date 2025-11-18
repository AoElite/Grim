package ac.grim.grimac.manager.logging;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PacketLogListener {

    private final PacketLogManager loggingManager;
    private final ServerVersion serverVersion;

    public PacketLogListener(PacketLogManager loggingManager) {
        this.loggingManager = loggingManager;
        this.serverVersion = PacketEvents.getAPI().getServerManager().getVersion();
    }

    @AllArgsConstructor
    @Getter
    public enum PacketDirection {
        INCOMING("<- C"),
        OUTGOING("S ->");
        private final String display;
    }

}
