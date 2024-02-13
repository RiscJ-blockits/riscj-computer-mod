package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * This class defines a register that can change its value based on the redstone power it receives.
 */
public class RedstoneInputBlock extends RegisterBlock {

    /**
     * Creates a new RedstoneInputBlockEntity.
     * @param pos The position of the block in the minecraft world for which the entity should be created.
     * @param state The state of the minecraft block for which the entity should be created.
     * @return A new RedstoneInputBlockEntity bound to the given block position.
     */
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneInputBlockEntity(pos, state);
    }

    /**
     * Called by neighboring blocks when they change. A change in redstone power level counts as a change.
     * @param state The state of the minecraft block.
     * @param world The world in which the block is placed.
     * @param pos The position of the block in the minecraft world.
     * @param sourceBlock The block that caused the change.
     * @param sourcePos The position of the block that caused the change.
     * @param notify Not defined.
     */
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
        int power = 0;
        for (Direction direction : Direction.values()) {
            power = Math.max(world.getEmittedRedstonePower(pos, direction), power);
        }
        ((RedstoneInputBlockEntity) world.getBlockEntity(pos)).setRedstonePower(power);
    }

}
