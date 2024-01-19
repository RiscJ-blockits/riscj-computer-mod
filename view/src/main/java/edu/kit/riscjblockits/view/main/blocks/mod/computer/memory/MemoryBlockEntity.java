package edu.kit.riscjblockits.view.main.blocks.mod.computer.memory;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.MemoryController;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntityWithInventory;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

/**
 * This class represents a memory entity from our mod in the game.
 * Every memory block has its own unique MemoryBlockEntity while it is loaded.
 */
public class MemoryBlockEntity extends ComputerBlockEntityWithInventory {

    /**
     * Creates a new MemoryBlockEntity with the given settings.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     */
    public MemoryBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.MEMORY_BLOCK_ENTITY, pos, state, 1);
    }

    /**
     * Every entity needs its own controller.
     * @return An MemoryController bound to this entity.
     */
    @Override
    protected ComputerBlockController createController() {
        return new MemoryController(this);
    }

}
