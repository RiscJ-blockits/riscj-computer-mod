package edu.kit.riscjblockits.model.instructionset;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class InstructionTest {
    @Test
    void testFilling() {
        Instruction instruction = new Instruction(new String[]{"[addr]", "[const]"}, "",
                new MicroInstruction[]{
                    new MemoryInstruction(new String[]{"A"}, "[const]", "r"),
                    new MemoryInstruction(new String[]{"[addr]"}, "[const]", "r")
                },
                new String[]{"[addr]<11:5>", "[const]<4>", "[addr]<4:0>"});
        Instruction clonedInstruction = new Instruction(instruction, "1111111000011111", new HashMap<>(), new HashMap<>());
        assertEquals("A", clonedInstruction.getExecution()[0].getFrom()[0]);
        assertEquals("0000", clonedInstruction.getExecution()[0].getTo());
        assertEquals("111111111111", clonedInstruction.getExecution()[1].getFrom()[0]);
    }

    @Test
    void testFillingWithConstantTranslation() {
        Instruction instruction = new Instruction(new String[]{"[addr]", "[const]"}, "",
                new MicroInstruction[]{
                        new MemoryInstruction(new String[]{"A"}, "[const]", "r"),
                        new MemoryInstruction(new String[]{"[addr]"}, "[const]", "r")
                },
                new String[]{"2222", "[addr]<11:5>", "[const]<4>", "[addr]<4:0>"});
        Instruction clonedInstruction = new Instruction(instruction, "22221111111000011111", new HashMap<>(), new HashMap<>());
        assertEquals("A", clonedInstruction.getExecution()[0].getFrom()[0]);
        assertEquals("0000", clonedInstruction.getExecution()[0].getTo());
        assertEquals("111111111111", clonedInstruction.getExecution()[1].getFrom()[0]);
    }
}