package phiro.redstonetweaks.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import phiro.redstonetweaks.config.RedstoneTweaksConfig;
import phiro.redstonetweaks.overlay.RedstoneOverlayHandler;

public class RedstoneTweaksFabric implements ClientModInitializer {
    private static KeyMapping toggleKey;
    private final RedstoneOverlayHandler handler = new RedstoneOverlayHandler();

    @Override
    public void onInitializeClient() {
        RedstoneTweaksConfig.init();

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.redstonetweaks.toggle",
            GLFW.GLFW_KEY_UNKNOWN,
            "category.redstonetweaks"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                RedstoneTweaksConfig.CONFIG.overlayEnabled = !RedstoneTweaksConfig.CONFIG.overlayEnabled;
                RedstoneTweaksConfig.save();
            }
        });

        WorldRenderEvents.LAST.register(context -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && context.camera().isInitialized() && RedstoneTweaksConfig.CONFIG.overlayEnabled) {
                handler.onRenderWorld();
            }
        });
    }
}
