package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.clustering.ClusterHandler;
import edu.kit.riscjblockits.model.blocks.ControlUnitModel;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;

import java.io.UnsupportedEncodingException;

/**
 * The controller for the control unit block.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class ControlUnitController extends ComputerBlockController{

    /**
     * Creates a new ControlUnitController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public ControlUnitController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity, BlockControllerType.CONTROL_UNIT);
    }

    /**
     * Creates the ControlUnit specific model.
     * @return The model for the ControlUnit.
     */
    @Override
    protected IControllerQueryableBlockModel createBlockModel() {
        return new ControlUnitModel();
    }

    /**
     * Returns the instruction set model inside the inventory of the block entity.
     * @return An {@link InstructionSetModel} object. Could be null.
     */
    public IQueryableInstructionSetModel getInstructionSetModel() {
        return ((ControlUnitModel)getModel()).getIstModel();
    }

    /**
     * Used from the view if it wants to update Data in the model.
     * @param data The data that should be set.
     */
    @Override
    public void setData(IDataElement data) {
        /* Data Format: key: "clustering", value: container
         *                                  key: "missingRegisters", value: string space-separated register names
         *                                  key: "foundRegisters", value: string space-separated register names
         *                                  key: "foundMemory", value: string with number of memory blocks
         *                                  key: "foundAlu", value: string with number of alu blocks
         *                                  key: "foundControlUnit", value: string with number of control unit blocks
         *                                  key: "foundSystemClock", value: string with number of system clock blocks
         *              key: "istModel", value: ToDo
         */
        if (!data.isContainer()) {
            return;
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals("clustering")) {
                ((ControlUnitModel) getModel()).setClusteringData(((IDataContainer) data).get(s));
            } else if (s.equals("istModel")) {
                if (((IDataContainer) data).get(s) == null) {           //istModel has been removed
                    ((ControlUnitModel) getModel()).setIstModel(null);
                    updateClusterHandler();
                    return;
                }
                ((IDataContainer) ((IDataContainer) data).get(s)).get("riscj_blockits.instructionSet");
                String ist = ((IDataStringEntry) ((IDataContainer) ((IDataContainer) data).get(s)).get("riscj_blockits.instructionSet")).getContent();
                IQueryableInstructionSetModel istModel = null;
                try {
                    istModel = InstructionSetBuilder.buildInstructionSetModel(ist);
                } catch (UnsupportedEncodingException e) {
                    return;
                }
                ((ControlUnitModel) getModel()).setIstModel(istModel);
                if (getClusterHandler() != null) {
                    updateClusterHandler();
                }
            }
        }
    }

    /**
     * We need to override this method because we need to update the IstModel in the ClusterHandler when a new Cluster is created.
     * @param clusterHandler The ClusterHandler that should be associated with the controller.
     */
    @Override
    public void setClusterHandler(ClusterHandler clusterHandler) {
        super.setClusterHandler(clusterHandler);
        //if we have multiple control units, the fastest gets to set the IstModel
        updateClusterHandler();
    }

    /**
     * Updates the IstModel in the ClusterHandler.
     */
    private void updateClusterHandler() {
        //To Do update if cluster handler is added later (should already work)
        boolean success = getClusterHandler().setIstModel(((ControlUnitModel) getModel()).getIstModel());
        if (!success) {
            ((ControlUnitModel) getModel()).setIstModel(null);      //ToDo schöner machen?
        }
    }

    /**
     * Removes the IstModel from the model.
     */
    public void rejectIstModel() {
        ((ControlUnitModel) getModel()).setIstModel(null);
    }

}
