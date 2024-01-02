package edu.kit.riscjblockits.view.main.blocks.systemclock;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class SystemClockBlockEntity extends ComputerBlockEntity {
    public SystemClockBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.SYSTEM_CLOCK_BLOCK_ENTITY, pos, state);
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    protected ComputerBlockController createController() {
        return new SystemClockController(this);
    }
}
