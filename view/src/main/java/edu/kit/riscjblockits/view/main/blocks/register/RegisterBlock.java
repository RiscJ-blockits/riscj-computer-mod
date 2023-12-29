package edu.kit.riscjblockits.view.main.blocks.register;


import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class RegisterBlock  extends ComputerBlock {
    public RegisterBlock(Settings settings) {
        super(settings);
    }

    /**
     * Creates a new RegisterBlock with default settings.
     */
    public RegisterBlock() {
        super();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RegisterBlockEntity(pos, state);
    }
}
