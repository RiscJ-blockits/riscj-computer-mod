package edu.kit.riscjblockits.model.instructionset;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class InstructionSetRiscTest {

    String[] commands = new String[]{"jal", };

    @Test
    void test() {
        InstructionSetModel model = InstructionSetBuilderTest.buildInstructionSetModelRiscV();
        Instruction instruction = model.getInstruction("jal");
        assertNotNull(instruction.getOpcode());
    }
}
