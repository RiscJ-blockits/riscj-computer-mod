package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.busgraph.BusSystemModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataElement;

import static edu.kit.riscjblockits.model.data.DataConstants.BUS_ACTIVE;
import static edu.kit.riscjblockits.model.data.DataConstants.BUS_DATA;

public class BusModel extends BlockModel{

    /**
     * The bus system this bus belongs to.
     */
    private BusSystemModel belongsToSystem;

    public BusModel() {
        setType(ModelType.BUS);
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
            busData.set(BUS_ACTIVE, new DataStringEntry("true"));         //ToDo hier boolean data entry schlau, wollen wir eh nicht speichern
        } else {
            busData.set(BUS_ACTIVE, new DataStringEntry("false"));
        }
        if (belongsToSystem != null) {
            busData.set(BUS_DATA, new DataStringEntry(belongsToSystem.getPresentData().getHexadecimalValue()));
        }
        setUnqueriedStateChange(false);
        return busData;
    }

    public void setBelongingBusSystemModel(BusSystemModel belongsToSystem) {
        this.belongsToSystem = belongsToSystem;
    }

}
