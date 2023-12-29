package edu.kit.riscjblockits.view.main.blocks.alu;

import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class AluBlock extends ComputerBlock {
    public AluBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    /**
     * Creates a new AluBlock with default settings.
     */
    public AluBlock() {
        super();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AluBlockEntity(pos, state);
    }
}
