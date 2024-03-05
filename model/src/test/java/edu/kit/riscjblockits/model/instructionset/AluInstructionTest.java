package edu.kit.riscjblockits.model.instructionset;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AluInstructionTest {

    AluInstruction test = new AluInstruction(new String[]{"R1"}, "R2", "r", null,"add");

    @Test
    void getAction() {
        assertEquals("add", test.getAction());
    }

    @Test
    void execute() {
        IExecutor executor = mock(IExecutor.class);
        test.execute(executor);
        verify(executor).execute(test);
    }
}