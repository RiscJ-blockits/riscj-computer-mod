package edu.kit.riscjblockits.controller.blocks.io;

import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_IN_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_MODE_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_OUT_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TYPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TerminalModeControllerTest {

    static TerminalModeController terminalModeController;
    static TerminalOutputController terminalOutputController;
    static TerminalInputController terminalInputController;
    static Data data;

    @BeforeEach
    void setUp() {
        IConnectableComputerBlockEntity blockEntity = mock(IConnectableComputerBlockEntity.class);
        terminalInputController = new TerminalInputController(blockEntity);
        terminalOutputController = new TerminalOutputController(blockEntity);
        terminalModeController = new TerminalModeController(blockEntity, terminalInputController, terminalOutputController);
        data = new Data();
    }

    @Test
    void setNewDataRegisterType() {
        data.set(REGISTER_TYPE, new DataStringEntry("In_test"));
        terminalModeController.setData(data);
        assertEquals("test", terminalInputController.getRegisterType());
        data.set(REGISTER_TYPE, new DataStringEntry("Out_test"));
        terminalModeController.setData(data);
        assertEquals("test", terminalOutputController.getRegisterType());
        data.set(REGISTER_TYPE, new DataStringEntry("Mode_test"));
        terminalModeController.setData(data);
        assertEquals("test", terminalModeController.getRegisterType());
    }

    @Test
    void setWrongNewDataRegisterType() {
        data.set(REGISTER_TYPE, new DataStringEntry("_test"));
        terminalModeController.setData(data);
        assertEquals(RegisterModel.UNASSIGNED_REGISTER, terminalModeController.getRegisterType());
        assertEquals(RegisterModel.UNASSIGNED_REGISTER, terminalOutputController.getRegisterType());
        assertEquals(RegisterModel.UNASSIGNED_REGISTER, terminalInputController.getRegisterType());
        data.set(REGISTER_TYPE, new DataStringEntry("gg_test"));
        terminalModeController.setData(data);
        assertEquals(RegisterModel.UNASSIGNED_REGISTER, terminalModeController.getRegisterType());
        assertEquals(RegisterModel.UNASSIGNED_REGISTER, terminalOutputController.getRegisterType());
        assertEquals(RegisterModel.UNASSIGNED_REGISTER, terminalInputController.getRegisterType());
        data.set(REGISTER_TYPE, new DataStringEntry(""));
        terminalModeController.setData(data);
        assertEquals(RegisterModel.UNASSIGNED_REGISTER, terminalModeController.getRegisterType());
        assertEquals(RegisterModel.UNASSIGNED_REGISTER, terminalOutputController.getRegisterType());
        assertEquals(RegisterModel.UNASSIGNED_REGISTER, terminalInputController.getRegisterType());
    }

    @Test
    void setRegisterTypes() {
        data.set(REGISTER_TERMINAL_IN_TYPE, new DataStringEntry("test"));
        terminalModeController.setData(data);
        assertEquals("test", terminalInputController.getRegisterType());
        data.set(REGISTER_TERMINAL_MODE_TYPE, new DataStringEntry("test"));
        terminalModeController.setData(data);
        assertEquals("test", terminalModeController.getRegisterType());
        data.set(REGISTER_TERMINAL_OUT_TYPE, new DataStringEntry("test"));
        terminalModeController.setData(data);
        assertEquals("test", terminalOutputController.getRegisterType());
    }

    @Test
    void setWrongRegisterTypes() {
        data.set(REGISTER_TERMINAL_IN_TYPE, new DataStringEntry(""));
        terminalModeController.setData(data);
        assertEquals("", terminalInputController.getRegisterType());
    }

    //ToDo copy test from register set Data Tests
    //ToDo add null test


}
