package phiro.redstonetweaks.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import phiro.redstonetweaks.config.OverlayConfig;
import phiro.redstonetweaks.config.RedstoneTweaksConfig;
import phiro.redstonetweaks.util.RedstoneBlockUtil;

import java.util.ArrayList;
import java.util.List;

public class RedstoneOverlayHandler {
    private final List<Overlay> overlays = new ArrayList<>();

    public RedstoneOverlayHandler() {
        overlays.add(new SignalStrengthOverlay());
        overlays.add(new ComponentStatusOverlay());
        overlays.add(new BlockUpdateOverlay());
        overlays.add(new ConnectionOverlay());
    }

    public void onRenderWorld() {
        OverlayConfig config = RedstoneTweaksConfig.CONFIG;
        if (!config.overlayEnabled) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        Level level = mc.level;
        int range = 16;
        BlockPos center = mc.player.blockPosition();

        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                for (int dz = -range; dz <= range; dz++) {
                    BlockPos pos = center.offset(dx, dy, dz);
                    if (!level.isInWorldBounds(pos)) continue;
                    BlockState state = level.getBlockState(pos);
                    if (!RedstoneBlockUtil.isRedstoneComponent(state)) continue;

                    double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    if (dist > range) continue;

                    for (Overlay overlay : overlays) {
                        if (shouldRender(overlay, config)) {
                            overlay.render(level, pos, state);
                        }
                    }
                }
            }
        }
    }

    private boolean shouldRender(Overlay overlay, OverlayConfig config) {
        if (overlay instanceof SignalStrengthOverlay) return config.showSignalStrength;
        if (overlay instanceof ComponentStatusOverlay) return config.showComponentStatus;
        if (overlay instanceof BlockUpdateOverlay) return config.showBlockUpdates;
        if (overlay instanceof ConnectionOverlay) return config.showWireConnections;
        return true;
    }
}
