package robmart.mod.mineparties.client.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import robmart.mod.mineparties.api.client.event.*;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(at = @At(value = "HEAD"), method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V")
    private void disconnect(CallbackInfo ci) {
        ClientStatusEvents.DISCONNECT_CLIENT_EVENT.invoker().onDisconnect((MinecraftClient) (Object) this);
    }
}
