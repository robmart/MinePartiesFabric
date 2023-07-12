package robmart.mod.mineparties.client.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import robmart.mod.mineparties.client.gui.screen.PartyScreen;
import robmart.mod.mineparties.common.networking.PartyInfo;

public class PartyPlayerScrollableWidget extends ScrollableWidget {
    private final MinecraftClient client;
    private final PartyScreen parent;

    public PartyPlayerScrollableWidget(MinecraftClient client, PartyScreen parent, int x, int y, int width, int height, Text text) {
        super(x, y, width, height, text);
        this.client = client;
        this.parent = parent;
    }

    @Override
    protected int getContentsHeight() {
        if (PartyScreen.partyInfo == null) return 0;
        return PartyScreen.partyInfo.partyInfoParts.size() * (client.textRenderer.fontHeight + 1);
    }

    @Override
    protected boolean overflows() {
        return getContentsHeight() > getHeight();
    }

    @Override
    protected double getDeltaYPerScroll() {
        return 0;
    }

    @Override
    protected void renderContents(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (PartyScreen.partyInfo == null) return;
        int entryCount = PartyScreen.partyInfo.partyInfoParts.size();
        for (int index = 0; index < entryCount; ++index) {
            int entryTop = this.getY() + 2 + (index * (client.textRenderer.fontHeight + 1));
            int entryLeft = this.getX() + 2;

            PartyInfo.PartyInfoPart entry = PartyScreen.partyInfo.partyInfoParts.get(index);
            client.textRenderer.draw(matrices, entry.playerId, entryLeft, entryTop, 0xffffff);
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}
