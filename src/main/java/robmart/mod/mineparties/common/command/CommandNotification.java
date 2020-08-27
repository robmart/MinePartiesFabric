package robmart.mod.mineparties.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandException;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import robmart.mod.mineparties.api.notification.Notification;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class CommandNotification {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("notification")
                        .then(CommandManager.argument("id", UuidArgumentType.uuid())
                                .executes(ctx -> execute(ctx.getSource(), UuidArgumentType.getUuid(ctx, "id"))))
        );
    }

    private static int execute(ServerCommandSource source, UUID id) {
        Notification notification = Notification.getNotificationList().get(id);
        if (notification == null)
            throw new CommandException(new TranslatableText("commands.mineparties.notification.notexist"));
        try {
            notification.execute();
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return 1;
    }
}
