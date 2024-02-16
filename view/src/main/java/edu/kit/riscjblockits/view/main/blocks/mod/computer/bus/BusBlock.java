package edu.kit.riscjblockits.view.main.blocks.mod.computer.bus;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ConnectingComputerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

/**
 * This class defines all bus blocks in the game.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class BusBlock extends ConnectingComputerBlock {

    /**
     * Creates a new BusBlock with the given settings.
     * @param settings The settings for the block as {@link net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings}.
     */
    public BusBlock(Settings settings) {
        super((float) 3 / 16, settings);
        this.setDefaultState(
                this.stateManager.getDefaultState()
                    .with(NORTH, Side.NONE)
                    .with(EAST, Side.NONE)
                    .with(SOUTH, Side.NONE)
                    .with(WEST, Side.NONE)
                    .with(UP, Side.NONE)
                    .with(DOWN, Side.NONE)
                    .with(RISCJ_blockits.ACTIVE_STATE_PROPERTY, false));
    }

    /**
     * Creates a new BusBlock with default settings.
     */
    public BusBlock() {
        super((float) 3 / 16);
        this.setDefaultState(
                this.stateManager.getDefaultState()
                        .with(NORTH, Side.NONE)
                        .with(EAST, Side.NONE)
                        .with(SOUTH, Side.NONE)
                        .with(WEST, Side.NONE)
                        .with(UP, Side.NONE)
                        .with(DOWN, Side.NONE)
                        .with(RISCJ_blockits.ACTIVE_STATE_PROPERTY, false));
    }

    /**
     * Creates a new entity for a bus.
     * This method is invoked by minecraft when the block is loaded.
     * @nullable This is marked as nullable for one minecraft internal call but should never return null.
     * See {@link net.minecraft.block.BlockEntityProvider#createBlockEntity(BlockPos, BlockState)}.
     *
     * @param pos The position of the block in the minecraft world for which the entity should be created.
     * @param state The state of the minecraft block for which the entity should be created.
     * @return A new default instance of {@link BusBlockEntity}.
     */
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BusBlockEntity(pos, state);
    }

    /**
     * This method is called when the block is placed in the world.
     * The Block initializes the creation of his Block Controller and updates the bus-block state.
     * @param world The minecraft world the block is placed in.
     * @param pos The position the block is placed at.
     * @param state The Block-State of the block.
     * @param placer The entity that placed the block.
     * @param itemStack The itemstack that was used to place the block.
     */
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
                         ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        ((BusBlockEntity) world.getBlockEntity(pos)).updateBlockState();
    }

    /**
     * Will update the block-state when a neighbor block changes.
     * @param state the own block state.
     * @param direction the direction of the neighborhood block.
     * @param neighborState the state of the neighborhood block.
     * @param world the world the block is placed in.
     * @param pos the position of the block.
     * @param neighborPos the position of the neighborhood block.
     * @return the updated block-state.
     */
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            world.scheduleBlockTick(pos, this, 1);
        } else {
            ((BusBlockEntity) world.getBlockEntity(pos)).updateBlockState();
        }
        return state;
    }

    /**
     * Will append the properties of this block to the given builder.
     * @param builder {@link net.minecraft.block.Block#appendProperties(StateManager.Builder)} for this block.
     */
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
        super.appendProperties(builder);
    }

}
