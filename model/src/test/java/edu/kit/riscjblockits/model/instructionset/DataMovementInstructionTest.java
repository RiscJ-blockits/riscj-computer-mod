package edu.kit.riscjblockits.model.instructionset;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DataMovementInstructionTest {

    DataMovementInstruction test = new DataMovementInstruction(new String[]{"R1"}, "R2", "r", null);

    @Test
    void execute() {
        IExecutor executor = mock(IExecutor.class);
        test.execute(executor);
        verify(executor).execute(test);
    }
}