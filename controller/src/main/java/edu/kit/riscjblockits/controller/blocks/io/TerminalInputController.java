package edu.kit.riscjblockits.controller.blocks.io;

import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

/**
 * This class is a controller for the input register inside the terminal block.
 * Inside the input register is the input of the user.
 * The next char from that input can be put inside the register model by setting the mode register.
 */
public class TerminalInputController extends RegisterController {

    private String input = "";      //ToDo beim reloaded speichern

    /**
     * Constructor for the TerminalInputController.
     * @param blockEntity The block entity that is connected to this controller.
     */
    public TerminalInputController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    public void setNewValue(Value value) {
        //we allow this here because at its core this is just a normal register and thus is treated as one.
        ((RegisterModel) getModel()).setValue(value);
    }

    /**
     * Adds input from the user to the stored input.
     * @param input The input to be added.
     */
    public void addInput(String input) {
        this.input += input;
    }

    /**
     * Puts the next value from the input inside the register model.
     */
    public void setNextValue() {
        if (input.isEmpty()) {
            ((RegisterModel) getModel()).setValue(Value.fromHex("00", 2));
            return;
        }
        char nextChar = input.charAt(0);
        String hexInput = Integer.toHexString(nextChar);
        ((RegisterModel) getModel()).setValue(Value.fromHex(hexInput, 2));
        if (input.length() == 1) {
            input = "";
        }
        input = input.substring(1);
    }

    /**
     * Resets the input that has not been written to the register model.
     */
    public void resetInput() {
        input = "";
    }

}
