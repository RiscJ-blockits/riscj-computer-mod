package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
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
     * This method is called when the block is right-clicked.
     * We override it because unlike a normal register, a redstone output block has no screen.
     * @param state The state of the minecraft block.
     * @param world The world in which the block is placed.
     * @param pos The position of the block in the minecraft world.
     * @param player The player that right-clicked on the block.
     * @param hand The hand with which the player right-clicked on the block.
     * @param hit The hit result of the ray cast that was used to determine the block that was right-clicked on.
     * @return A PASS action result to indicate that we don't want to do anything when the block is right-clicked.
     */
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                              BlockHitResult hit) {
        //we don't want to do anything when the block is right-clicked
        return ActionResult.PASS;
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
