package robmart.mod.mineparties.api.faction;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableTextContent;
import robmart.mod.mineparties.common.networking.PartyInfo;
import robmart.mod.targetingapifabric.api.faction.Faction;

import java.util.ArrayList;
import java.util.List;

public class FactionParty extends Faction {
    private String oldName;
    public FactionParty(String name) {
        super(name, true);
        oldName = name;
    }

    @Override
    public void setName(String name) {
        oldName = getName();
        super.setName(name);

        SendPartyInfo();
    }

    @Override
    public void addMemberEntity(Entity entityToAdd) {
        super.addMemberEntity(entityToAdd);

        if (entityToAdd instanceof PlayerEntity) {
            for (Object member : getAllMembers()) {
                if (member instanceof PlayerEntity)
                    ((PlayerEntity) member).sendMessage(MutableText.of(new TranslatableTextContent("commands.mineparties.party.joined", entityToAdd.getName())), false);
            }
        }

        SendPartyInfo();
    }

    @Override
    public void removeMemberEntity(Entity entityToRemove) {
        super.removeMemberEntity(entityToRemove);

        SendPartyInfo();
    }

    @Override
    public void clearMembers() {
        super.clearMembers();

        SendPartyInfo();
    }

    public List<PlayerEntity> getAllPlayers() {
        List<PlayerEntity> playerEntities = new ArrayList<>();
        for (Object member : getAllMembers()) {
            if (member instanceof PlayerEntity player)
                playerEntities.add(player);
        }

        return playerEntities;
    }

    public void SendPartyInfo() {
        PartyInfo partyInfo = oldName.equals(getName()) ? new PartyInfo(getName()) : new PartyInfo(getName(), oldName);
        for (PlayerEntity player : getAllPlayers()) {
            if (player instanceof ServerPlayerEntity sPlayer)
                partyInfo.add(new PartyInfo.PartyInfoPart(sPlayer.getName().getString()));
        }

        if (!partyInfo.partyInfoParts.isEmpty()) {
            for (PlayerEntity player : getAllPlayers()) {
                if (player instanceof ServerPlayerEntity sPlayer)
                    ServerPlayNetworking.send(sPlayer, PartyInfo.PARTY_INFO_PACKET_ID, partyInfo.getByteBuf());
            }
        }
    }
}
