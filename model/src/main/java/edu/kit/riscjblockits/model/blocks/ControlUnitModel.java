package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;

public class ControlUnitModel extends BlockModel{

    private IQueryableInstructionSetModel istModel;

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
        return null;
    }


    public IQueryableInstructionSetModel getIstModel() {
        return istModel;
    }

    public void setIstModel(IQueryableInstructionSetModel istModel) {
        this.istModel = istModel;
    }

}
