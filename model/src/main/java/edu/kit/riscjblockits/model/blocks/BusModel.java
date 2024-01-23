package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.busgraph.BusSystemModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataElement;

public class BusModel extends BlockModel{

    /**
     * The bus system this bus belongs to.
     */
    private BusSystemModel belongsToSystem;

    public BusModel() {
        setType(ModelType.BUS);
    }

    @Override
    public boolean hasUnqueriedStateChange() {
        return false;
    }

    /**
     * Getter for the data the view needs for ui.
     * @return Data Format: key: "active", value: "true" or "false"
     *                      key: "presentData", value: presentData as Hex String
     */
    @Override
    public IDataElement getData() {
        Data busData = new Data();
        if (belongsToSystem.getActiveVisualization(getPosition())) {
            busData.set("active", new DataStringEntry("true"));         //ToDo hier boolean data entry schlau, wollen wir eh nicht speichern
        } else {
            busData.set("active", new DataStringEntry("false"));
        }
        busData.set("presentData", new DataStringEntry(belongsToSystem.getPresentData().getHexadecimalValue()));
        return busData;
    }

    public void setBelongingBusSystemModel(BusSystemModel belongsToSystem) {
        this.belongsToSystem = belongsToSystem;
    }

}
