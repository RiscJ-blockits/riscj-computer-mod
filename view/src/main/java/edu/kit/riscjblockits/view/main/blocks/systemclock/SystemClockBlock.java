package edu.kit.riscjblockits.view.main.blocks.systemclock;


import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class SystemClockBlock extends ComputerBlock {
    public SystemClockBlock(Settings settings) {
        super(settings);
    }

    /**
     * Creates a new SystemClockBlock with default settings.
     */
    public SystemClockBlock() {
        super();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SystemClockBlockEntity(pos, state);
    }
}
