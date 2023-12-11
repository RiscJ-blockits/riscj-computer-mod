package edu.kit.riscjblockits.view.main.blocks.modblock;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public abstract class ModblockEntity extends BlockEntity {
    public ModblockEntity(BlockEntityType<?> type, BlockPos pos,
                               BlockState state) {
        super(type, pos, state);
    }
}
