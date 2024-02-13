package edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock;

import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * This class defines all computer clocks in the game.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class SystemClockBlock extends ComputerBlock {

    public static final int MAX_CURSORPOS = 7;

    public static final IntProperty CURSORPOS = IntProperty.of("cursorpos", 0, MAX_CURSORPOS);



    /**
     * Creates a new SystemClockBlock with the given settings.
     * @param settings The settings for the block as {@link net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings}.
     */
    public SystemClockBlock(Settings settings) {
        super(settings);
    }

    /**
     * Creates a new SystemClockBlock with default settings.
     */
    public SystemClockBlock() {
        super();
    }

    /**
     * Creates a new entity for a system clock.
     * This method is invoked by minecraft when the block is loaded.
     * @nullable This is marked as nullable for one minecraft internal call but should never return null.
     * See {@link net.minecraft.block.BlockEntityProvider#createBlockEntity(BlockPos, BlockState)}.
     *
     * @param pos The position of the block in the minecraft world for which the entity should be created.
     * @param state The state of the minecraft block for which the entity should be created.
     * @return A new default instance of {@link SystemClockBlockEntity}.
     */
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SystemClockBlockEntity(pos, state);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        updatePowered(state, world, pos);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        updatePowered(state, world, pos);
    }

    private void updatePowered(BlockState state, World world, BlockPos pos) {
        ((SystemClockBlockEntity) world.getBlockEntity(pos)).setPowered(world.isReceivingRedstonePower(pos));
    }

    /**
     * Will append the properties of this block to the given builder.
     * @param builder {@link net.minecraft.block.Block#appendProperties(StateManager.Builder)} for this block.
     */
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CURSORPOS);
        super.appendProperties(builder);
    }
}
