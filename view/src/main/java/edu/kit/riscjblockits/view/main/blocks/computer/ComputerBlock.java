package edu.kit.riscjblockits.view.main.blocks.computer;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class ComputerBlock extends ModBlock {
    public ComputerBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        // Connection Logic
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        //check if the block has been broken
        if (!world.isClient && state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ComputerBlockEntity) {
                ((ComputerBlockEntity) blockEntity).onBroken();
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, be) -> ComputerBlockEntity.tick(world1, pos, state1, (ComputerBlockEntity) be);
    }
}
