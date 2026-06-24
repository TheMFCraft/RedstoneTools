package phiro.redstonetweaks.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SignalStrengthOverlay extends Overlay {
    @Override
    public void render(Level level, BlockPos pos, BlockState state) {
        int signal = phiro.redstonetweaks.util.RedstoneBlockUtil.getSignalStrength(level, pos, state);
        if (signal > 0) {
            Minecraft mc = Minecraft.getInstance();
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;
            phiro.redstonetweaks.util.RenderUtil.drawText3D(
                String.valueOf(signal), x, y, z,
                1.0f, 1.0f, 0.2f, 0.2f, 1.0f
            );
        }
    }
}
