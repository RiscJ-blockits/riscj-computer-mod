package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.BusSystemModel;
import edu.kit.riscjblockits.model.data.IDataElement;

public class BusModel extends BlockModel{

    /**
     * The bus system this bus belongs to.
     */
    private BusSystemModel belongsToSystem;

    @Override
    public boolean hasUnqueriedStateChange() {
        return false;
    }

    /**
     *
     * @param dataElement
     */
    @Override
    public void writeDataRequest(IDataElement dataElement) {
        //ToDo Asks the BusSystemModel for data and visualisation
        belongsToSystem.getActiveVisualization(getPosition());
    }

    public void setBelongingBusSystemModel(BusSystemModel belongsToSystem) {
        this.belongsToSystem = belongsToSystem;
    }
}
