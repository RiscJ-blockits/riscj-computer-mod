package edu.kit.riscjblockits.view.main.blocks.register;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class RegisterBlockEntity extends ComputerBlockEntity {
    public RegisterBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.REGISTER_BLOCK_ENTITY, pos, state);
    }

    @Override
    public @Nullable Object getRenderData() {
        return super.getRenderData();
    }
}
