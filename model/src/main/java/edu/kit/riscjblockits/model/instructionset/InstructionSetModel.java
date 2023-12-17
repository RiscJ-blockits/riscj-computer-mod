package edu.kit.riscjblockits.model.instructionset;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstructionSetModel {
    private final String name;
    private final int instructionLength;

    private final String programCounterRegister;
    private final String[] aluRegisters;
    private final Map<String, Integer> floatRegisters;
    private final Map<String, Integer> integerRegisters;
    private final int memoryWordLength;
    private final int memoryAddressLength;
    private final String memoryByteOrder;
    private final String[] aluOperations;

    /**
     * map the command (e.g."ADD") to the corresponding arguments and translation
     */
    private final Map<String, Pair<String[], String[]>> commandArgumentsTranslationMap;

    /**
     * map the opcodes to all matching instructions with their corresponding micro-instructions and full translation
     */
    private final Map<String, List<Pair<MicroInstruction[], String[]>>> opcodeExecutionTranslationMap;

    InstructionSetModel() {
        this.name = null;
        this.instructionLength = 0;
        this.programCounterRegister = null;
        this.aluRegisters = null;
        this.floatRegisters = null;
        this.integerRegisters = null;
        this.memoryWordLength = 0;
        this.memoryAddressLength = 0;
        this.memoryByteOrder = null;
        this.aluOperations = null;
        this.commandArgumentsTranslationMap = null;
        this.opcodeExecutionTranslationMap = null;
    }
    InstructionSetModel(String name, int instructionLength,
                               String programCounterRegister,
                               String[] aluRegisters,
                               Map<String, Integer> floatRegisters,
                               Map<String, Integer> integerRegisters,
                               int memoryWordLength,
                               int memoryAddressLength,
                               String memoryByteOrder,
                               String[] aluOperations,
                               Map<String, Pair<String[], String[]>> commandArgumentsTranslationMap,
                               Map<String, List<Pair<MicroInstruction[], String[]>>> opcodeExecutionTranslationMap) {
        this.name = name;
        this.instructionLength = instructionLength;
        this.programCounterRegister = programCounterRegister;
        this.aluRegisters = aluRegisters;
        this.floatRegisters = floatRegisters;
        this.integerRegisters = integerRegisters;
        this.memoryWordLength = memoryWordLength;
        this.memoryAddressLength = memoryAddressLength;
        this.memoryByteOrder = memoryByteOrder;
        this.aluOperations = aluOperations;
        this.commandArgumentsTranslationMap = commandArgumentsTranslationMap;
        this.opcodeExecutionTranslationMap = opcodeExecutionTranslationMap;
    }

    public String getName() {
        return name;
    }

    public String[] getAluRegisters() {
        return aluRegisters;
    }

    public Map<String, Integer> getIntegerRegisters() {
        return integerRegisters;
    }

    public Map<String, Pair<String[], String[]>> getCommandArgumentsTranslationMap() {
        return commandArgumentsTranslationMap;
    }
}