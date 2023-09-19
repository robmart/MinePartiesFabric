package robmart.mod.mineparties.client.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import robmart.mod.mineparties.client.gui.screen.PartyScreen;

import java.util.List;

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
        if (PartyScreen.party == null) return 0;
        return (PartyScreen.party.getAllPlayers().size() + 1) * (client.textRenderer.fontHeight + 1);
    }

    @Override
    protected boolean overflows() {
        return getContentsHeight() > getHeight();
    }

    @Override
    protected double getDeltaYPerScroll() {
        return client.textRenderer.fontHeight + 1;
    }

    @Override
    protected void renderContents(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (PartyScreen.party == null) return;
        int entryCount = PartyScreen.party.getAllPlayers().size();
        List<String> players = PartyScreen.party.getAllPlayers();
        for (int index = 0; index < entryCount; ++index) {
            int entryTop = this.getY() + 2 + (index * (client.textRenderer.fontHeight + 1));
            int entryLeft = this.getX() + 2;

            String entry = players.get(index);
            client.textRenderer.draw(matrices, entry, entryLeft, entryTop, 0xffffff);
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}
