package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.data.IDataVisitor;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.IDataElement;

/**
 * The controller for a register block entity.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class RegisterController extends ComputerBlockController {

    /**
     * Creates a new RegisterController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public RegisterController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity, BlockControllerType.REGISTER);
    }

    /**
     * Creates the Register-specific model.
     * @return The model for the Register.
     */
    @Override
    protected IControllerQueryableBlockModel createBlockModel() {
        return new RegisterModel();
    }

    /**
     * Returns the register type. The types are defined in the Instruction Set Model.
     * @return The type of the register.
     */
    public String getRegisterType() {
        return ((RegisterModel)getModel()).getRegisterType();
    }

    /**
     * Getter for the current value inside the register.
     * @return The Value stored in the register.
     */
    public Value getValue() {
        return ((RegisterModel)getModel()).getValue();
    }

    /**
     * Setter for the value inside the register.
     * @param value The new value that should be stored in the register.
     */
    public void setNewValue(Value value) {
        //ToDo check input
        ((RegisterModel)getModel()).setValue(value);
    }

    /**
     * Used from the view if it wants to update Data in the model.
     * @param data The data that should be set.
     */
    @Override
    public void setData(IDataElement data) {
        //ToDo
        IDataVisitor visitor = new IDataVisitor() {
            @Override
            public void visit(IDataContainer dataContainer) {

            }
            @Override
            public void visit(IDataStringEntry dataStringEntry) {
                ((RegisterModel) getModel()).setRegisterType(dataStringEntry.getContent());
            }
        };
        data.receive(visitor);

        setNewValue(null);
    }

}
