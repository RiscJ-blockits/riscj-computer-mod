package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;

public class ControlUnitModel extends BlockModel{

    private InstructionSetModel istModel;

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


    public InstructionSetModel getIstModel() {
        return istModel;
    }

    public void setIstModel(InstructionSetModel istModel) {
        this.istModel = istModel;
    }

}
