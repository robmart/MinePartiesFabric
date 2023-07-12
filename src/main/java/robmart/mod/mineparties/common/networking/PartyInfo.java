package robmart.mod.mineparties.common.networking;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import robmart.mod.mineparties.api.reference.Reference;

import java.util.ArrayList;
import java.util.List;

public class PartyInfo {
    public static final Identifier PARTY_INFO_PACKET_ID = new Identifier(Reference.MOD_ID, "party_info");
    public List<PartyInfoPart> partyInfoParts = new ArrayList<>();
    public String Name;
    public String OldName;

    public PartyInfo(String name) {
        Name = name;
    }

    public PartyInfo(String name, String oldName) {
        Name = name;
        OldName = oldName;
    }

    public PartyInfo(PacketByteBuf buf) {
        Name = buf.readString();
        boolean hasOldName = buf.readBoolean();
        OldName = buf.readString();
        int size = buf.readInt();

        for (int i = 0; i < size; i++) {
            partyInfoParts.add(new PartyInfoPart(buf.readString()));
        }
    }

    public PartyInfo add(PartyInfoPart part) {
        partyInfoParts.add(part);
        return this;
    }

    public PacketByteBuf write() {
        PacketByteBuf buf = PacketByteBufs.create();
        boolean hasOldName = this.OldName != null && !this.OldName.equals("");

        buf.writeString(this.Name);
        buf.writeBoolean(hasOldName);
        buf.writeString(hasOldName ? this.OldName : "oldname");
        buf.writeInt(this.partyInfoParts.size());
        for (PartyInfoPart part : this.partyInfoParts) {
            buf.writeString(part.playerId);
        }

        return buf;
    }

    public static class PartyInfoPart {
        public String playerId;

        public PartyInfoPart(String playerId) {
            this.playerId = playerId;
        }
    }
}
