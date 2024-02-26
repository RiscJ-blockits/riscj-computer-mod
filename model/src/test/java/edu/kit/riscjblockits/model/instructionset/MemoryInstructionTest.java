package edu.kit.riscjblockits.model.instructionset;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MemoryInstructionTest {

    MemoryInstruction test = new MemoryInstruction(new String[]{"R1"}, "R2", "r");

    @Test
    void execute() {
        IExecutor executor = mock(IExecutor.class);
        test.execute(executor);
        verify(executor).execute(test);
    }

    @Test
    void getFlag() {
    }
}