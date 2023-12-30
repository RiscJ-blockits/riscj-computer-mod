package edu.kit.riscjblockits.view.main.blocks.systemclock;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

/**
 * This class represents a system clock entity from our mod in the game.
 * Every system clock has its own unique SystemClockBlockEntity while it is loaded.
 */
public class SystemClockBlockEntity extends ComputerBlockEntity {

    /**
     * Creates a new SystemClockBlockEntity with the given settings.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     */
    public SystemClockBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.SYSTEM_CLOCK_BLOCK_ENTITY, pos, state);
    }

    /**
     * Every entity needs its own controller.
     * @return An SystemClockController bound to this entity.
     */
    @Override
    protected BlockController createController() {
        return new SystemClockController(this);
    }

    /**
     * ToDo
     * @return
     */
    @Override
    public String getInfo() {
        return null;
    }

}
