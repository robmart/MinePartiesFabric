package robmart.mod.mineparties.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PartyScreen extends Screen {
    private static final Identifier TEXTURE = new Identifier("mineparties:textures/gui/party.png");
    private static final int TEXTURE_HEIGHT = 166;
    private static final int TEXTURE_WIDTH = 176;

    public PartyScreen() {
        super(Text.of("Party"));
    }

    @Override
    protected void init() {

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int i = (this.width - TEXTURE_WIDTH) / 2;
        int j = (this.height - TEXTURE_HEIGHT) / 2;
        this.drawTexture(matrices, i, j, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
