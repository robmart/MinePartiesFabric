package robmart.mod.mineparties.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;
import robmart.mod.mineparties.api.faction.FactionParty;
import robmart.mod.targetingapifabric.api.Targeting;
import robmart.mod.targetingapifabric.api.faction.Faction;

public class PartyScreen extends Screen {
    private static final Identifier TEXTURE = new Identifier("mineparties:textures/gui/party.png");
    private static final Identifier ADD_BUTTON_TEXTURE = new Identifier("mineparties:textures/gui/plus_button.png");
    private static final Identifier MINUS_BUTTON_TEXTURE = new Identifier("mineparties:textures/gui/minus_button.png");
    private static final Identifier EDIT_BUTTON_TEXTURE = new Identifier("mineparties:textures/gui/edit_button.png");
    private static final int TEXTURE_HEIGHT = 166;
    private static final int TEXTURE_WIDTH = 176;

    private TextFieldWidget partyNameWidget;
    private TexturedButtonWidget partyCreateWidget;
    private TexturedButtonWidget partyLeaveWidget;
    private TexturedButtonWidget partyEditWidget;

    private FactionParty party;
    private int counter = 0;

    public PartyScreen() {
        super(Text.of("Party"));
    }

    public void loadParty() {
        for (Faction faction : Targeting.getFactionsFromEntity(client.player)) {
            if (faction instanceof FactionParty) {
                party = (FactionParty) faction;
                for (Object object : party.getAllMembers()) {
                    if (object instanceof Entity entity) {
                        System.out.println(entity.getName());
                    }
                }
                return;
            }
        }
    }

    public void createParty(){
        if (partyNameWidget.getText().equals("") && party == null) {
            MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("party create");
        } else if (!partyNameWidget.getText().equals("") && party == null) {
            System.out.println(partyNameWidget.getText());
            MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("party create " + partyNameWidget.getText());
        }

        partyNameWidget.setText("");
    }

    public void leaveParty(){
        MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("party leave");
        party = null;
    }

    public void editName(){
        if (!partyNameWidget.getText().equals("")) {
            party.setName(partyNameWidget.getText());
            partyNameWidget.setText("");
        }
    }

    @Override
    protected void init() {
        loadParty();

        partyNameWidget = new TextFieldWidget(this.textRenderer, this.width / 2 - 80, this.height / 2 - 75,
                TEXTURE_WIDTH / 2 - 10, 15, MutableText.of(new TranslatableTextContent("mineparties.gui.party.name")));

        partyCreateWidget = new TexturedButtonWidget(this.width / 2 + 60, this.height / 2 - 77, 20, 18, 0, 0, 19, ADD_BUTTON_TEXTURE, (button) -> createParty());
        partyEditWidget = new TexturedButtonWidget(this.width / 2 + 38, this.height / 2 - 77, 20, 18, 0, 0, 19, EDIT_BUTTON_TEXTURE, (button) -> editName());
        partyLeaveWidget = new TexturedButtonWidget(this.width / 2 + 60, this.height / 2 - 77, 20, 18, 0, 0, 19, MINUS_BUTTON_TEXTURE, (button) -> leaveParty());

        addSelectableChild(partyNameWidget);

        addSelectableChild(partyCreateWidget);
        addSelectableChild(partyEditWidget);
        addSelectableChild(partyLeaveWidget);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        partyNameWidget.render(matrices, mouseX, mouseY, delta);

        if (party == null) {
            partyLeaveWidget.active = false;
            partyCreateWidget.active = true;

            partyNameWidget.setY(this.height / 2 - 75);

            partyCreateWidget.render(matrices, mouseX, mouseY, delta);

            loadParty();
        } else {
            partyLeaveWidget.active = true;
            partyCreateWidget.active = false;

            partyNameWidget.setY(this.height / 2 - 57);

            textRenderer.draw(matrices, party.getName(), this.width / 2 - 78, this.height / 2 - 72, 0);

            partyEditWidget.render(matrices, mouseX, mouseY, delta);
            partyLeaveWidget.render(matrices, mouseX, mouseY, delta);
        }

        textRenderer.draw(matrices, MutableText.of(new TranslatableTextContent("mineparties.gui.party.members")), this.width / 2 - 78, this.height / 2 - 30, 0);
        textRenderer.draw(matrices, MutableText.of(new TranslatableTextContent("mineparties.gui.party.settings")), this.width / 2 + 3, this.height / 2 - 30, 0);
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
        super.renderBackground(matrices);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int i = (this.width - TEXTURE_WIDTH) / 2;
        int j = (this.height - TEXTURE_HEIGHT) / 2;
        this.drawTexture(matrices, i, j, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
