package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;

import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_CLUSTERING;
import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_ITEM_PRESENT;

/**
 * Represents the data and state of a control unit. Every control unit block has one.
 */
public class ControlUnitModel extends BlockModel{

    /**
     * The Instruction Set Model defines all behavior of the computer.
     * Is null if no Instruction Set Model Item is inserted in the control unit block.
     */
    private IQueryableInstructionSetModel istModel;

    /**
     * Holds the Data which blocks are missing for a valid architecture.
     * Is used to display the information in the view.
     * Can be null if the ClusterHandler has not checked the architecture.
     */
    private IDataContainer clusteringData;

    /**
     * Constructor. Returns the model for a control unit.
     */
    public ControlUnitModel() {
        super();
        setType(ModelType.CONTROL_UNIT);
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
        if (istModel != null) {
            //ToDo auch hier wäre boolean besser
            cuData.set(CONTROL_ITEM_PRESENT, new DataStringEntry("true"));
        } else {
            cuData.set(CONTROL_ITEM_PRESENT, new DataStringEntry("false"));
        }
        return cuData;
    }

    public IQueryableInstructionSetModel getIstModel() {
        return istModel;
    }

    /**
     * Setter for the Instruction Set Model.
     * @param istModel the Instruction Set Model to set
     * @return true if the istModel has changed, false if not
     */
    public boolean setIstModel(IQueryableInstructionSetModel istModel) {
        if (this.istModel == null || !this.istModel.equals(istModel)) {
            this.istModel = istModel;
            setUnqueriedStateChange(true);
            return true;
        }
        //just to be safe
        this.istModel = istModel;
        setUnqueriedStateChange(true);
        return false;
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
