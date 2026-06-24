package phiro.redstonetweaks.neoforge;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;
import phiro.redstonetweaks.config.RedstoneTweaksConfig;
import phiro.redstonetweaks.overlay.RedstoneOverlayHandler;

@Mod(value = "redstonetweaks", dist = Dist.CLIENT)
public class RedstoneTweaksNeoForge {
    private static KeyMapping toggleKey;
    private final RedstoneOverlayHandler handler = new RedstoneOverlayHandler();

    public RedstoneTweaksNeoForge(IEventBus modBus) {
        modBus.addListener(this::onKeyMappingRegister);
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
        NeoForge.EVENT_BUS.addListener(this::onRenderLevel);
    }

    private void onKeyMappingRegister(RegisterKeyMappingsEvent event) {
        toggleKey = new KeyMapping(
            "key.redstonetweaks.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.redstonetweaks"
        );
        event.register(toggleKey);
    }

    private void onClientTick(ClientTickEvent.Post event) {
        while (toggleKey.consumeClick()) {
            RedstoneTweaksConfig.CONFIG.overlayEnabled = !RedstoneTweaksConfig.CONFIG.overlayEnabled;
            RedstoneTweaksConfig.save();
        }
    }

    private void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null || !RedstoneTweaksConfig.CONFIG.overlayEnabled) return;
        handler.onRenderWorld();
    }
}
