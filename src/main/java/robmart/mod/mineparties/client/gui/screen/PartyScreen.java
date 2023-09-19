package robmart.mod.mineparties.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;
import robmart.mod.mineparties.api.faction.FactionParty;
import robmart.mod.mineparties.client.gui.widget.PartyPlayerScrollableWidget;
import robmart.mod.targetingapifabric.api.Targeting;
import robmart.mod.targetingapifabric.api.TargetingClient;
import robmart.mod.targetingapifabric.api.faction.Faction;
import robmart.mod.targetingapifabric.api.faction.manager.FactionManager;

import java.util.Random;

public class PartyScreen extends Screen { //TODO: Reduce magic numbers
    private static final Identifier TEXTURE = new Identifier("mineparties:textures/gui/party.png");
    private static final Identifier ADD_BUTTON_TEXTURE = new Identifier("mineparties:textures/gui/plus_button.png");
    private static final Identifier MINUS_BUTTON_TEXTURE = new Identifier("mineparties:textures/gui/minus_button.png");
    private static final Identifier EDIT_BUTTON_TEXTURE = new Identifier("mineparties:textures/gui/edit_button.png");
    private static final int TEXTURE_HEIGHT = 166;
    private static final int TEXTURE_WIDTH = 256;

    public static FactionParty party;

    private MinecraftClient client;
    private TextFieldWidget partyNameWidget;
    private TexturedButtonWidget partyCreateWidget;
    private TexturedButtonWidget partyLeaveWidget;
    private TexturedButtonWidget partyEditWidget;
    private ScrollableWidget membersWidget;

    private int counter = 0;

    public PartyScreen(MinecraftClient client) {
        super(Text.of("Party"));
        this.client = client;
    }

    @Override
    public void tick() {
        super.tick();

        if (party == null) {
            for (Faction faction : TargetingClient.getFactionsFromEntity(client.player)) {
                if (faction instanceof FactionParty party)
                    PartyScreen.party = party;
            }
        } else if (!TargetingClient.getFactionList().contains(PartyScreen.party) || !PartyScreen.party.isMember(client.player)) {
            party = null;
        }
    }

    public void createParty(){
        if (partyNameWidget.getText().equals("") && party == null) {
            this.client.getNetworkHandler().sendChatCommand("party create");
        } else if (!partyNameWidget.getText().equals("") && party == null) {
            this.client.getNetworkHandler().sendChatCommand("party create " + partyNameWidget.getText());
        }

        partyNameWidget.setText("");
    }

    public void leaveParty(){
        this.client.getNetworkHandler().sendChatCommand("party leave");
        party = null;
    }

    public void editName(){
        if (!partyNameWidget.getText().equals("")) {
            this.client.getNetworkHandler().sendChatCommand("party name " + partyNameWidget.getText());
            partyNameWidget.setText("");
        }
    }

    public void invitePlayer(){
       if (!partyNameWidget.getText().equals("") && party != null) {
            this.client.getNetworkHandler().sendChatCommand("party invite " + partyNameWidget.getText());
        }

        partyNameWidget.setText("");
    }

    @Override
    protected void init() {
        partyNameWidget = new TextFieldWidget(this.textRenderer, this.width / 2 - 119, this.height / 2 - 76,
                108, 15, MutableText.of(new TranslatableTextContent("mineparties.gui.party.name")));
        partyNameWidget.setPlaceholder(Text.translatable("mineparties.gui.party.toname"));

        partyCreateWidget = new TexturedButtonWidget(this.width / 2 + 100, this.height / 2 - 77, 20, 18, 0, 0, 19, ADD_BUTTON_TEXTURE, (button) ->
        {
            if (party == null) {
                createParty();
            } else {
                invitePlayer();
            }
        });
        partyCreateWidget.setTooltip(Tooltip.of(Text.translatable("mineparties.gui.party.createtooltip")));
        partyEditWidget = new TexturedButtonWidget(this.width / 2 + 78, this.height / 2 - 57, 20, 18, 0, 0, 19, EDIT_BUTTON_TEXTURE, (button) -> editName());
        partyEditWidget.setTooltip(Tooltip.of(Text.translatable("mineparties.gui.party.renametooltip")));
        partyLeaveWidget = new TexturedButtonWidget(this.width / 2 + 100, this.height / 2 - 77, 20, 18, 0, 0, 19, MINUS_BUTTON_TEXTURE, (button) -> leaveParty());
        partyLeaveWidget.setTooltip(Tooltip.of(Text.translatable("mineparties.gui.party.leavetooltip")));

        membersWidget = new PartyPlayerScrollableWidget(client, this, this.width / 2 - 120, this.height / 2 - 18, 102, 92, Text.empty());

        addSelectableChild(partyNameWidget);

        addSelectableChild(partyCreateWidget);
        addSelectableChild(partyEditWidget);
        addSelectableChild(partyLeaveWidget);

        addSelectableChild(membersWidget);

        Random random = new Random();

//        while (partyInfo != null && partyInfo.partyInfoParts.size() < 15)
//            partyInfo.partyInfoParts.add(new PartyInfo.PartyInfoPart("Test" + random.nextInt(1000)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        partyNameWidget.render(matrices, mouseX, mouseY, delta);

        if (party == null) {
            partyLeaveWidget.active = false;

            partyNameWidget.setY(this.height / 2 - 76);
            partyNameWidget.setPlaceholder(Text.translatable("mineparties.gui.party.toname"));

            partyCreateWidget.setY(this.height / 2 - 77);
            partyCreateWidget.setTooltip(Tooltip.of(Text.translatable("mineparties.gui.party.createtooltip")));
        } else {
            partyLeaveWidget.active = true;

            partyNameWidget.setY(this.height / 2 - 55);
            partyNameWidget.setPlaceholder(Text.translatable("mineparties.gui.party.nameorinvite"));

            partyCreateWidget.setY(this.height / 2 - 57);
            partyCreateWidget.setTooltip(Tooltip.of(Text.translatable("mineparties.gui.party.invitetooltip")));

            textRenderer.draw(matrices, party.getName(), this.width / 2 - 120, this.height / 2 - 72, 0);

            partyEditWidget.render(matrices, mouseX, mouseY, delta);
            partyLeaveWidget.render(matrices, mouseX, mouseY, delta);
        }

        partyCreateWidget.render(matrices, mouseX, mouseY, delta);

        textRenderer.draw(matrices, MutableText.of(new TranslatableTextContent("mineparties.gui.party.members")), this.width / 2 - 120, this.height / 2 - 30, 0);
        textRenderer.draw(matrices, MutableText.of(new TranslatableTextContent("mineparties.gui.party.settings")), this.width / 2 + 10, this.height / 2 - 30, 0);

        membersWidget.render(matrices, mouseX, mouseY, delta);
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

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (this.client.options.inventoryKey.matchesKey(keyCode, scanCode) && !partyNameWidget.isFocused()) {
            this.close();
            return true;
        }
        return true;
    }
}
