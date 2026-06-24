package phiro.redstonetweaks.overlay;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import phiro.redstonetweaks.util.RedstoneBlockUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class Overlay {
    public abstract void render(Level level, BlockPos pos, BlockState state);
}
