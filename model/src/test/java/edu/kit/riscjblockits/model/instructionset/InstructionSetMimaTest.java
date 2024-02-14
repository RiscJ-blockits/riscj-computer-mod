package edu.kit.riscjblockits.model.instructionset;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InstructionSetMimaTest {

    private final String[] commands = new String[] {"LDC", "ADD", "LDV", "STV", "AND", "OR", "XOR", "HALT", "NOT", "RAR", "EQL", "JMP", "JMN"};
    @Test
    void test() {
        InstructionSetModel model = InstructionSetBuilderTest.buildInstructionSetModelMima();
        assertEquals("MIMA", model.getName());
        assertEquals(1, model.getIntegerRegister("IR"));
        for (String command : commands) {
            Instruction inst = model.getInstruction(command);
            assertNotNull(inst);
            for (MicroInstruction micro: inst.getExecution()) {
                assertNotNull(micro);
            }
        }
    }
}
