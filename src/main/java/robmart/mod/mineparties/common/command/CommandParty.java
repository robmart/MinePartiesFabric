package robmart.mod.mineparties.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import robmart.mod.mineparties.api.faction.FactionParty;
import robmart.mod.mineparties.api.notification.Notification;
import robmart.mod.targetingapifabric.api.Targeting;
import robmart.mod.targetingapifabric.api.faction.Faction;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CommandParty {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("party")
                        .then(CommandManager.literal("create")
                                .executes(ctx -> createParty(ctx.getSource()))
                                .then(CommandManager.argument("name", MessageArgumentType.message())
                                .executes(ctx -> createParty(ctx.getSource(), MessageArgumentType.getMessage(ctx, "name")))
                        )).then(CommandManager.literal("list")
                                .executes(ctx -> listMembers(ctx.getSource()))
                    ).then(CommandManager.literal("invite")
                        .then(CommandManager.argument("player", EntityArgumentType.players())
                                .executes(ctx -> invite(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player")))))

        );
    }

    private static int createParty(ServerCommandSource source){
        for (Faction faction : Targeting.getFactionsFromEntity(source.getEntity())) {
            if (faction instanceof FactionParty) {
                source.sendError(new TranslatableText("commands.mineparties.party.inparty"));
                return 0;
            }
        }

        FactionParty party = new FactionParty(String.format("%s's Party", source.getName()));
        party.addMemberEntity(source.getEntity());
        Targeting.registerFaction(party);

        source.sendFeedback(new TranslatableText("commands.mineparties.party.success1", party.getName()), true);
        return 1;
    }

    private static int createParty(ServerCommandSource source, Text name){
        for (Faction faction : Targeting.getFactionsFromEntity(source.getEntity())) {
            if (faction instanceof FactionParty) {
                source.sendError(new TranslatableText("commands.mineparties.party.inparty"));
                return 0;
            }
        }

        FactionParty party = new FactionParty(name.asString());
        party.addMemberEntity(source.getEntity());
        Targeting.registerFaction(party);

        source.sendFeedback(new TranslatableText("commands.mineparties.party.success1", party.getName()), true);
        return 1;
    }

    private static int listMembers(ServerCommandSource source){
        AtomicInteger returnint = new AtomicInteger();
        Targeting.getFactionsFromEntity(source.getEntity()).forEach(faction -> {
            if (faction instanceof FactionParty) {
                faction.getAllMembers().forEach(obj -> {
                    if (obj instanceof PlayerEntity)
                        source.sendFeedback(((PlayerEntity) obj).getName(), false);
                });
                returnint.set(1);
            }
        });

        if (returnint.get() < 1)
            source.sendError(new TranslatableText("commands.mineparties.party.noparty"));
        return returnint.get();
    }

    private static int invite(ServerCommandSource source, PlayerEntity entity) {
        AtomicReference<FactionParty> party = new AtomicReference<>();
        Targeting.getFactionsFromEntity(source.getEntity()).forEach(faction -> {
            if (faction instanceof FactionParty)
                party.set((FactionParty) faction);
        });

        if (party.get() == null) {
            source.sendError(new TranslatableText("commands.mineparties.party.noparty"));
            return 0;
        }

        Notification notification = null;
        try {
            notification = new Notification(entity, new TranslatableText("commands.mineparties.party.invite",
                    source.getEntity().getName()), Faction.class.getMethod("addMemberEntity", Entity.class), party.get(),
                    entity);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        notification.sendMessage();
        return 1;
    }
}
