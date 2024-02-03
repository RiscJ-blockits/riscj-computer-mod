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

public class RedstoneInputBlock extends RegisterBlock {

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneInputBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                              BlockHitResult hit) {
        //we don't want to do anything when the block is right-clicked
        return ActionResult.PASS;
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
        int power = 0;
        for (Direction direction : Direction.values()) {
            power = Math.max(world.getEmittedRedstonePower(pos, direction), power);
        }
        ((RedstoneInputBlockEntity) world.getBlockEntity(pos)).setRedstonePower(power);
    }

}
