package edu.kit.riscjblockits.view.main.blocks.programming;

import edu.kit.riscjblockits.view.main.blocks.mod.ModBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ProgrammingBlock extends ModBlock {
    public ProgrammingBlock(Settings settings) {
        super(settings);
    }

    /**
     * Creates a new ProgrammingBlock with default settings.
     */
    public ProgrammingBlock() {
        super();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ProgrammingBlockEntity(pos, state);
    }


}
