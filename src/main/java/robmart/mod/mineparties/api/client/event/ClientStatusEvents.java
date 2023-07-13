package robmart.mod.mineparties.api.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

public class ClientStatusEvents {
    public static final Event<DisconnectClient> DISCONNECT_CLIENT_EVENT = EventFactory.createArrayBacked(DisconnectClient.class,
            (listeners) -> (client) -> {
                for (DisconnectClient listener : listeners) listener.onDisconnect(client);
            });

    @FunctionalInterface
    public interface DisconnectClient {
        void onDisconnect(MinecraftClient client);
    }
}
