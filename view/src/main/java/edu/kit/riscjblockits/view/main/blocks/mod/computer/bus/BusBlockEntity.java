package edu.kit.riscjblockits.view.main.blocks.mod.computer.bus;

import edu.kit.riscjblockits.controller.blocks.BusController;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.EntityType;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

/**
 * This class represents a bus entity from our mod in the game.
 * Every bus has its own unique BusBlockEntity while it is loaded.
 */
public class BusBlockEntity extends ComputerBlockEntity {

    /**
     * Creates a new BusBlockEntity with the given settings.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     */
    public BusBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.BUS_BLOCK_ENTITY, pos, state);
        setType(EntityType.CONNECTABLE);
    }

    /**
     * Every entity needs its own controller.
     * @return An BusController bound to this entity.
     */
    @Override
    protected ComputerBlockController createController() {
        return new BusController(this);
    }

}
