package edu.kit.riscjblockits.controller.blocks.io;

import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static edu.kit.riscjblockits.model.blocks.RegisterModel.DEFAULT_WORD_LENGTH;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_ALU_REGS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_FOUND;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_MISSING;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_INPUT;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_IN_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_MODE_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_OUT_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WORD_LENGTH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TerminalModeControllerTest {

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
        registerModel = (RegisterModel) terminalModeController.getModel();
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

    @Test
    void setData() {
        Data choseData = new Data();
        choseData.set("missing", new DataStringEntry("R1 R2 R3"));
        choseData.set("found", new DataStringEntry("R3 R4 R5"));
        data.set("registers", choseData);
        data.set(REGISTER_WORD_LENGTH, new DataStringEntry("32"));
        terminalModeController.setData(data);
        data = (Data) registerModel.getData();
        assertEquals("32", ((DataStringEntry) (data.get(REGISTER_WORD_LENGTH))).getContent());
        choseData = (Data) data.get("registers");
        assertEquals("R1 R2 R3", ((DataStringEntry) (choseData.get("missing"))).getContent());
        assertEquals("R3 R4 R5", ((DataStringEntry) (choseData.get("found"))).getContent());
    }

    @Test
    void setWrongLengthData() {
        data.set(REGISTER_WORD_LENGTH, new DataStringEntry(""));
        terminalModeController.setData(data);
        assertEquals(String.valueOf(DEFAULT_WORD_LENGTH), ((DataStringEntry) ((Data) registerModel.getData()).get(REGISTER_WORD_LENGTH)).getContent());
    }

    @Test
    void setValueWithoutWord() {
        data.set(REGISTER_VALUE, new DataStringEntry("33"));
        terminalModeController.setData(data);
        assertEquals("", ((DataStringEntry) ((Data) registerModel.getData()).get(REGISTER_VALUE)).getContent());
    }

    @Test
    void setWrongValue() {
        data.set(REGISTER_WORD_LENGTH, new DataStringEntry("4"));
        data.set(REGISTER_VALUE, new DataStringEntry("sdkjfh"));
        terminalModeController.setData(data);
        assertEquals("", ((DataStringEntry) ((Data) registerModel.getData()).get(REGISTER_VALUE)).getContent());
    }

    @Test
    void setTerminalInputValue() {
        data.set(REGISTER_TERMINAL_INPUT, new DataStringEntry("10"));
        data.set(REGISTER_WORD_LENGTH, new DataStringEntry("4"));
        terminalModeController.setData(data);
        assertEquals("000010", terminalInputController.getValue().getHexadecimalValue());
    }

    @Test
    void setTerminalInputValueWrong() {
        data.set(REGISTER_TERMINAL_INPUT, new DataStringEntry("10"));
        //missing word length
        terminalModeController.setData(data);
        assertEquals("", terminalInputController.getValue().getHexadecimalValue());
    }

    @Test
    void setClusteringDataWrong() {
        Data choseData = new Data();
        choseData.set(REGISTER_MISSING, new DataStringEntry("R1 R2 R3"));
        //missing second data
        data.set(REGISTER_REGISTERS, choseData);
        terminalModeController.setData(data);
        data = (Data) registerModel.getData();
        choseData = (Data) data.get(REGISTER_REGISTERS);
        assertEquals(0, choseData.getKeys().size());
    }

}
