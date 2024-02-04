package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

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
