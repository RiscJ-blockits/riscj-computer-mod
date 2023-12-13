package edu.kit.riscjblockits.view.main.blocks.controlunit;


import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ControlUnitBlock extends ComputerBlock {


    public ControlUnitBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ControlUnitBlockEntity(pos, state);
    }
}
