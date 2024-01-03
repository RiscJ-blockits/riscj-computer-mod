package edu.kit.riscjblockits.view.main.blocks.systemclock;

import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

/**
 * This class defines all computer clocks in the game.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class SystemClockBlock extends ComputerBlock {

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

}
