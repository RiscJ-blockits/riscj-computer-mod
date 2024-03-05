package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_ALU_REGS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_FOUND;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_MISSING;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WORD_LENGTH;

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
        super.setData(data);
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
                case REGISTER_TYPE -> {
                    String type = ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
                    ((RegisterModel) getModel()).setRegisterType(type);
                    if (getClusterHandler() != null) {
                        this.getClusterHandler().checkFinished();
                    }
                }
                case REGISTER_REGISTERS -> {
                    IDataContainer registers = (IDataContainer) ((IDataContainer) data).get(s);
                    String[] missingAvailableRegisters = new String[2];
                    try {
                        missingAvailableRegisters[0] = ((IDataStringEntry) registers.get(REGISTER_MISSING)).getContent();
                        missingAvailableRegisters[1] = ((IDataStringEntry) registers.get(REGISTER_FOUND)).getContent();
                    } catch (ClassCastException e) {
                        continue;
                    }
                    ((RegisterModel) getModel()).setMissingAvailableRegisters(missingAvailableRegisters);
                    if (registers.get(REGISTER_ALU_REGS) instanceof IDataStringEntry) {
                        String[] aluRegs = ((IDataStringEntry) registers.get(REGISTER_ALU_REGS)).getContent().split(" ");
                        ((RegisterModel) getModel()).setAluRegisterNames(aluRegs);
                    }
                }
                case REGISTER_WORD_LENGTH -> {
                    int wordLength;
                    try {
                        wordLength = Integer.parseInt(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent());
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    ((RegisterModel) getModel()).setWordLength(wordLength);
                }
                case REGISTER_VALUE -> {
                    int wordLength;
                    Value value;
                    try {
                        wordLength = Integer.parseInt(((IDataStringEntry) ((IDataContainer) data).get(REGISTER_WORD_LENGTH)).getContent());
                        value = Value.fromHex(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent(), wordLength);
                    } catch (NumberFormatException | ClassCastException e) {
                        continue;       //we cant set value without a word length
                    }
                    ((RegisterModel) getModel()).setValue(value);
                }
                default -> {
                    //do nothing
                }
            }
        }
    }

}
