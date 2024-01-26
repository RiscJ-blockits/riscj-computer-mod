package edu.kit.riscjblockits.view.main.blocks.mod.computer.bus;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ConnectingComputerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
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

    private static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    /**
     * Creates a new BusBlock with the given settings.
     * @param settings The settings for the block as {@link net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings}.
     */
    public BusBlock(Settings settings) {
        super((float) 3 /16);
        this.setDefaultState(
                this.stateManager.getDefaultState()
                    .with(NORTH, false)
                    .with(EAST, false)
                    .with(SOUTH, false)
                    .with(WEST, false)
                    .with(UP, true)
                    .with(DOWN, false)
                    .with(ACTIVE, false));

    }

    /**
     * Creates a new BusBlock with default settings.
     */
    public BusBlock() {
        super((float) 3 /16);
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

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.withConnectionProperties(ctx.getWorld(), ctx.getBlockPos());
    }

    public BlockState withConnectionProperties(BlockView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.down());
        BlockState blockState2 = world.getBlockState(pos.up());
        BlockState blockState3 = world.getBlockState(pos.north());
        BlockState blockState4 = world.getBlockState(pos.east());
        BlockState blockState5 = world.getBlockState(pos.south());
        BlockState blockState6 = world.getBlockState(pos.west());
        return this.getDefaultState()
                .with(DOWN, blockState.isIn(RISCJ_blockits.COMPUTER_BLOCK_TAG))
                .with(UP, blockState2.isIn(RISCJ_blockits.COMPUTER_BLOCK_TAG))
                .with(NORTH, blockState3.isIn(RISCJ_blockits.COMPUTER_BLOCK_TAG))
                .with(EAST, blockState4.isIn(RISCJ_blockits.COMPUTER_BLOCK_TAG))
                .with(SOUTH, blockState5.isIn(RISCJ_blockits.COMPUTER_BLOCK_TAG))
                .with(WEST, blockState6.isIn(RISCJ_blockits.COMPUTER_BLOCK_TAG));
    }


    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            world.scheduleBlockTick(pos, this, 1);
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        } else {
            boolean bl = neighborState.isIn(RISCJ_blockits.COMPUTER_BLOCK_TAG);
            return (BlockState)state.with((Property)FACING_PROPERTIES.get(direction), bl);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, ACTIVE);
        super.appendProperties(builder);
    }

}
