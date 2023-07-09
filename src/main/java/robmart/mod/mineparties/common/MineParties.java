package robmart.mod.mineparties.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import robmart.mod.mineparties.api.notification.Notification;
import robmart.mod.mineparties.common.command.CommandNotification;
import robmart.mod.mineparties.common.command.CommandParty;

public class MineParties implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(t -> {
            for (Notification notification : Notification.getNotificationList().values()) {
                if (!notification.hasSentMessage())
                    notification.sendMessage();
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CommandParty.register(dispatcher);
            CommandNotification.register(dispatcher);
        });
    }
}
