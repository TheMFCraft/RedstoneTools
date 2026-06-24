package phiro.redstonetweaks.overlay;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import phiro.redstonetweaks.util.RenderUtil;

import java.util.List;

public class ConnectionOverlay extends Overlay {
    @Override
    public void render(Level level, BlockPos pos, BlockState state) {
        List<BlockPos> connections = phiro.redstonetweaks.util.RedstoneBlockUtil.getWireConnections(level, pos);
        if (connections.isEmpty()) return;

        double x1 = pos.getX() + 0.5, y1 = pos.getY() + 0.5, z1 = pos.getZ() + 0.5;

        for (BlockPos conn : connections) {
            double x2 = conn.getX() + 0.5, y2 = conn.getY() + 0.5, z2 = conn.getZ() + 0.5;
            int power = state.getValue(RedStoneWireBlock.POWER);
            float brightness = power / 15.0f;
            RenderUtil.drawLine(x1, y1, z1, x2, y2, z2, brightness, 0.0f, 0.0f, 0.8f);
        }
    }
}
