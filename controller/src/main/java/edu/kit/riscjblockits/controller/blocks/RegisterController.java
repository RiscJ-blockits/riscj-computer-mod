package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

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
        /* Data Format: key: "type", value: "registerType"
        *               key: "registers", value: container
        *                                  container:   key: "missing", value: string space-separated register names
        *                                               key: "found", value: string with space-separated register names
        *               key: "word", value: string with word length
        *               key: "value", value: string with value
        */
        if (!data.isContainer()) {
            return;
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            switch (s) {
                case "type" -> {
                    String type = ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
                    ((RegisterModel) getModel()).setRegisterType(type);
                }
                case "registers" -> {
                    IDataContainer registers = (IDataContainer) ((IDataContainer) data).get(s);
                    String[] missingAvailableRegisters = new String[2];
                    missingAvailableRegisters[0] = ((IDataStringEntry) registers.get("missing")).getContent();
                    missingAvailableRegisters[1] = ((IDataStringEntry) registers.get("found")).getContent();
                    ((RegisterModel) getModel()).setMissingAvailableRegisters(missingAvailableRegisters);
                }
                case "word" -> {
                    int wordLength = Integer.parseInt(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent());
                    ((RegisterModel) getModel()).setWordLength(wordLength);
                }
                case "value" -> {
                    int wordLength;
                    try {
                        wordLength = Integer.parseInt(((IDataStringEntry) ((IDataContainer) data).get("word")).getContent());
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    Value value = Value.fromHex(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent(), wordLength);
                    ((RegisterModel) getModel()).setValue(value);
                }
            }
        }
    }

}
