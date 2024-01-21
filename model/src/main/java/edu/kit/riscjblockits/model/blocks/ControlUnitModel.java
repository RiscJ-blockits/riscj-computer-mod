package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;

public class ControlUnitModel extends BlockModel{

    private IQueryableInstructionSetModel istModel;

    private IDataContainer clusteringData;

    public ControlUnitModel() {
        super();
        setType(ModelType.CONTROL_UNIT);
        //ToDo remove Test Code
        istModel = InstructionSetBuilder.buildInstructionSetModelMima();
    }

    @Override
    public boolean hasUnqueriedStateChange() {
        return false;
    }

    @Override
    public IDataElement getData() {
        //ToDo check if clusteringData is not null if we return it.

        return null;
    }


    public IQueryableInstructionSetModel getIstModel() {
        return istModel;
    }

    public void setIstModel(IQueryableInstructionSetModel istModel) {
        this.istModel = istModel;
    }

    public void setClusteringData(IDataElement clusteringData) {
        this.clusteringData = (IDataContainer) clusteringData;
    }

}
