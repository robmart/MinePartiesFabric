package robmart.mod.mineparties.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import robmart.mod.mineparties.client.inventorytab.InventoryTab;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {
    private static final Identifier TABS_TEXTURE = new Identifier("textures/gui/container/creative_inventory/tabs.png");

    private InventoryTab selectedTab = InventoryTab.getDefaultTab();

    @Inject(at = @At(value = "RETURN"), method = "drawBackground")
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        for (InventoryTab inventoryTab : InventoryTab.tabsToDisplay()) {
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderTexture(0, TABS_TEXTURE);
            if (inventoryTab == selectedTab) continue;
            this.renderTabIcon(matrices, inventoryTab);
        }
    }


}
