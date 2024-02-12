package edu.kit.riscjblockits.controller.blocks.io;

import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_INPUT;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMNAL_MODE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WORD_LENGTH;

//main terminal controller
public class TerminalModeController extends RegisterController {

    private final TerminalInputController inputController;
    private final RegisterController outputController;

    public TerminalModeController(IConnectableComputerBlockEntity blockEntity, TerminalInputController inputController, RegisterController outputController) {
        super(blockEntity);
        this.inputController = inputController;
        this.outputController = outputController;
    }

    @Override
    public void setNewValue(Value value) {
        int input;
        try {
            input = Integer.parseInt(value.getHexadecimalValue(), 16);
        } catch (NumberFormatException e) {
            return;
        }
        System.out.println("input: " + input);
        if (input == 1) {
            inputController.setNextValue();
        }
        if (input == 2) {
            inputController.resetInput();
        }
        //((RegisterModel) getModel()).setValue(value);
    }

    @Override
    public void startClustering(BlockPosition pos) {
        super.startClustering(pos);
        //This is the controller that gets first registered with the cluster. It needs to start the clustering in the other controllers
        inputController.startClustering(pos);
        outputController.startClustering(pos);
    }

    @Override
    public void setData(IDataElement data) {
        super.setData(data);
        inputController.setData(data);
        outputController.setData(data);
        if (!data.isContainer()) {
            return;
        }
        int wordLength;
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(REGISTER_TERMNAL_MODE)) {
                try {
                    wordLength = Integer.parseInt(((IDataStringEntry) ((IDataContainer) data).get(REGISTER_WORD_LENGTH)).getContent());
                } catch (NumberFormatException e) {
                    continue;
                }
                Value value = Value.fromHex(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent(), wordLength);
                ((RegisterModel) getModel()).setValue(value);
            } else if (s.equals(REGISTER_TERMINAL_INPUT)) {
                try {
                    wordLength = Integer.parseInt(((IDataStringEntry) ((IDataContainer) data).get(REGISTER_WORD_LENGTH)).getContent());
                } catch (NumberFormatException e) {
                    continue;
                }
                Value value = Value.fromHex(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent(), wordLength);
                inputController.setNewValue(value);
            }
        }
    }

}
