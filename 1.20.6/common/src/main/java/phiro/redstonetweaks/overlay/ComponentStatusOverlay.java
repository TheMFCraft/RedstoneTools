package phiro.redstonetweaks.overlay;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ComponentStatusOverlay extends Overlay {
    @Override
    public void render(Level level, BlockPos pos, BlockState state) {
        boolean on = phiro.redstonetweaks.util.RedstoneBlockUtil.isComponentOn(level, pos, state);
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        String text = on ? "ON" : "OFF";
        float r = on ? 0.2f : 0.5f;
        float g = on ? 1.0f : 0.5f;
        float b = on ? 0.2f : 0.5f;
        phiro.redstonetweaks.util.RenderUtil.drawText3D(text, x, y + 0.3, z, 0.7f, r, g, b, 1.0f);
    }
}
