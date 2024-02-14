package edu.kit.riscjblockits.model.instructionset;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ComplexMicroInstructionTest {

    @Test
    void getFilled() {
        ComplexMicroInstruction complexMicroInstruction = new DataMovementInstruction(new String[]{"[addr]", "[const]"}, "", "r", new MemoryInstruction(new String[]{"Mem[A]"}, "&[t0]", "r"));


        Map<String, String> argumentsInstructionMap = new HashMap<>();
        argumentsInstructionMap.put("[const]", "0x16");
        argumentsInstructionMap.put("[t0]", "00101");
        argumentsInstructionMap.put("[addr]", "0x17");
        HashMap<Integer, String> intRegisters = new HashMap<>();
        intRegisters.put(8, "s0");
        intRegisters.put(5, "t0");
        ComplexMicroInstruction filled = (ComplexMicroInstruction) complexMicroInstruction.getFilled(argumentsInstructionMap, intRegisters, null);
        assertEquals("r", filled.getMemoryFlag());
        assertEquals("Mem[A]", filled.getMemoryInstruction().getFrom()[0]);
        assertEquals("t0", filled.getMemoryInstruction().getTo());
    }
}