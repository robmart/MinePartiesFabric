package robmart.mod.mineparties.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import robmart.mod.mineparties.api.faction.FactionParty;
import robmart.mod.targetingapifabric.api.Targeting;
import robmart.mod.targetingapifabric.api.faction.Faction;

import java.lang.annotation.Target;

public class PartyScreen extends Screen {
    private static final Identifier TEXTURE = new Identifier("mineparties:textures/gui/party.png");
    private static final Identifier ADD_BUTTON_TEXTURE = new Identifier("mineparties:textures/gui/plus_button.png");
    private static final int TEXTURE_HEIGHT = 166;
    private static final int TEXTURE_WIDTH = 176;

    private TextFieldWidget partyNameWidget;
    private TexturedButtonWidget partyCreateWidget;

    private FactionParty party;

    public PartyScreen() {
        super(Text.of("Party"));
    }

    public void loadParty() {
        for (Faction faction : Targeting.getFactionsFromEntity(client.player)) {
            if (faction instanceof FactionParty) {
                party = (FactionParty) faction;
            }
        }
    }

    public void createParty(){
        if (partyNameWidget.getText().equals("") && party == null) {
            client.player.sendChatMessage("/party create");
        } else if (!partyNameWidget.getText().equals("") && party == null) {
            client.player.sendChatMessage("/party create " + partyNameWidget.getText());
        }
    }

    @Override
    protected void init() {
        loadParty();

        partyNameWidget = new TextFieldWidget(this.textRenderer, this.width / 2 - 80, this.height / 2 - 75,
                TEXTURE_WIDTH / 2 - 10, 15, new TranslatableText("mineparties.name"));
        partyCreateWidget = new TexturedButtonWidget(this.width / 2 + 60, this.height / 2 - 77, 20, 18, 0, 0, 19, ADD_BUTTON_TEXTURE, (button) -> createParty());
        addSelectableChild(partyNameWidget);
        addSelectableChild(partyCreateWidget);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        if (party == null) {
            partyNameWidget.render(matrices, mouseX, mouseY, delta);
            partyCreateWidget.render(matrices, mouseX, mouseY, delta);

            loadParty();
        } else {
            textRenderer.draw(matrices, party.getName(), this.width / 2 - 80, this.height / 2 - 75, 0);
        }
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
        super.renderBackground(matrices);
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
