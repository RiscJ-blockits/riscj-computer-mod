package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;

import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_CLUSTERING;
import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_ITEM_PRESENT;

public class ControlUnitModel extends BlockModel{

    private IQueryableInstructionSetModel istModel;

    /** ToDo nicht im Entwurf
     * Holds the Data which blocks are missing for a valid architecture.
     * Is used to display the information in the view.
     */
    private IDataContainer clusteringData;

    public ControlUnitModel() {
        super();
        setType(ModelType.CONTROL_UNIT);
    }

    @Override
    public boolean hasUnqueriedStateChange() {
        //ToDo remove TestCode
        return true;
    }

    /**
     * Getter for the data the view needs for ui.
     * @return Data Format: key: "clustering", value: container
     *                                          key: "missingRegisters", value: string space-separated register names
     *                                          key: "foundRegisters", value: string space-separated register names
     *                                          key: "foundMemory", value: string with number of memory blocks
     *                                          key: "foundAlu", value: string with number of alu blocks
     *                                          key: "foundControlUnit", value: string with number of control unit blocks
     *                                          key: "foundControlUnit", value: string with number of control unit blocks
     *                      key: "istModel", value: "true" if istModel is set, "false" if not
     */
    @Override
    public IDataElement getData() {
        Data cuData = new Data();
        if (clusteringData != null) {
            cuData.set(CONTROL_CLUSTERING, clusteringData);
        }
        if (istModel != null) {             //ToDo auch hier wäre boolean besser
            cuData.set(CONTROL_ITEM_PRESENT, new DataStringEntry("true"));
        } else {
            cuData.set(CONTROL_ITEM_PRESENT, new DataStringEntry("false"));
        }
        setUnqueriedStateChange(false);
        return cuData;
    }

    public IQueryableInstructionSetModel getIstModel() {
        return istModel;
    }

    public void setIstModel(IQueryableInstructionSetModel istModel) {
        this.istModel = istModel;
        setUnqueriedStateChange(true);
    }

    /**
     * Setter for the clustering data.
     * @param clusteringData a Data container formatted as described in {@link #getData()}
     */
    public void setClusteringData(IDataElement clusteringData) {
        this.clusteringData = (IDataContainer) clusteringData;
        setUnqueriedStateChange(true);
    }

}
