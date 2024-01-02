package edu.kit.riscjblockits.view.main.blocks.memory;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.MemoryController;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class MemoryBlockEntity extends ComputerBlockEntity {
    public MemoryBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.MEMORY_BLOCK_ENTITY, pos, state);
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    protected ComputerBlockController createController() {
        return new MemoryController(this);
    }
}
