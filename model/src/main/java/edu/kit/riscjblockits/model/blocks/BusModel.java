package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.busgraph.BusSystemModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataElement;

import static edu.kit.riscjblockits.model.data.DataConstants.BUS_ACTIVE;
import static edu.kit.riscjblockits.model.data.DataConstants.BUS_DATA;

/**
 * Represents the data and state of a bus. Every bus has one.
 */
public class BusModel extends BlockModel{

    /**
     * The bus system this bus belongs to. A bus system is a network of buses and connected computer blocks.
     */
    private BusSystemModel belongsToSystem;

    /**
     * Constructor. Returns the model for a bus.
     */
    public BusModel() {
        setType(ModelType.BUS);
    }

    @Override
    public void setPosition(BlockPosition position) {
        position.setBus(true);
        super.setPosition(position);
    }

    @Override
    public boolean hasUnqueriedStateChange() {
        //is her always true, because bus data changes almost every tick anyway
        return true;
    }

    /**
     * Getter for the data the view needs for ui.
     * @return Data Format: key: "active", value: "true" or "false"
     *                      key: "presentData", value: presentData as Hex String
     */
    @Override
    public IDataElement getData() {
        Data busData = new Data();
        if (belongsToSystem != null && belongsToSystem.getActiveVisualization(getPosition())) {
            busData.set(BUS_ACTIVE, new DataStringEntry("true"));
            //ToDo hier boolean data entry schlau, wollen wir eh nicht speichern
        } else {
            busData.set(BUS_ACTIVE, new DataStringEntry("false"));
        }
        if (belongsToSystem != null) {
            busData.set(BUS_DATA, new DataStringEntry(belongsToSystem.getPresentData().getHexadecimalValue()));
        }
        setUnqueriedStateChange(false);
        return busData;
    }

    /**
     * Sets the bus system this bus belongs to. A bus system is a network of buses and connected computer blocks.
     * @param belongsToSystem The bus system this bus belongs to.
     */
    public void setBelongingBusSystemModel(BusSystemModel belongsToSystem) {
        this.belongsToSystem = belongsToSystem;
    }

    /**
     * Asks the Bus system if the bus is transporting data.
     * Gets called every tick and it used here to also
     * @return true if the bus is transporting data
     */
    @Override
    public boolean getVisualisationState() {
        if(this.belongsToSystem == null) return false;
        return belongsToSystem.getActiveVisualization(getPosition());
    }

    /**
     * Getter for the bus system this bus belongs to.
     * @return The bus system this bus belongs to.
     */
    public BusSystemModel getBelongsToSystem() {
        return belongsToSystem;
    }
}
