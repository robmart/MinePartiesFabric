package robmart.mod.mineparties.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import robmart.mod.mineparties.api.faction.FactionParty;
import robmart.mod.targetingapifabric.api.Targeting;
import robmart.mod.targetingapifabric.api.faction.Faction;

public class CommandParty {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("party")
                        .then(CommandManager.literal("create")
                                .executes(ctx -> createParty(ctx.getSource()))
                        ).then(CommandManager.literal("list")
                                .executes(ctx -> listMembers(ctx.getSource()))
                ).then(CommandManager.literal("invite")
                )
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

    private static int listMembers(ServerCommandSource source){
        return 1;
    }

    private static int invite(ServerCommandSource source, PlayerEntity entity){
        return 1;
    }
}
