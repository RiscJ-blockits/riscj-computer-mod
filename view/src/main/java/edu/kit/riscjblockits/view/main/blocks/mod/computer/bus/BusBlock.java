package edu.kit.riscjblockits.view.main.blocks.mod.computer.bus;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ConnectingComputerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

/**
 * This class defines all bus blocks in the game.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class BusBlock extends ConnectingComputerBlock {

    private static final BooleanProperty ACTIVE = RISCJ_blockits.ACTIVE_STATE_PROPERTY;

    /**
     * Creates a new BusBlock with the given settings.
     * @param settings The settings for the block as {@link net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings}.
     */
    public BusBlock(Settings settings) {
        super((float) 3 /16, settings);
        this.setDefaultState(
                this.stateManager.getDefaultState()
                    .with(NORTH, false)
                    .with(EAST, false)
                    .with(SOUTH, false)
                    .with(WEST, false)
                    .with(UP, false)
                    .with(DOWN, false));

    }

    /**
     * Creates a new BusBlock with default settings.
     */
    public BusBlock() {
        super((float) 3 /16);
        this.setDefaultState(
                this.stateManager.getDefaultState()
                        .with(NORTH, false)
                        .with(EAST, false)
                        .with(SOUTH, false)
                        .with(WEST, false)
                        .with(UP, false)
                        .with(DOWN, false)
                        .with(ACTIVE, false));

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
     * Will get the state of the block when it is placed in the world.
     * @param ctx
     * @return
     */
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.withConnectionProperties(ctx.getWorld(), ctx.getBlockPos());
    }

    /**
     * Will add the connection properties to the blank block-state.
     * @param world
     * @param pos
     * @return
     */
    public BlockState withConnectionProperties(BlockView world, BlockPos pos) {
        BlockState stateDown = world.getBlockState(pos.down());
        BlockState stateUp = world.getBlockState(pos.up());
        BlockState stateNorth = world.getBlockState(pos.north());
        BlockState stateEast = world.getBlockState(pos.east());
        BlockState stateSouth = world.getBlockState(pos.south());
        BlockState stateWest = world.getBlockState(pos.west());
        return this.getDefaultState()
                .with(DOWN, stateDown.isIn(RISCJ_blockits.COMPUTER_BLOCK_TAG))
                .with(UP, stateUp.isIn(RISCJ_blockits.COMPUTER_BLOCK_TAG))
                .with(NORTH, stateNorth.isIn(RISCJ_blockits.COMPUTER_BLOCK_TAG))
                .with(EAST, stateEast.isIn(RISCJ_blockits.COMPUTER_BLOCK_TAG))
                .with(SOUTH, stateSouth.isIn(RISCJ_blockits.COMPUTER_BLOCK_TAG))
                .with(WEST, stateWest.isIn(RISCJ_blockits.COMPUTER_BLOCK_TAG));
    }


    /**
     * Will update the block-state when a neighbor block changes.
     * @param state the own block state.
     * @param direction the direction of the neighbor block.
     * @param neighborState the state of the neighbor block.
     * @param world the world the block is placed in.
     * @param pos the position of the block.
     * @param neighborPos the position of the neighbor block.
     * @return the updated block-state.
     */
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            world.scheduleBlockTick(pos, this, 1);
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        } else {
            boolean bl = neighborState.isIn(RISCJ_blockits.COMPUTER_BLOCK_TAG);
            return state.with(FACING_PROPERTIES.get(direction), bl);
        }
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
