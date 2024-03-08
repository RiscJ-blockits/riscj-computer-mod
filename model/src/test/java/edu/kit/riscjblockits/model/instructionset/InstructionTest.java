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

    @Test
    void testDetectionOfBinaryBGE() {
        InstructionSetModel model = InstructionSetBuilderTest.buildInstructionSetModelRiscV();
        Instruction instruction = model.getInstruction("bge");
        assertTrue(instruction.matchesBinary("00000000010101000101000001100011"));
    }

    @Test
    void testDetectionOfBinaryJAL() {
        InstructionSetModel model = InstructionSetBuilderTest.buildInstructionSetModelRiscV();
        Instruction instruction = model.getInstruction("jal");
        assertTrue(instruction.matchesBinary("00100011010000000001001101101111"));
    }

    @Test
    void testFillingBGE() {
        InstructionSetModel model = InstructionSetBuilderTest.buildInstructionSetModelRiscV();
        Instruction instruction = model.getInstruction("bge");
        HashMap<Integer, String> intRegisters = new HashMap<>();
        intRegisters.put(8, "s0");
        intRegisters.put(5, "t0");
        Instruction clonedInstruction = new Instruction(instruction, "00000000010101000101000001100011", intRegisters, new HashMap<>());
        ConditionedInstruction conditionedInstruction = (ConditionedInstruction) clonedInstruction.getExecution()[3];
        assertEquals("s0", conditionedInstruction.getCondition().getCompare1());
        assertEquals("t0", conditionedInstruction.getCondition().getCompare2());
    }
}