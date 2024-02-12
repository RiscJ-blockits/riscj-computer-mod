package edu.kit.riscjblockits.controller.blocks.io;

import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

public class TerminalInputController extends RegisterController {

    private String input = "";      //ToDo beim reloaded speichern

    public TerminalInputController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    public void setNewValue(Value value) {
        //we allow this here because at its core this is just a normal register and thus is treated as one.
        ((RegisterModel) getModel()).setValue(value);
    }

    public void addInput(String input) {
        this.input += input;
    }

    public void setNextValue() {
        if (input.isEmpty()) {
            return;
        }
        char nextChar = input.charAt(0);
        String hexInput = Integer.toHexString(nextChar);
        ((RegisterModel) getModel()).setValue(Value.fromHex(hexInput, 2));
        input = input.substring(1);
    }

    public void resetInput() {
        input = "";
    }

}
