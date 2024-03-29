package edu.kit.riscjblockits.view.main.blocks.mod.computer;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.bus.BusBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * All {@link ModBlock} that are needed to build a computer.
 * These Blocks can connect via or are a {@link BusBlock} to form a functioning computer.
 * Every ComputerBlock has a unique ComputerBlockEntity during runtime.
 */
public abstract class ComputerBlock extends ModBlock {

    /**
     * Determines if the register is active or not.
     */
    private static final BooleanProperty ACTIVE = RISCJ_blockits.ACTIVE_STATE_PROPERTY;

    /**
     * Creates a new ComputerBlock with the given settings.
     * @param settings the settings for the block.
     */
    protected ComputerBlock(AbstractBlock.Settings settings) {
        super(settings.luminance(state -> state.get(ACTIVE) ? 15 : 0).nonOpaque());
        setDefaultState(getDefaultState().with(ACTIVE, false));
    }

    /**
     * Creates a new ComputerBlock with default settings.
     */
    protected ComputerBlock() {
        super(FabricBlockSettings.create().luminance(state -> state.get(ACTIVE) ? 8 : 0).nonOpaque());
        setDefaultState(getDefaultState().with(ACTIVE, false));
    }

    /**
     * Called on every block state change.
     * Used to update the block entity prior to its destruction after the block has been broken.
     * @param state the old block state.
     * @param world the minecraft world the block is placed in.
     * @param pos the position of the block.
     * @param newState the new block state.
     * @param moved true if the block was moved, false otherwise.
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

    /**
     * When we want to get a block entity to receive every world tick,
     * we can register a receiving method inside the entity here.
     * @param world the minecraft world the block is placed in.
     * @param state the state of the block.
     * @param type the type of the block entity.
     * @return the block entity ticker for the block entity.
     * @param <T> the type of the block entity.
     */
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, be) -> ComputerBlockEntity.tick(world1, pos, state1, (ComputerBlockEntity) be);
    }

    /**
     * Is called by minecraft. We add our custom block state.
     * @param builder {@link net.minecraft.block.Block#appendProperties(StateManager.Builder)} for this block.
     */
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

}
