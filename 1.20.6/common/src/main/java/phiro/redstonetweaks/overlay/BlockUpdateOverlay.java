package phiro.redstonetweaks.overlay;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import phiro.redstonetweaks.util.RenderUtil;

import java.util.List;

public class BlockUpdateOverlay extends Overlay {
    @Override
    public void render(Level level, BlockPos pos, BlockState state) {
        List<BlockPos> targets = phiro.redstonetweaks.util.RedstoneBlockUtil.getUpdateTargets(level, pos, state);
        double x1 = pos.getX() + 0.5, y1 = pos.getY() + 0.5, z1 = pos.getZ() + 0.5;

        for (BlockPos target : targets) {
            double x2 = target.getX() + 0.5, y2 = target.getY() + 0.5, z2 = target.getZ() + 0.5;
            RenderUtil.drawLine(x1, y1, z1, x2, y2, z2, 1.0f, 1.0f, 0.0f, 0.6f);
            RenderUtil.drawBoxHighlight(target, 1.0f, 1.0f, 0.0f, 0.3f);
        }
    }
}
