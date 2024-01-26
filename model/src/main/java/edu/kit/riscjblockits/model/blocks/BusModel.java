package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.busgraph.BusSystemModel;
import edu.kit.riscjblockits.model.data.IDataElement;

public class BusModel extends BlockModel{

    /**
     * The bus system this bus belongs to.
     */
    private BusSystemModel belongsToSystem;

    public BusModel() {
        setType(ModelType.BUS);
        getPosition().setBus(true);
    }

    @Override
    public boolean hasUnqueriedStateChange() {
        return false;
    }

    /**
     * Returns the data the view needs to display.
     * @return the data the view needs to display
     */
    @Override
    public IDataElement getData() {
        //ToDo Asks the BusSystemModel for data and visualisation
        belongsToSystem.getActiveVisualization(getPosition());
        return null;
    }

    public void setBelongingBusSystemModel(BusSystemModel belongsToSystem) {
        this.belongsToSystem = belongsToSystem;
    }

}
