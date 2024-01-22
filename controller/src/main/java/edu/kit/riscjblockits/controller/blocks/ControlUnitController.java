package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.clustering.ClusterHandler;
import edu.kit.riscjblockits.model.blocks.ControlUnitModel;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;

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
     * @return An {@link InstructionSetModel} object.
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
                //ToDo add method to update Ist Model

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
        boolean success =  clusterHandler.setIstModel(((ControlUnitModel) getModel()).getIstModel());
        //ToDo tell the player if this istModel is not the one that is used
    }

}
