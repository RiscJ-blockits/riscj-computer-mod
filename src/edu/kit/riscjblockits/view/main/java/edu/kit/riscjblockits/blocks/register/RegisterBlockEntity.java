package edu.kit.riscjblockits.blocks.register;

import edu.kit.riscjblockits.blocks.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class RegisterBlockEntity extends ComputerBlockEntity {
    public RegisterBlockEntity(BlockEntityType<?> type, BlockPos pos,
                               BlockState state) {
        super(type, pos, state);
    }

    @Override
    public @Nullable Object getRenderData() {
        return super.getRenderData();
    }
}
