package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BusModel;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
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
        setControllerType(BlockControllerType.BUS);
    }

    /**
     * Creates the bus-specific model.
     * @return The model for the bus.
     */
    @Override
    protected IControllerQueryableBlockModel createBlockModel() {
        return new BusModel();
    }

    /**
     * Used from the view if it wants to update Data in the model.
     * @param data The data that should be set.
     */
    @Override
    public void setData(IDataElement data) {
        //ToDo
        ((BusModel) getModel()).setBelongingBusSystemModel(null);
    }

}
