package edu.kit.riscjblockits.view.main.blocks.mod.computer;

import edu.kit.riscjblockits.view.main.blocks.mod.ModBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.bus.BusBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * All {@link ModBlock} that are needed to build a computer.
 * These Blocks can connect via or are a {@link BusBlock} to form a functioning computer.
 * Every ComputerBlock has a unique ComputerBlockEntity during runtime.
 */
public abstract class ComputerBlock extends ModBlock {

    /**
     * Creates a new ComputerBlock with the given settings.
     * @param settings the settings for the block.
     */
    protected ComputerBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    /**
     * Creates a new ComputerBlock with default settings.
     */
    protected ComputerBlock() {
        super();
    }

    /**
     * Called on every block state change.
     * Used to update the block entity, prior to its destruction after the block has been broken.
     *
     * @param state the old block state.
     * @param world the minecraft world the block is placed in.
     * @param pos the position of the block.
     * @param newState the new block state.
     * @param moved true if the block was moved, false otherwise.
     *
     */
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

    /** TODO Javadoc - Get help from Nils
     *
     * @param world the minecraft world the block is placed in.
     * @param state the state of the block.
     * @param type the type of the block entity.
     * @return
     * @param <T>
     */
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, be) -> ComputerBlockEntity.tick(world1, pos, state1, (ComputerBlockEntity) be);
    }
}
