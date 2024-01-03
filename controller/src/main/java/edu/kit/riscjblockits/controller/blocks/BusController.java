package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.BusModel;
import edu.kit.riscjblockits.model.data.IDataElement;

/**
 * The controller for the bus block.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class BusController extends ComputerBlockController{

    /**
     * Creates a new BusController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public BusController(IConnectableComputerBlockEntity blockEntity) {
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

    @Override
    public void setData(IDataElement data) {

    }
}
