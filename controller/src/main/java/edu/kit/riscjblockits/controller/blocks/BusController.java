package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.BusModel;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.busgraph.BusSystemModel;
import edu.kit.riscjblockits.model.busgraph.IQueryableBusSystem;
import edu.kit.riscjblockits.model.data.IDataElement;

import java.util.List;

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
        super(blockEntity, BlockControllerType.BUS);
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
        //Todo noch leer //remove?
    }

    public List<BlockPosition> getBusSystemNeighbors() {
        if (getClusterHandler() == null) {
            return null;
        }
        return getClusterHandler().getBusSystemModel().getBusSystemNeighbors(getModel().getPosition());
    }

    /**
     * Sets the BusSystemModel in the Model that the bus belongs to.
     * @param busSystemModel The bus system model that the bus belongs to.
     */
    public void setBusSystemModel(BusSystemModel busSystemModel) {
        ((BusModel) getModel()).setBelongingBusSystemModel(busSystemModel);
    }

}
