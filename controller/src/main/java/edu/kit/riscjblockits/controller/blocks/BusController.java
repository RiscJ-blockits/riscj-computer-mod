package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.BusModel;

/**
 * The controller for the bus block.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class BusController extends BlockController{

    /**
     * Creates a new BusController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public BusController(IQueryableBlockEntity blockEntity) {
        super(blockEntity);
    }

    /**
     * Creates the bus-specific model.
     * @return The model for the bus.
     */
    @Override
    protected BlockModel createBlockModel() {
        return new BusModel();
    }

    //ToDo remove
    @Override
    public boolean isBus() {
        return true;
    }

}
