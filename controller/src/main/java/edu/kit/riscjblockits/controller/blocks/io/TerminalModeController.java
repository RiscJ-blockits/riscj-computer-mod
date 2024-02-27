package edu.kit.riscjblockits.controller.blocks.io;

import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.controller.clustering.ClusterHandler;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_FOUND;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_MISSING;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_INPUT;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_IN_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_MODE_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_OUT_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMNAL_MODE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WORD_LENGTH;

/**
 * This is the main controller for the terminal block.
 * It coordinates between the three controllers.
 * It also manages the mode register part of the terminal block.
 */
public class TerminalModeController extends RegisterController {

    private final TerminalInputController inputController;
    private final TerminalOutputController outputController;

    /**
     * Constructor for the TerminalModeController.
     * @param blockEntity The block entity that is connected to this controller.
     * @param inputController The controller for the input register.
     * @param outputController The controller for the output register.
     */
    public TerminalModeController(IConnectableComputerBlockEntity blockEntity, TerminalInputController inputController, TerminalOutputController outputController) {
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
        if (input == 1) {
            inputController.setNextValue();
        }
        if (input == 2) {
            inputController.resetInput();
        }
    }

    @Override
    public void startClustering(BlockPosition pos) {
        super.startClustering(pos);
        inputController.getModel().setPosition(pos);
        outputController.getModel().setPosition(pos);
        //This is the controller that gets first registered with the cluster. It needs to start the clustering in the other controllers
        ClusterHandler cluster = getClusterHandler();
        inputController.setClusterHandler(cluster);
        outputController.setClusterHandler(cluster);
        cluster.addBlocks(inputController);
        cluster.addBlocks(outputController);
    }

    @Override
    public void setData(IDataElement data) {
        getModel().onStateChange();
        if (!data.isContainer()) {
            return;
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            switch (s) {
                case REGISTER_TYPE -> { //a new register type was set on the screen
                    String type = ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
                    String first;
                    String second;
                    try {
                        first = type.split("_")[0];
                        second = type.split("_")[1];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                    if (first.equals("In")) {
                        IDataContainer input = new Data();
                        input.set(REGISTER_TYPE, new DataStringEntry(second));
                        inputController.setData(input);
                    } else if (first.equals("Out")) {
                        IDataContainer input = new Data();
                        input.set(REGISTER_TYPE, new DataStringEntry(second));
                        outputController.setData(input);
                    } else if (first.equals("Mode")) {
                        ((RegisterModel) getModel()).setRegisterType(second);
                    }
                    if (getClusterHandler() != null) {
                        this.getClusterHandler().checkFinished();
                    }
                    return;
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
                    ((RegisterModel) outputController.getModel()).setMissingAvailableRegisters(
                        missingAvailableRegisters);
                    ((RegisterModel) inputController.getModel()).setMissingAvailableRegisters(
                        missingAvailableRegisters);
                }
                case REGISTER_WORD_LENGTH -> {
                    int wordLength;
                    try {
                        wordLength = Integer.parseInt(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent());
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    ((RegisterModel) getModel()).setWordLength(wordLength);
                    ((RegisterModel) outputController.getModel()).setWordLength(wordLength);
                    ((RegisterModel) inputController.getModel()).setWordLength(wordLength);
                }
                case REGISTER_VALUE, REGISTER_TERMNAL_MODE -> {
                    int wordLength;
                    Value value;
                    try {
                        wordLength = Integer.parseInt(((IDataStringEntry) ((IDataContainer) data).get(REGISTER_WORD_LENGTH)).getContent());
                        value = Value.fromHex(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent(), wordLength);
                    } catch (NumberFormatException | ClassCastException | NullPointerException e) {
                        continue;
                    }
                    ((RegisterModel) getModel()).setValue(value);
                }
                case REGISTER_TERMINAL_INPUT -> {
                    int wordLength;
                    Value value;
                    try {
                        wordLength = Integer.parseInt(
                            ((IDataStringEntry) ((IDataContainer) data).get(REGISTER_WORD_LENGTH)).getContent());
                        value = Value.fromHex(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent(), wordLength);
                    } catch (NumberFormatException | ClassCastException | NullPointerException e) {
                        continue;
                    }
                    inputController.setNewValue(value);
                }
                case REGISTER_TERMINAL_IN_TYPE -> {
                    String type = ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
                    IDataContainer input = new Data();
                    input.set(REGISTER_TYPE, new DataStringEntry(type));
                    inputController.setData(input);
                }
                case REGISTER_TERMINAL_OUT_TYPE -> {
                    String type = ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
                    IDataContainer input = new Data();
                    input.set(REGISTER_TYPE, new DataStringEntry(type));
                    outputController.setData(input);
                }
                case REGISTER_TERMINAL_MODE_TYPE -> {
                    String type = ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
                    ((RegisterModel) getModel()).setRegisterType(type);
                }
                default -> {
                    //do nothing
                }
            }
        }
    }

}
