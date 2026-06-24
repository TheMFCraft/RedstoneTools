package phiro.redstonetweaks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class RedstoneBlockUtil {

    public static boolean isRedstoneComponent(BlockState state) {
        Block b = state.getBlock();
        return b instanceof RedStoneWireBlock
                || b instanceof RepeaterBlock
                || b instanceof ComparatorBlock
                || b instanceof LeverBlock
                || b instanceof ButtonBlock
                || b instanceof PressurePlateBlock
                || b instanceof WeightedPressurePlateBlock
                || b instanceof TripWireHookBlock
                || b instanceof RedstoneLampBlock
                || b instanceof PistonBaseBlock
                || b instanceof PistonHeadBlock
                || b instanceof DispenserBlock
                || b instanceof DropperBlock
                || b instanceof DoorBlock
                || b instanceof FenceGateBlock
                || b instanceof TrapDoorBlock
                || b instanceof NoteBlock
                || b instanceof BellBlock
                || b instanceof SculkSensorBlock
                || b instanceof DaylightDetectorBlock
                || b instanceof TargetBlock
                || b instanceof ObserverBlock
                || b instanceof PoweredRailBlock
                || b instanceof RedstoneTorchBlock
                || b instanceof RedstoneWallTorchBlock;
    }

    public static int getSignalStrength(Level level, BlockPos pos, BlockState state) {
        Block b = state.getBlock();
        if (b instanceof RedStoneWireBlock) {
            return state.getValue(RedStoneWireBlock.POWER);
        }
        if (b instanceof RepeaterBlock) {
            return state.getValue(RepeaterBlock.POWERED) ? 15 : 0;
        }
        if (b instanceof ComparatorBlock) {
            return state.getValue(ComparatorBlock.POWERED) ? 15 : 0;
        }
        if (b instanceof LeverBlock) {
            return state.getValue(LeverBlock.POWERED) ? 15 : 0;
        }
        if (b instanceof ButtonBlock) {
            return state.getValue(ButtonBlock.POWERED) ? 15 : 0;
        }
        if (b instanceof PressurePlateBlock) {
            return state.getValue(PressurePlateBlock.POWERED) ? 15 : 0;
        }
        if (b instanceof WeightedPressurePlateBlock) {
            return state.getValue(WeightedPressurePlateBlock.POWER);
        }
        if (b instanceof TripWireHookBlock) {
            return state.getValue(TripWireHookBlock.POWERED) ? 15 : 0;
        }
        if (b instanceof SculkSensorBlock) {
            return state.getValue(SculkSensorBlock.POWER);
        }
        if (b instanceof TargetBlock) {
            return level.getBestNeighborSignal(pos);
        }
        if (b instanceof ObserverBlock) {
            return state.getValue(ObserverBlock.POWERED) ? 15 : 0;
        }
        if (b instanceof DaylightDetectorBlock) {
            return state.getValue(DaylightDetectorBlock.POWER);
        }
        if (b instanceof RedstoneTorchBlock || b instanceof RedstoneWallTorchBlock) {
            return state.getValue(RedstoneTorchBlock.LIT) ? 15 : 0;
        }
        return 0;
    }

    public static boolean isComponentOn(Level level, BlockPos pos, BlockState state) {
        Block b = state.getBlock();
        if (b instanceof RepeaterBlock) {
            return state.getValue(RepeaterBlock.POWERED);
        }
        if (b instanceof ComparatorBlock) {
            return state.getValue(ComparatorBlock.POWERED);
        }
        if (b instanceof LeverBlock) {
            return state.getValue(LeverBlock.POWERED);
        }
        if (b instanceof ButtonBlock) {
            return state.getValue(ButtonBlock.POWERED);
        }
        if (b instanceof PressurePlateBlock) {
            return state.getValue(PressurePlateBlock.POWERED);
        }
        if (b instanceof WeightedPressurePlateBlock) {
            return state.getValue(WeightedPressurePlateBlock.POWER) > 0;
        }
        if (b instanceof TripWireHookBlock) {
            return state.getValue(TripWireHookBlock.POWERED);
        }
        if (b instanceof RedstoneLampBlock) {
            return state.getValue(RedstoneLampBlock.LIT);
        }
        if (b instanceof PistonBaseBlock) {
            return state.getValue(PistonBaseBlock.EXTENDED);
        }
        if (b instanceof DoorBlock) {
            return state.getValue(DoorBlock.OPEN);
        }
        if (b instanceof FenceGateBlock) {
            return state.getValue(FenceGateBlock.OPEN);
        }
        if (b instanceof TrapDoorBlock) {
            return state.getValue(TrapDoorBlock.OPEN);
        }
        if (b instanceof NoteBlock) {
            return state.getValue(NoteBlock.POWERED);
        }
        if (b instanceof BellBlock) {
            return state.getValue(BellBlock.POWERED);
        }
        if (b instanceof SculkSensorBlock) {
            return state.getValue(SculkSensorBlock.POWER) > 0;
        }
        if (b instanceof TargetBlock) {
            return level.getBestNeighborSignal(pos) > 0;
        }
        if (b instanceof ObserverBlock) {
            return state.getValue(ObserverBlock.POWERED);
        }
        if (b instanceof RedstoneTorchBlock || b instanceof RedstoneWallTorchBlock) {
            return state.getValue(RedstoneTorchBlock.LIT);
        }
        return getSignalStrength(level, pos, state) > 0;
    }

    public static List<BlockPos> getUpdateTargets(Level level, BlockPos pos, BlockState state) {
        List<BlockPos> targets = new ArrayList<>();
        Block b = state.getBlock();

        for (Direction dir : Direction.values()) {
            BlockPos targetPos = pos.relative(dir);
            if (!level.isInWorldBounds(targetPos)) continue;
            BlockState targetState = level.getBlockState(targetPos);
            if (targetState.isRedstoneConductor(level, targetPos)) {
                for (Direction inner : Direction.values()) {
                    BlockPos innerPos = targetPos.relative(inner);
                    if (!innerPos.equals(pos)) {
                        BlockState innerState = level.getBlockState(innerPos);
                        if (isRedstoneComponent(innerState)) {
                            targets.add(innerPos);
                        }
                    }
                }
            } else if (isRedstoneComponent(targetState)) {
                targets.add(targetPos);
            }
        }
        return targets;
    }

    public static List<BlockPos> getWireConnections(Level level, BlockPos pos) {
        List<BlockPos> connections = new ArrayList<>();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof RedStoneWireBlock)) return connections;

        int power = state.getValue(RedStoneWireBlock.POWER);
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = pos.relative(dir);
            if (!level.isInWorldBounds(neighbor)) continue;
            BlockState neighborState = level.getBlockState(neighbor);
            if (neighborState.getBlock() instanceof RedStoneWireBlock) {
                connections.add(neighbor);
            }
        }
        return connections;
    }
}
