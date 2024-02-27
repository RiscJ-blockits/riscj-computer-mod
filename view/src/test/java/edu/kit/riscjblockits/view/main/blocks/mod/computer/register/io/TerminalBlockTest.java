package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.view.main.TestSetupMain;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TestSetupMain.class)
class TerminalBlockTest {

    @Test
    void
    createTerminalBlock() {
        TerminalBlock terminalBlock = new TerminalBlock();
        assertEquals(0.0F, terminalBlock.getHardness());
    }

    @Test
    void createBlockEntity() {
        TerminalBlock terminalBlock = new TerminalBlock();
        TerminalBlockEntity terminalBlockEntity = (TerminalBlockEntity) terminalBlock.createBlockEntity(new BlockPos(0,0,0), null);
        assert terminalBlockEntity != null;
        assertFalse(terminalBlockEntity.isActive());
        assertFalse(terminalBlockEntity.hasWorld());
    }

}
