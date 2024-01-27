package robmart.mod.mineparties.api.faction;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableTextContent;
import robmart.mod.mineparties.common.helper.DataHelper;
import robmart.mod.targetingapifabric.api.faction.Faction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FactionParty extends Faction {
    private String oldName;
    public FactionParty(String name) {
        super(name, true);
        oldName = name;
    }

    public FactionParty(String name, boolean permanent, boolean isServerSide) {
        super(name, permanent, isServerSide);
    }

    @Override
    public boolean setName(String name) {
        if (!name.matches(".*\\w.*")) return false;

        oldName = getName();

        return super.setName(name);
    }

    @Override
    public void addMemberEntity(Entity entityToAdd) {
        super.addMemberEntity(entityToAdd);

        if (this.isServerSide() && entityToAdd instanceof PlayerEntity) {
            for (String member : getAllPlayers()) {

                DataHelper.playerFromUsername(member).sendMessage(MutableText.of(new TranslatableTextContent("commands.mineparties.party.joined", entityToAdd.getName())), false);
            }
        }
    }

    public List<String> getAllPlayers() {
        List<String> playerEntities = new ArrayList<>();
        Map<Object, Boolean> members = getAllMembers();
        for (Object member : members.keySet()) {
            if (members.get(member))
                playerEntities.add(member.toString());
        }

        return playerEntities;
    }
}
