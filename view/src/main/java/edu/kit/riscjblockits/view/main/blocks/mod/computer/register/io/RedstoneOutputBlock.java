package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

/**
 * This class defines a register that always outputs the redstone power it has stored.
 */
public class RedstoneOutputBlock extends RegisterBlock {

    /**
     * Creates a new RedstoneOutputBlockEntity.
     * @param pos The position of the block in the minecraft world for which the entity should be created.
     * @param state The state of the minecraft block for which the entity should be created.
     * @return A new RedstoneOutputBlockEntity bound to the given block position.
     */
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneOutputBlockEntity(pos, state);
    }

    /**
     * We need to tell the world to treat this block as a redstone emitter.
     * @param state The state of the minecraft block.
     * @return always true
     */
    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    /**
     * This method is called by a neighboring block.
     * <a href="https://minecraft.fandom.com/wiki/Redstone_mechanics#Power">Difference between weak/strong power.</a>
     * @param state The state of the minecraft block.
     * @param world The world in which the block is placed.
     * @param pos The position of the block in the minecraft world.
     */
    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return ((RedstoneOutputBlockEntity) world.getBlockEntity(pos)).getRedstonePower();
    }

    /**
     * This method is called by a neighboring block.
     * <a href="https://minecraft.fandom.com/wiki/Redstone_mechanics#Power">Difference between weak/strong power.</a>
     * @param state The state of the minecraft block.
     * @param world The world in which the block is placed.
     * @param pos The position of the block in the minecraft world.
     */
    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return ((RedstoneOutputBlockEntity) world.getBlockEntity(pos)).getRedstonePower();
    }

    /**
     * We can schedule a tick for this block to update the redstone power. The tick is scheduled in the block entity.
     * @param state The state of the minecraft block.
     * @param world The world in which the block is placed.
     * @param pos The position of the block in the minecraft world.
     * @param random A random number generator.
     */
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.updateNeighbors(pos, this);
    }

}
