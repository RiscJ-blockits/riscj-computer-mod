package edu.kit.riscjblockits.controller.blocks.io;

import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TerminalInputControllerTest {

    static TerminalModeController terminalModeController;
    static TerminalOutputController terminalOutputController;
    static TerminalInputController terminalInputController;
    static RegisterModel registerModel;
    static Data data;

    @BeforeEach
    void setUp() {
        IConnectableComputerBlockEntity blockEntity = mock(IConnectableComputerBlockEntity.class);
        terminalInputController = new TerminalInputController(blockEntity);
        terminalOutputController = new TerminalOutputController(blockEntity);
        terminalModeController = new TerminalModeController(blockEntity, terminalInputController, terminalOutputController);
        registerModel = (RegisterModel) terminalInputController.getModel();
        data = new Data();
    }

    @Test
    void addInput() {
        terminalInputController.addInput("a");
        terminalInputController.addInput("a");
        terminalInputController.setNextValue();
        assertEquals("000061", terminalInputController.getValue().getHexadecimalValue());
        terminalInputController.setNextValue();
        assertEquals("000061", terminalInputController.getValue().getHexadecimalValue());
        terminalInputController.setNextValue();
        assertEquals("000000", terminalInputController.getValue().getHexadecimalValue());
    }

}
