package edu.kit.riscjblockits.view.main.ai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AiProgrammerTest {

    @Test
    void queryAiWithoutKey() {
        AiProgrammer aiProgrammer = new AiProgrammer("");
        assertEquals(AiProgrammer.ERROR_KEY_NOT_FOUND, aiProgrammer.queryAi("test"));
    }

    @Test
    void queryAiWithWrongKey() {
        AiProgrammer aiProgrammer = new AiProgrammer("xxx");
        assertEquals(AiProgrammer.ERROR_MESSAGE, aiProgrammer.queryAi("test"));
    }

}
