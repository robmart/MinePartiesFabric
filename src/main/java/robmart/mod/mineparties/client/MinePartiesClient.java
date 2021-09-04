package robmart.mod.mineparties.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;
import robmart.mod.mineparties.client.gui.screen.PartyScreen;
import robmart.mod.targetingapifabric.api.reference.Reference;

@Environment(EnvType.CLIENT)
public class MinePartiesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.examplemod.spook", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R, // The keycode of the key
                Reference.MOD_ID + ".party" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                client.setScreen(new PartyScreen());
            }
        });
    }
}
