package robmart.mod.mineparties.common.helper;

import net.minecraft.entity.player.PlayerEntity;
import robmart.mod.targetingapifabric.api.reference.Reference;

public class DataHelper {
    public static PlayerEntity playerFromUsername(String username) {
        if (Reference.MINECRAFT_SERVER == null) return null;

        return Reference.MINECRAFT_SERVER.getPlayerManager().getPlayer(username);
    }
}
