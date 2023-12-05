package edu.kit.riscjblockits.blocks.computer;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class ComputerBlockEntity extends BlockEntity {
    public ComputerBlockEntity(BlockEntityType<?> type, BlockPos pos,
                               BlockState state) {
        super(type, pos, state);
    }
}
