package edu.kit.riscjblockits.view.main.blocks.memory;


import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class MemoryBlock extends ComputerBlock {
    public MemoryBlock(Settings settings) {
        super(settings);
    }

    /**
     * Creates a new MemoryBlock with default settings.
     */
    public MemoryBlock() {
        super();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MemoryBlockEntity(pos, state);
    }
}
