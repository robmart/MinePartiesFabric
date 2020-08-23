package robmart.mod.mineparties.api.faction;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import robmart.mod.targetingapifabric.api.faction.Faction;

public class FactionParty extends Faction {
    public FactionParty(String name) {
            super(name, true);
        }

        @Override
        public void addMemberEntity(Entity entityToAdd) {
            if (entityToAdd instanceof PlayerEntity) {
                for (Object member : getAllMembers()) {
                    if (member instanceof PlayerEntity)
                        ((PlayerEntity) member).sendMessage(new TranslatableText("commands.mineparties.party.joined", entityToAdd.getName()), false);
                }
            }
            super.addMemberEntity(entityToAdd);
        }
}
