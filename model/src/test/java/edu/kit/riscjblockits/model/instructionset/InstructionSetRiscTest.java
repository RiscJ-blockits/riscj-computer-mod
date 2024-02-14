package edu.kit.riscjblockits.model.instructionset;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InstructionSetRiscTest {

    String[] commands = new String[]{"jal", };

    @Test
    void test() {
        InstructionSetModel model = InstructionSetBuilder.buildInstructionSetModelRiscV();
        Instruction instruction = model.getInstruction("jal");
        assertNotNull(instruction.getOpcode());
    }
}
