package edu.kit.riscjblockits.blocks.systemclock;

import edu.kit.riscjblockits.blocks.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class SystemClockBlockEntity extends ComputerBlockEntity {
    public SystemClockBlockEntity(BlockEntityType<?> type, BlockPos pos,
                                  BlockState state) {
        super(type, pos, state);
    }
}
